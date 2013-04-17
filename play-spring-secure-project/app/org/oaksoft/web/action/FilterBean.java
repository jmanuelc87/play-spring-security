package org.oaksoft.web.action;

import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public interface FilterBean
{
    public Http.Request getRequest();

    public Method getMethod();

    public Action<?> getAction();
}
