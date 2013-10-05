package com.opentext.textaxes;

import static com.opentext.textaxes.AxesRunner.generateCartesianProduct;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(AxesRunner.class)
public class AnnotatedParametersTest
{
	@Rule
	public ExpectedException	expectedException	= ExpectedException.none();
	@Parameter(0)
	public int								factorA;
	@Parameter(1)
	public String							factorB;

	@Axis(0)
	public static Iterable<Integer> a() {
		return Arrays.asList(1, 2, 4, 8);
	}

	@Axis(1)
	public static Iterable<String> b() {
		return Arrays.asList("a", "bc");
	}

	@Excluding
	public static boolean businessRule(@Axis(1) String b) {
		return b.contains("b");
	}

	@Excluding
	public static boolean businessRuleA(@Axis(0) int a1, @Axis(0) int a2) {
		Assert.assertEquals(a1, a2);
		return false;
	}

	@Excluding
    @edu.umd.cs.findbugs.annotations.SuppressWarnings("ES_COMPARING_PARAMETER_STRING_WITH_EQ")
	public static boolean businessRuleB(@Axis(1) String b1, @Axis(1) String b2) {
		Assert.assertEquals(b1, b2);
		Assert.assertSame(b1, b2);
		return false;
	}

	@Parameters
	public static Iterable<Object[]> data() {
		return generateCartesianProduct(AnnotatedParametersTest.class);
	}

	@Test
	public void doNothing() {
		// tests are in @Excluding methods
	}

}