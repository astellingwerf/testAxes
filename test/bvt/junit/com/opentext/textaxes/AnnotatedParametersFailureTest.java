package com.opentext.textaxes;

import static com.opentext.textaxes.AxesRunner.generateCartesianProduct;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AnnotatedParametersFailureTest
{
	@Rule
	public ExpectedException	expectedException	= ExpectedException.none();

	@Test
	public void doNothing() {
		expectedException.expect(IllegalArgumentException.class);
		generateCartesianProduct(HalfAnnotatedMethod.class);
	}

	private static class HalfAnnotatedMethod
	{
		@Axis(0)
		public static Iterable<Integer> a() {
			return Arrays.asList(0);
		}

		@Excluding
		public static boolean method(@SuppressWarnings("unused") @Axis(0) int paramA, @SuppressWarnings("unused") int paramB) {
			return true;
		}
	}

}