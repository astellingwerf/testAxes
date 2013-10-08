package com.opentext.textaxes;

import static com.opentext.textaxes.AxesRunner.generateCartesianProduct;
import static java.util.Arrays.asList;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(AxesRunner.class)
public class CollectionAxesTest
{
	// Parameters
	@Parameters(name = "Run: {0} & {1} & {2} ")
	public static Iterable<Object[]> data() {
		return generateCartesianProduct(CollectionAxesTest.class);
	}

	@Parameter(0)
	public List<String>	factorA;
	@Parameter(1)
	public String				factorB;
	@Parameter(2)
	public List<String>	factorC;

	@Axis(0)
	public static List<List<String>> a() {
		return asList(asList("ABC", "DEF"), asList("GHI", "JKL"));
	}

	@Axis(1)
	public static List<String> b() {
		return asList("X", "Y");
	}

	@Axis(2)
	public static List<List<String>> c() {
		return asList(asList("ABC", "DEF"), asList("GHI", "JKL"));
	}

	// Filtering
	private static int	excludingCounter	= 0;

	@Excluding
	public static boolean moreThan2Sixes(	@SuppressWarnings("unused") List<String> a,
																				@SuppressWarnings("unused") String b,
																				@SuppressWarnings("unused") List<String> c) {
		excludingCounter++;
		return false;
	}

	@AfterClass
	public static void assertFilterInvocationCount() {
		Assert.assertEquals(8, excludingCounter);
	}

	// Testing
	private static int	testCaseCounter	= 0;

	@Test
	@edu.umd.cs.findbugs.annotations.SuppressWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
	public void test1() throws Exception {
		testCaseCounter++;
		// Do whatever the test should do
	}

	@Test
	@edu.umd.cs.findbugs.annotations.SuppressWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
	public void test2() throws Exception {
		testCaseCounter++;
		// Do whatever the test should do
	}

	@AfterClass
	public static void assertTestCaseCount() {
		Assert.assertEquals(16, testCaseCounter);
	}
}
