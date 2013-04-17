package org.oaksoft.security.web.access;

import org.oaksoft.web.action.ProxyChainAction;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.SecurityMetadataSource;
import play.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlaySecurityMetadataSource implements SecurityMetadataSource
{
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException
    {
        Method method = ((ProxyChainAction) o).getMethod();
        String expression = getExpression(method);

        List<ConfigAttribute> list = new ArrayList<ConfigAttribute>();
        list.add(new SecurityConfig(expression));

        return list;
    }

    protected String getExpression(Method method)
    {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Secured) {
                Secured secured = (Secured) annotation;
                return secured.expression();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes()
    {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz)
    {
        return ProxyChainAction.class.isAssignableFrom(clazz);
    }
}
