package org.oaksoft.security.web;

import org.oaksoft.web.action.FilterBean;
import org.oaksoft.web.action.PlayFilterChainActionType;
import org.oaksoft.web.action.WrappedResult;
import org.oaksoft.web.action.impl.DefaultFilterBean;
import org.oaksoft.web.action.impl.DefaultProxyChainAction;
import org.oaksoft.web.filter.ChainFilter;
import org.oaksoft.web.filter.GenericPlayFilterBean;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class PlayFilterChain extends GenericPlayFilterBean
{

    private Map<String, List<ChainFilter>> filtersMap = new HashMap<String, List<ChainFilter>>();

    private String[] filtersOrder = {"SECURITY_CONTEXT_FILTER", "LOGOUT_FILTER", "FORM_LOGIN_FILTER", "FILTER_SECURITY_INTERCEPTOR"};

    @Override
    public void onStart()
    {
    }

    @Override
    public void onStop()
    {

    }

    @Override
    public Action<?> doFilter(final FilterBean filterBean)
    {
        return new Action.Simple()
        {
            @Override
            public Result call(Http.Context context) throws Throwable
            {

                WrappedResult result = null;

                for (String order : filtersOrder) {

                    List<ChainFilter> filters = filtersMap.get(order);

                    for (ChainFilter filter : filters) {
                        result = filter.doFilter(new DefaultProxyChainAction(context, delegate, filterBean.getMethod()));

                        if (result != null) {
                            if (result.getActionType() == PlayFilterChainActionType.CONTINNUE) {
                                continue;
                            }

                            if (result.getActionType() == PlayFilterChainActionType.RETURN_RESULT) {
                                try {
                                    return result.getResult();
                                } finally {
                                    for (String order1 : filtersOrder) {
                                        List<ChainFilter> filters1 = filtersMap.get(order1);

                                        for (ChainFilter filter1 : filters1) {
                                            filter1.destroy(context);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                try {
                    return result.getResult();
                } finally {
                    for (String order : filtersOrder) {
                        List<ChainFilter> filters = filtersMap.get(order);

                        for (ChainFilter filter : filters) {
                            filter.destroy(context);
                        }
                    }
                }
            }
        };
    }

    /**
     * Sets the filters to be applied by the filter chain
     *
     * @param filtersMap a map with the filters to be applied by the filter chain
     */
    public void setFiltersMap(Map<String, List<ChainFilter>> filtersMap)
    {
        this.filtersMap = filtersMap;
    }

    @Override
    public void destroy() throws Exception
    {
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
    }
}
