package com.opentext.textaxes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;

import static com.opentext.textaxes.AxesRunner.generateCartesianProduct;

public class NonIterableAxisTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Parameter(0)
    public int factorA;
    @Parameter(1)
    public int factorB;

    @Axis(0)
    public static Iterable<Integer> a() {
        return Arrays.asList(1, 2, 4, 8);
    }

    @Axis(1)
    public static int[] b() {
        return new int[]{16, 32};
    }

    @Test
    public void testWithArrayAxis() {
        expectedException.expect(IllegalArgumentException.class);
        generateCartesianProduct(NonIterableAxisTest.class);
    }

}