package com.opentext.textaxes;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.junit.runners.Parameterized;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Sets.cartesianProduct;
import static com.google.common.collect.Sets.newHashSet;

public class AxesRunner extends Parameterized {
    public AxesRunner(Class<?> testClass) throws Throwable {
        super(testClass);
    }

    public static Iterable<Object[]> generateCartesianProduct(Class<?> clazz) {
        List<Method> axisMethods = newArrayList(getAnnotatedMethods(clazz, Axis.class));
        Collections.sort(axisMethods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getAnnotation(Axis.class).value() - o2.getAnnotation(Axis.class).value();
            }
        });

        List<Set<Object>> axes = transform(axisMethods, new Function<Method, Set<Object>>() {
            @Override
            public Set<Object> apply(Method m) {
                try {
                    return newHashSet((Iterable<?>) m.invoke(null));
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new IllegalStateException(ex);
                }
            }
        });

        Set<List<Object>> resultSet = cartesianProduct(axes);

        Iterable<Method> excludeFilterMethods = getAnnotatedMethods(clazz, Excluding.class);

        List<Object[]> results = newArrayList();
        for (List<Object> paramsForSingleTest : resultSet) {
            final Object[] objects = paramsForSingleTest.toArray();

            if (Iterables.any(excludeFilterMethods, new Predicate<Method>() {
                @Override
                public boolean apply(Method m) {
                    try {
                        return (boolean) m.invoke(null, objects);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
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

    public static Iterable<Method> getAnnotatedMethods(Class<?> clazz, final Class<? extends Annotation> annotation) {
        return Iterables.filter(newArrayList(clazz.getMethods()), new Predicate<Method>() {
            @Override
            public boolean apply(Method m) {
                return staticMethodWithAnnotation(m, annotation);
            }
        });
    }

    private static boolean staticMethodWithAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return ((method.getModifiers() & Modifier.STATIC) == Modifier.STATIC) && method.getAnnotation(annotationClass) != null;
    }
}