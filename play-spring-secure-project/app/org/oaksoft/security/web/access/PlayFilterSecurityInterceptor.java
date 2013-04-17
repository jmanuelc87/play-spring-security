package org.oaksoft.security.web.access;

import org.oaksoft.web.action.PlayFilterChainActionType;
import org.oaksoft.web.action.ProxyChainAction;
import org.oaksoft.web.action.WrappedResult;
import org.oaksoft.web.action.impl.DefaultWrappedResult;
import org.oaksoft.web.filter.ChainFilter;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlayFilterSecurityInterceptor extends AbstractPlaySecurityInterceptor implements ChainFilter
{

    private SecurityMetadataSource securityMetadataSource;

    @Override
    public void initialize()
    {
    }

    @Override
    public void destroy(Http.Context context)
    {
    }

    public WrappedResult doFilter(ProxyChainAction proxyChainAction)
    {
        Action<?> delegate = proxyChainAction.getAction();
        Http.Context context = proxyChainAction.getHttpContext();

        if (!isSecured(proxyChainAction)) {

            WrappedResult result = null;
            try {
                result = new DefaultWrappedResult(PlayFilterChainActionType.RETURN_RESULT, delegate.call(context));
            } catch (Throwable throwable) {
            }
            return result;
        }

        InterceptorStatusToken token = super.beforeInvocation(proxyChainAction);

        if (token == null) {
            return super.credentialsNotFound(context, "no authorized");
        }

        WrappedResult result = null;
        try {
            result = new DefaultWrappedResult(PlayFilterChainActionType.RETURN_RESULT, delegate.call(context));
        } catch (Throwable throwable) {
        }

        return result;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource()
    {
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(SecurityMetadataSource securityMetadataSource)
    {
        this.securityMetadataSource = securityMetadataSource;
    }
}
