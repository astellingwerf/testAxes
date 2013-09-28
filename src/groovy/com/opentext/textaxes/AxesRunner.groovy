package com.opentext.textaxes;

import java.lang.reflect.Method
import java.lang.reflect.Modifier

import org.junit.runners.Parameterized

public class AxesRunner extends Parameterized {
	public AxesRunner(Class<?> testClass) throws Throwable {
		super(testClass);
	}

	public static Iterable<Object[]> generateCartesianProduct(Class<?> clazz){
		Collection<Method> axisMethods = clazz.methods.findAll { Method method ->
			staticMethodWithAnnotation(method, Axis.class)
		}.sort(false) { Method method -> method.getAnnotation(Axis.class).value() }

		Collection<Method> excludeFilterMethods = clazz.methods.findAll { Method method ->
			staticMethodWithAnnotation(method, Excluding.class)
		}
		Collection<Method> onlyIfFilterMethods = clazz.methods.findAll { Method method ->
			staticMethodWithAnnotation(method, OnlyIf.class)
		}

		return cartesianProduct(axisMethods).collect { it.toArray() }.findAll { Object[] parametersOfSingleTest ->
			!excludeFilterMethods.any { Method method ->
				method.invoke(null, parametersOfSingleTest)
			} && onlyIfFilterMethods.every { Method method ->
				method.invoke(null, parametersOfSingleTest)
			}
		}
	}

	private static cartesianProduct(Collection<Method> axisMethods) {
		def a = axisMethods[0].invoke(null)
		for(int i = 1; i < axisMethods.size(); i++)
			a = cartesianProduct3(a, axisMethods[i].invoke(null), i ==1)
		return a;
	}

	private static cartesianProduct3(A, B, boolean first) {
		def result = []
		A.each{a->
			result += B.collect{ b->
				if(first)
					[a, b]
				else
					a + [b]
			}
		}
		return result
	}

	private static boolean staticMethodWithAnnotation(Method method, Class<?> annotationClass) {
		return ((method.modifiers & Modifier.STATIC) == Modifier.STATIC) &&	method.getAnnotation(annotationClass) != null;
	}
}
