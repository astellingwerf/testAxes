package com.opentext.textaxes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Inherited
public @interface Axis
{
	/**
	 * Method that returns the index of the parameter in the array returned by the method annotated by {@link org.junit.runners.Parameterized.Parameters}.<br/>
     * <b>OR</b><br/>
     * Parameter of {@link Excluding} that must contain the value of the corresponding parameter in the array returned by the method annotated by {@link org.junit.runners.Parameterized.Parameters}.<br/>
	 * Index range must start at 0.
	 * Default value is 0.
	 * 
	 * @return the index of the parameter.
     * @see Excluding
     * @see org.junit.runners.Parameterized.Parameters
	 */
	int value() default 0;
}
