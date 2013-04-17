package org.oaksoft.web.action.impl;

import org.oaksoft.web.action.PlayFilterChainActionType;
import org.oaksoft.web.action.WrappedResult;
import play.mvc.Result;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class DefaultWrappedResult implements WrappedResult
{
    private PlayFilterChainActionType actionType;

    private Result result;

    public DefaultWrappedResult(PlayFilterChainActionType actionType, Result result)
    {
        this.actionType = actionType;
        this.result = result;
    }

    @Override
    public PlayFilterChainActionType getActionType()
    {
        return actionType;
    }

    @Override
    public Result getResult() throws RuntimeException
    {
        if (actionType == PlayFilterChainActionType.RETURN_RESULT && result == null) {
            throw new RuntimeException();
        }

        return result;
    }
}
