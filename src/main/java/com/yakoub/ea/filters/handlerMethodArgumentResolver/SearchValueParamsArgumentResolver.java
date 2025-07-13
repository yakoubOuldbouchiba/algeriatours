package com.yakoub.ea.filters.handlerMethodArgumentResolver;



import com.yakoub.ea.filters.clause.ClauseOneArg;
import com.yakoub.ea.filters.enums.Operation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



import static org.springframework.util.StringUtils.hasText;

public class SearchValueParamsArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        SearchValue SearchValueParamsAnnotation = methodParameter.getParameterAnnotation(SearchValue.class);
        return SearchValueParamsAnnotation != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        String value = request.getParameter("searchValue");
        String expr = request.getParameter("searchExpr");
        String op = request.getParameter("searchOperation");

        if (isDefined(value) && isDefined(expr)) {
            if (!isDefined(op)) op = "=";

            return new ClauseOneArg(
                    expr.trim(),
                    Operation.valueOfLabel(op.trim()),
                    value.trim()
            );
        }

        return null;

    }

    private boolean isDefined(String param) {
        return hasText(param) && !"undefined".equalsIgnoreCase(param.trim());
    }
}