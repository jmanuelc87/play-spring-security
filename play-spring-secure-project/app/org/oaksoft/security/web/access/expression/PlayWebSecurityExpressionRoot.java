package org.oaksoft.security.web.access.expression;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlayWebSecurityExpressionRoot extends SecurityExpressionRoot
{
    public PlayWebSecurityExpressionRoot(Authentication a)
    {
        super(a);
    }
}
