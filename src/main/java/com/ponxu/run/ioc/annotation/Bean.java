/**
 * 
 */
package com.ponxu.run.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ponxu.run.ioc.BeanFactory;

/**
 * 把类注释为一个bean
 * 
 * @author xwz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
	public String id() default "";

	public String scope() default BeanFactory.SCOPE_DEFAULT;
}
