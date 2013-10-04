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
	 * Method that returns the index of the parameter in the array returned by the method annotated by <code>Parameters</code>.<br/>
	 * Index range must start at 0.
	 * Default value is 0.
	 * 
	 * @return the index of the parameter.
	 */
	int value() default 0;
}
