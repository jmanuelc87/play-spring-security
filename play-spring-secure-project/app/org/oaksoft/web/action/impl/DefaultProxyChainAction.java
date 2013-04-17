package org.oaksoft.web.action.impl;

import org.oaksoft.web.action.ProxyChainAction;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class DefaultProxyChainAction implements ProxyChainAction
{

    private Map<String, Object> properties = new HashMap<String, Object>();

    private Http.Context context;

    private Action<?> delegate;

    private Method method;

    public DefaultProxyChainAction(Http.Context context, Action<?> delegate, Method method)
    {
        this.context = context;
        this.delegate = delegate;
        this.method = method;
    }

    @Override
    public Http.Context getHttpContext()
    {
        return context;
    }

    @Override
    public Action<?> getAction()
    {
        return delegate;
    }

    @Override
    public Method getMethod()
    {
        return this.method;
    }

    @Override
    public void add(String key, Object obj)
    {
        properties.put(key, obj);
    }

    @Override
    public Object get(String key)
    {
        return properties.get(key);
    }
}
