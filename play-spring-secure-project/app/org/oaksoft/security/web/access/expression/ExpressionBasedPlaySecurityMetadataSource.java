package org.oaksoft.security.web.access.expression;

import org.oaksoft.security.web.access.PlaySecurityMetadataSource;
import org.oaksoft.security.web.util.RequestMatcher;
import org.oaksoft.web.action.ProxyChainAction;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.expression.SecurityExpressionHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class ExpressionBasedPlaySecurityMetadataSource extends PlaySecurityMetadataSource
{
    SecurityExpressionHandler<ProxyChainAction> expressionHandler;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException
    {
        return processMap(o, expressionHandler.getExpressionParser());
    }


    private Collection<ConfigAttribute> processMap(Object obj, ExpressionParser parser)
    {
        Method method = ((ProxyChainAction) obj).getMethod();

        ArrayList<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

        attributes.add(new WebExpressionConfigAttribute(parser.parseExpression(getExpression(method))));

        return attributes;
    }

    @Override
    public boolean supports(Class<?> clazz)
    {
        return ProxyChainAction.class.isAssignableFrom(clazz);
    }

    public void setExpressionHandler(SecurityExpressionHandler<ProxyChainAction> expressionHandler)
    {
        this.expressionHandler = expressionHandler;
    }
}
