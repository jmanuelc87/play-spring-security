package org.oaksoft.security.web.util;

import org.oaksoft.web.action.ProxyChainAction;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public interface RequestMatcher
{
    public boolean matches(ProxyChainAction proxyChainAction);
}
