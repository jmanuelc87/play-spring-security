package org.oaksoft.web.action.impl;


import org.oaksoft.web.action.FilterBean;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

public class DefaultFilterBean implements FilterBean
{
    private Http.Request request;

    private Method method;

    private Action<?> action;

    public DefaultFilterBean(Http.Request request, Method method, Action<?> action)
    {
        this.request = request;
        this.method = method;
        this.action = action;
    }

    @Override
    public Http.Request getRequest()
    {
        return this.request;
    }

    @Override
    public Method getMethod()
    {
        return this.method;
    }

    @Override
    public Action<?> getAction()
    {
        return this.action;
    }
}
