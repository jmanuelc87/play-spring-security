package org.oaksoft.security.web.context;

import org.oaksoft.web.action.PlayFilterChainActionType;
import org.oaksoft.web.action.ProxyChainAction;
import org.oaksoft.web.action.WrappedResult;
import org.oaksoft.web.action.impl.DefaultWrappedResult;
import org.oaksoft.web.filter.ChainFilter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlaySecurityContextPersistenceFilter implements ChainFilter
{

    private PlaySecurityContextRepository repo;

    private Http.Context context;

    private Action<?> delegate;

    @Override
    public void initialize()
    {

    }

    @Override
    public void destroy(Http.Context context)
    {
        SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        repo.saveContext(contextAfterChainExecution, context);
        Logger.debug("SecurityContextHolder now cleared, as request processing completed");
    }

    @Override
    public WrappedResult doFilter(ProxyChainAction proxyChainAction)
    {
        Logger.debug("SecurityContextPersistenceFilter Applied!");

        context = proxyChainAction.getHttpContext();
        delegate = proxyChainAction.getAction();

        SecurityContext contextBeforeChainExecution = repo.loadContext(context);


        SecurityContextHolder.setContext(contextBeforeChainExecution);
        WrappedResult wrappedResult = null;
        try {
            wrappedResult = new DefaultWrappedResult(PlayFilterChainActionType.CONTINNUE, delegate.call(context));
        } catch (Throwable t) {

        }

        return wrappedResult;
    }

    public void setPlaySecurityContextRepository(PlaySecurityContextRepository repo)
    {
        Assert.notNull(repo, "SecurityContextRepository cannot be null");
        this.repo = repo;
    }
}
