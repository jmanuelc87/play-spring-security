package org.oaksoft.security.web.authentication.logout;

import org.oaksoft.web.action.PlayFilterChainActionType;
import org.oaksoft.web.action.ProxyChainAction;
import org.oaksoft.web.action.WrappedResult;
import org.oaksoft.web.action.impl.DefaultWrappedResult;
import org.oaksoft.web.filter.ChainFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import play.Logger;
import play.cache.Cache;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlayLogoutFilter implements ChainFilter
{
    public static final String PLAY_SECURITY_CONTEXT_KEY = "PLAY_SECURITY_CONTEXT";

    private String playSecurityContextKey = PLAY_SECURITY_CONTEXT_KEY;

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

        Logger.debug("PlayLogoutFilter Applied!");

        Action<?> delegate = proxyChainAction.getAction();
        Http.Context context = proxyChainAction.getHttpContext();

        if (requiresLogout(proxyChainAction)) {

            deleteSession(context);

            Logger.debug("Play Session Deleted!");

            return performLogout(proxyChainAction);
        }

        WrappedResult result = null;
        try {
            result = new DefaultWrappedResult(PlayFilterChainActionType.CONTINNUE, delegate.call(context));
        } catch (Throwable throwable) {
        }

        return result;
    }

    protected boolean requiresLogout(ProxyChainAction proxyChainAction)
    {
        Method method = proxyChainAction.getMethod();

        Annotation[] annotations = method.getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Logout) {
                String successRedirect = ((Logout) annotation).successRedirect();
                proxyChainAction.add("successRedirect", successRedirect);
                return true;
            }
        }

        return false;
    }

    protected WrappedResult performLogout(ProxyChainAction proxyChainAction)
    {
        final String url = (String) proxyChainAction.get("successRedirect");
        Http.Context context = proxyChainAction.getHttpContext();

        Action.Simple action = new Action.Simple()
        {
            @Override
            public Result call(Http.Context context) throws Throwable
            {
                return redirect(url);
            }
        };

        WrappedResult w = null;
        try {
            w = new DefaultWrappedResult(PlayFilterChainActionType.RETURN_RESULT, action.call(context));
        } catch (Throwable throwable) {
        }

        return w;
    }

    protected void deleteSession(Http.Context context)
    {
        if (context.request().cookie(playSecurityContextKey) != null) {
            String value = context.request().cookie(playSecurityContextKey).value();
            Cache.remove(value);
            context.response().discardCookie(value);
            SecurityContextHolder.clearContext();
        }
    }
}
