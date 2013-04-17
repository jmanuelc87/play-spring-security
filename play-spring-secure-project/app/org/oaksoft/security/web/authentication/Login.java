package org.oaksoft.security.web.authentication;

import play.mvc.Call;
import play.mvc.Result;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Login
{
    public String successRedirect() default "";

    public String failureRedirect() default "";
}
