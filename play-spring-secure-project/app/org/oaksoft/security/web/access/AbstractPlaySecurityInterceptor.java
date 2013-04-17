package org.oaksoft.security.web.access;

import org.hibernate.action.CollectionAction;
import org.oaksoft.security.web.authentication.Login;
import org.oaksoft.web.action.PlayFilterChainActionType;
import org.oaksoft.web.action.ProxyChainAction;
import org.oaksoft.web.action.WrappedResult;
import org.oaksoft.web.action.impl.DefaultWrappedResult;
import org.oaksoft.web.filter.ChainFilter;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public abstract class AbstractPlaySecurityInterceptor
{

    private AuthenticationManager authenticationManager;

    private AccessDecisionManager accessDecisionManager;


    protected InterceptorStatusToken beforeInvocation(ProxyChainAction proxyChainAction)
    {
        Assert.notNull(proxyChainAction);

        Action<?> delegate = proxyChainAction.getAction();
        Http.Context context = proxyChainAction.getHttpContext();

        Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(proxyChainAction);

        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException("Error at secure object invocation!");
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }

        Authentication authenticated = authenticateIfRequired();

        try {
            this.accessDecisionManager.decide(authenticated, proxyChainAction, attributes);
        } catch (AccessDeniedException accessDeniedException) {
            return null;
        }

        return new InterceptorStatusToken(SecurityContextHolder.getContext(), false, attributes, proxyChainAction);
    }

    protected boolean isSecured(ProxyChainAction proxyChainAction)
    {
        Method method = proxyChainAction.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Secured) {
                return true;
            }
        }

        return false;
    }

    protected Authentication authenticateIfRequired()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            return authentication;
        }

        authentication = authenticationManager.authenticate(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    protected WrappedResult credentialsNotFound(Http.Context context, final String reason)
    {
        Action.Simple action = new Action.Simple()
        {
            @Override
            public Result call(Http.Context context) throws Throwable
            {
                return unauthorized();
            }
        };

        WrappedResult result = null;
        try {
            result = new DefaultWrappedResult(PlayFilterChainActionType.RETURN_RESULT, action.call(context));
        } catch (Throwable throwable) {
        }

        return result;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager)
    {
        this.accessDecisionManager = accessDecisionManager;
    }

    public abstract SecurityMetadataSource obtainSecurityMetadataSource();
}
