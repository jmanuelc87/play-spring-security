package org.oaksoft.web.action;

import play.mvc.Result;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public interface WrappedResult
{
    public PlayFilterChainActionType getActionType();

    public Result getResult() throws RuntimeException;
}
