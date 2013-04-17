package org.oaksoft.security.web.access.expression;

import org.oaksoft.web.action.ProxyChainAction;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlayDefaultWebSecurityExpressionHandler extends AbstractSecurityExpressionHandler<ProxyChainAction>
{

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, ProxyChainAction proxyChainAction)
    {
        PlayWebSecurityExpressionRoot root = new PlayWebSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}
