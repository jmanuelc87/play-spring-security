package org.oaksoft.security.web.context;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import play.Logger;
import play.cache.Cache;
import play.mvc.Http;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlayHttpSessionSecurityContextRepository implements PlaySecurityContextRepository
{

    public static final String PLAY_SECURITY_CONTEXT_KEY = "PLAY_SECURITY_CONTEXT";

    private final Object contextObject = SecurityContextHolder.createEmptyContext();

    private String playSecurityContextKey = PLAY_SECURITY_CONTEXT_KEY;

    @Override
    public SecurityContext loadContext(Http.Context context)
    {
        SecurityContext securityContext = readSecurityContextFromSession(context);

        if (securityContext == null) {
            Logger.debug("no context was available from play session");
            securityContext = generateNewContext();
        }


        return securityContext;
    }

    private SecurityContext readSecurityContextFromSession(Http.Context context)
    {
        if (context == null) {
            Logger.debug("No Http Context currently exist");

            return null;
        }

        Object contextFromSession = null;
        if (context.request().cookie(playSecurityContextKey) != null) {
            String value = context.request().cookie(playSecurityContextKey).value();
            contextFromSession = Cache.get(value);
        }

        if (contextFromSession == null) {
            Logger.debug("Play Session returned null object for PLAY_SECURITY_CONTEXT");

            return null;
        }

        if (!(contextFromSession instanceof SecurityContext)) {
            Logger.debug("PLAY_SECURITY_CONTEXT did not contain a SecurityContext");
            return null;
        }

        Logger.debug("obtained a valid SecurityContext");

        return (SecurityContext) contextFromSession;
    }

    protected SecurityContext generateNewContext()
    {
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public void saveContext(SecurityContext securityContext, Http.Context httpContext)
    {
        if (httpContext.request().cookie(playSecurityContextKey) == null) {
            String random = RandomStringUtils.randomAlphanumeric(45);
            httpContext.response().setCookie(playSecurityContextKey, random);
            Cache.set(random, securityContext, 3600);
        } else {
            String value = httpContext.request().cookie(playSecurityContextKey).value();
            Cache.remove(value);
            String random = RandomStringUtils.randomAlphanumeric(45);
            httpContext.response().setCookie(playSecurityContextKey, random);
            Cache.set(random, securityContext, 3600);
        }
    }

    @Override
    public boolean containsContext(Http.Context context)
    {
        String value = context.request().cookie(playSecurityContextKey).value();
        return Cache.get(value) != null;
    }
}
