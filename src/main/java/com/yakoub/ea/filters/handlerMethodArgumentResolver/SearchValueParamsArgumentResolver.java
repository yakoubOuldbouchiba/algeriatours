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
        String searchOperation = null;
        String searchValue = null;
        String searchExpr = null;
        HttpServletRequest httpRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        if (hasText(httpRequest.getParameter("searchValue")) && !httpRequest.getParameter("searchExpr").equals("undefined")) {
            searchValue = httpRequest.getParameter("searchValue");
            searchOperation = httpRequest.getParameter("searchOperation");
            searchExpr = httpRequest.getParameter("searchExpr");
            return new ClauseOneArg(searchExpr, Operation.valueOfLabel(searchOperation), searchValue);
        }
        return null;
    }
}