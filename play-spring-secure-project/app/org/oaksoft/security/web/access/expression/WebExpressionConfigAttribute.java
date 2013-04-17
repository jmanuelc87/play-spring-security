package org.oaksoft.security.web.access.expression;

import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
class WebExpressionConfigAttribute implements ConfigAttribute
{
    private final Expression authorizeExpression;

    public WebExpressionConfigAttribute(Expression authorizeExpression)
    {
        this.authorizeExpression = authorizeExpression;
    }

    Expression getAuthorizeExpression()
    {
        return authorizeExpression;
    }

    public String getAttribute()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return authorizeExpression.getExpressionString();
    }
}