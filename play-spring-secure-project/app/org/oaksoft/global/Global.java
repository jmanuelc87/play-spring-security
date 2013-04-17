package org.oaksoft.global;

import org.oaksoft.web.action.impl.DefaultFilterBean;
import org.oaksoft.web.filter.GenericPlayFilterBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class Global extends GlobalSettings
{

    private ApplicationContext context;

    private GenericPlayFilterBean filter;

    @Override
    public void onStart(Application application)
    {
        this.context = new ClassPathXmlApplicationContext("spring/app-context.xml");
        Logger.debug("Spring Started...");

        filter = this.context.getBean("filter", GenericPlayFilterBean.class);
        filter.onStart();

        Logger.debug("Application Started...");
    }

    @Override
    public void onStop(Application application)
    {
        Logger.debug("Application Stoped...");
    }

    @Override
    public Action onRequest(Http.Request request, Method method)
    {

        try {
            return filter.doFilter(new DefaultFilterBean(request, method, null));
        } finally {
            Logger.debug(request.path());
            filter.onStop();
        }
    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) throws Exception
    {
        return context.getBean(clazz);
    }
}
