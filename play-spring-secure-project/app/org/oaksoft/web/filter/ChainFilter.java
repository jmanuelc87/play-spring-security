package org.oaksoft.web.filter;

import org.oaksoft.web.action.ProxyChainAction;
import org.oaksoft.web.action.WrappedResult;
import play.mvc.Http;
import play.mvc.Result;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public interface ChainFilter
{
    public void initialize();

    public void destroy(Http.Context context);

    public WrappedResult doFilter(ProxyChainAction proxyChainAction);
}
