package org.oaksoft.security.web.authentication;

import org.oaksoft.web.action.PlayFilterChainActionType;
import org.oaksoft.web.action.ProxyChainAction;
import org.oaksoft.web.action.WrappedResult;
import org.oaksoft.web.action.impl.DefaultWrappedResult;
import org.oaksoft.web.filter.ChainFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public abstract class AbstractPlayAuthenticationProcessingFilter implements ChainFilter
{

    private AuthenticationManager authenticationManager;

    @Override
    public void initialize()
    {
    }

    @Override
    public void destroy(Http.Context context)
    {
    }

    @Override
    public WrappedResult doFilter(ProxyChainAction proxyChainAction)
    {
        Http.Context context = proxyChainAction.getHttpContext();
        Action<?> delegate = proxyChainAction.getAction();

        if (!requiresAuthentication(proxyChainAction)) {
            WrappedResult result = null;
            try {
                result = new DefaultWrappedResult(PlayFilterChainActionType.CONTINNUE, delegate.call(context));
            } catch (Throwable throwable) {
            }
            return result;
        }

        Authentication authResult;


        try {
            authResult = attemptAuthentication(context);

            if (authResult == null) {
                // TODO implement if authResult is null
            }

        } catch (InternalAuthenticationServiceException e) {

            return unsuccessfulAuthentication(context, (String) proxyChainAction.get("failure"));
        } catch (AuthenticationException e) {

            return unsuccessfulAuthentication(context, (String) proxyChainAction.get("failure"));
        } catch (IOException e) {

            return unsuccessfulAuthentication(context, (String) proxyChainAction.get("failure"));
        }

        return successfulAuthentication(context, authResult, (String) proxyChainAction.get("success"));
    }

    protected boolean requiresAuthentication(ProxyChainAction proxyChainAction)
    {
        Method method = proxyChainAction.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Login) {
                Login login = (Login) annotation;
                proxyChainAction.add("success", login.successRedirect());
                proxyChainAction.add("failure", login.failureRedirect());
                return true;
            }
        }

        return false;
    }

    /**
     * @param context
     * @return
     * @throws AuthenticationException
     * @throws IOException
     */
    public abstract Authentication attemptAuthentication(Http.Context context) throws AuthenticationException, IOException;


    /**
     * @param context
     * @param failureUrl
     * @return
     */
    protected WrappedResult unsuccessfulAuthentication(Http.Context context, final String failureUrl)
    {
        SecurityContextHolder.clearContext();

        Logger.debug("unsuccessful authentication!");

        Action.Simple delegate = new Action.Simple()
        {
            @Override
            public Result call(Http.Context context) throws Throwable
            {
                return redirect(failureUrl);
            }
        };

        WrappedResult result = null;
        try {
            result = new DefaultWrappedResult(PlayFilterChainActionType.RETURN_RESULT, delegate.call(context));
        } catch (Throwable throwable) {
        }

        return result;
    }

    /**
     * @param context
     * @param authentication
     * @return
     */
    protected WrappedResult successfulAuthentication(Http.Context context, Authentication authentication, final String successUrl)
    {
        Logger.debug("Authentication success. Updating SecurityContextHolder...");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Action.Simple delegate = new Action.Simple()
        {
            @Override
            public Result call(Http.Context context) throws Throwable
            {
                return redirect(successUrl);
            }
        };

        WrappedResult result = null;
        try {
            result = new DefaultWrappedResult(PlayFilterChainActionType.RETURN_RESULT, delegate.call(context));
        } catch (Throwable throwable) {
        }

        return result;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationManager getAuthenticationManager()
    {
        return authenticationManager;
    }
}
