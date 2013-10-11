package com.opentext.textaxes;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Sets.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.runners.Parameterized;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.opentext.textaxes.utils.Arrays;

public class AxesRunner extends Parameterized
{
	public AxesRunner(Class<?> testClass) throws Throwable {
		super(testClass);
	}

	public static Iterable<Object[]> generateCartesianProduct(Class<?> clazz) {
		List<Method> axisMethods = newArrayList(getAnnotatedMethods(clazz, Axis.class));
		Collections.sort(axisMethods, new Comparator<Method>()
		{
			@Override
			public int compare(Method o1, Method o2) {
				return o1.getAnnotation(Axis.class).value() - o2.getAnnotation(Axis.class).value();
			}
		});

		List<Set<Object>> axes = transform(axisMethods, new Function<Method, Set<Object>>()
		{
			@Override
			public Set<Object> apply(Method m) {
				try {
					final Object invoke = m.invoke(null);
					if (invoke instanceof Iterable<?>) {
						return newHashSet((Iterable<?>) invoke);
					}
					else {
						throw new IllegalArgumentException(m + " does not return an Iterable<?>");
					}
				}
				catch (IllegalAccessException | InvocationTargetException ex) {
					throw new IllegalStateException(ex);
				}
			}
		});

		Set<List<Object>> resultSet = cartesianProduct(axes);

		Iterable<Method> excludeFilterMethods = getAnnotatedMethods(clazz, Excluding.class);

		List<Object[]> results = newArrayList();
		for (List<Object> paramsForSingleTest : resultSet) {
			final Object[] objects = paramsForSingleTest.toArray();

			if (Iterables.any(excludeFilterMethods, new Predicate<Method>()
			{
				@Override
				public boolean apply(Method m) {
					try {
						final Predicate<Annotation[]> anyAnnotationOfType = hasAnyAnnotationOfType(Axis.class);
						final Annotation[][] parameterAnnotations = m.getParameterAnnotations();
						if (Arrays.all(parameterAnnotations, anyAnnotationOfType)) {
							Object[] inputs = new Object[parameterAnnotations.length];
							int i = 0;
							for (Annotation[] annotations : parameterAnnotations) {
								Axis annotation = (Axis) Arrays.find(annotations, isAnnotationOfType(Axis.class));
								inputs[i++] = objects[annotation.value()];
							}

							return (boolean) m.invoke(null, inputs);
						}
						else if (!Arrays.any(parameterAnnotations, anyAnnotationOfType)) {
							return (boolean) m.invoke(null, objects);
						}
						else {
							throw new IllegalArgumentException("Either none or all parameters have to be annotated by @Axis");
						}
					}
					catch (IllegalAccessException | InvocationTargetException ex) {
						throw new IllegalStateException(ex);
					}
				}

			})) {
				continue;
			}
			results.add(objects);
		}

		return results;
	}

	private static Iterable<Method> getAnnotatedMethods(Class<?> clazz, final Class<? extends Annotation> annotation) {
		return Arrays.filter(clazz.getMethods(), new Predicate<Method>()
		{
			@Override
			public boolean apply(Method m) {
				return staticMethodWithAnnotation(m, annotation);
			}
		});
	}

	private static boolean staticMethodWithAnnotation(Method method, Class<? extends Annotation> annotationClass) {
		return ((method.getModifiers() & Modifier.STATIC) == Modifier.STATIC) && method.getAnnotation(annotationClass) != null;
	}

	private static Predicate<Annotation[]> hasAnyAnnotationOfType(final Class<? extends Annotation> type) {
		return new Predicate<Annotation[]>()
		{
			@Override
			public boolean apply(Annotation[] input) {
				return Arrays.any(input, isAnnotationOfType(type));
			}
		};
	}

	private static Predicate<Annotation> isAnnotationOfType(final Class<? extends Annotation> type) {
		return new Predicate<Annotation>()
		{
			@Override
			public boolean apply(Annotation input) {
				return input.annotationType() == type;
			}
		};
	}
}