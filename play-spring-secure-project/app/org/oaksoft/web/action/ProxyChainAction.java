package org.oaksoft.web.action;

import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public interface ProxyChainAction
{
    public Http.Context getHttpContext();

    public Action<?> getAction();

    public Method getMethod();

    public void add(String key, Object obj);

    public Object get(String key);
}
