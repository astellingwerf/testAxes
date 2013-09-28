package com.opentext.textaxes;

import static com.opentext.textaxes.AxesRunner.generateCartesianProduct;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(AxesRunner.class)
public class SimpleAxesTest
{

	// Parameters
	@Parameters(name = "Multiply {0} by {1} by {2}")
	public static Iterable<Object[]> data() {
		return generateCartesianProduct(SimpleAxesTest.class);
	}

	@Parameter(0)
	public int	factorA; 

	@Parameter(1)
	public int	factorB;

	@Parameter(2)
	public int	factorC;

	@Axis(0)
	public static int[] a() {
		return new int[] { 1, 2, 4, 8 };
	}

	@Axis(1)
	public static int[] b() {
		return new int[] { 16, 32 };
	}

	@Axis(2)
	public static int[] c() {
		return new int[] { 64, 128, 256, 512, 1024 };
	}

	// Filtering
	private static int	excludingCounter	= 0;
	private static int	onlyIfCounter			= 0;

	@Excluding
	public static boolean moreThan2Sixes(@SuppressWarnings("unused") int a, int b, int c) {
		excludingCounter++;
		return Integer.toString(b).contains("6") && Integer.toString(c).contains("6");
	}

	@OnlyIf
	public static boolean alwaysTrue(@SuppressWarnings("unused") int a, @SuppressWarnings("unused") int b, @SuppressWarnings("unused") int c) {
		onlyIfCounter++;
		return true;
	}

	@AfterClass
	public static void assertFilterInvocationCount() {
		Assert.assertEquals(40, excludingCounter);
		Assert.assertEquals(32, onlyIfCounter);
	}

	// Testing
	private static int	testCaseCounter	= 0;

	@Test
	public void test() throws Exception {
		testCaseCounter++;
		// Do whatever the test should do
	}

	@AfterClass
	public static void assertTestCaseCount() {
		Assert.assertEquals(32, testCaseCounter);
	}

}