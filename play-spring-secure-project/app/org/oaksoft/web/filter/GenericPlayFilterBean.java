package org.oaksoft.web.filter;

import org.oaksoft.web.action.FilterBean;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import play.Application;
import play.mvc.Action;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public abstract class GenericPlayFilterBean implements InitializingBean, DisposableBean
{

    public abstract void onStart();

    public abstract void onStop();

    public abstract Action<?> doFilter(FilterBean filterBean);
}
