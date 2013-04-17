package org.oaksoft.security.web.context;

import org.springframework.security.core.context.SecurityContext;
import play.mvc.Http;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public interface PlaySecurityContextRepository
{
    public SecurityContext loadContext(Http.Context context);

    public void saveContext(SecurityContext securityContext, Http.Context httpContext);

    public boolean containsContext(Http.Context context);
}
