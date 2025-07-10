package com.yakoub.ea.filters.handlerMethodArgumentResolver;



import com.yakoub.ea.execptions.BusinessException;
import com.yakoub.ea.filters.clause.Clause;
import com.yakoub.ea.filters.clause.ClauseArrayArgs;
import com.yakoub.ea.filters.clause.ClauseOneArg;
import com.yakoub.ea.filters.clause.ClauseTwoArgs;
import com.yakoub.ea.filters.enums.Operation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

public class CritiriaParamsArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Critiria sortParamParamsAnnotation = methodParameter.getParameterAnnotation(Critiria.class);
        return sortParamParamsAnnotation != null;
    }
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        List<Clause> clauses = new ArrayList<>();
        HttpServletRequest httpRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        String[] filters = httpRequest.getParameterValues("filter");
        if (filters != null) {
            for (String filter : filters) {
                clauses.addAll(parseFilter(filter));
            }
        }

        String searchValue = httpRequest.getParameter("searchValue");
        String searchExpr = httpRequest.getParameter("searchExpr");
        if (hasText(searchValue) && !"undefined".equals(searchValue)
                && hasText(searchExpr) && !"undefined".equals(searchExpr)) {
            String searchOperation = hasText(httpRequest.getParameter("searchOperation")) &&
                    !"undefined".equals(httpRequest.getParameter("searchOperation"))
                    ? httpRequest.getParameter("searchOperation")
                    : "=";

            clauses.add(new ClauseOneArg(
                    searchExpr,
                    Operation.valueOfLabel(searchOperation.replace("\"", "")),
                    searchValue
            ));
        }

        return clauses;
    }

    private List<Clause> parseFilter(String filter) throws BusinessException {
        List<Clause> list = new ArrayList<>();
        if (!hasText(filter)) return list;

        String[] parts = filter.split(":", 3);
        if (parts.length != 3)
            throw new BusinessException("Each filter must be in the form attribute:operation:value — got: " + filter);

        String attribute = parts[0];
        String op = parts[1];
        String value = parts[2];

        Operation operation = Operation.valueOfLabel(op);

        switch (operation) {
            case Between:
                String[] range = value.split(",");
                if (range.length != 2)
                    throw new BusinessException("BETWEEN requires two comma-separated values: " + value);
                list.add(new ClauseTwoArgs(attribute, operation, range[0].trim(), range[1].trim()));
                break;
            case In:
            case NotIn:
                String[] values = value.split(",");
                if (values.length == 0)
                    throw new BusinessException("IN requires at least one value: " + value);
                list.add(new ClauseArrayArgs(attribute, operation, values));
                break;

            default:
                list.add(new ClauseOneArg(attribute, operation, value));
        }

        return list;
    }


}
