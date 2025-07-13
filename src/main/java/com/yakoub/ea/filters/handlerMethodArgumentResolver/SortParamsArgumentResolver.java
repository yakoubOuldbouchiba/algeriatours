package com.yakoub.ea.filters.handlerMethodArgumentResolver;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yakoub.ea.filters.specification.RequestService;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

public class SortParamsArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        SortParam sortParamParamsAnnotation = methodParameter.getParameterAnnotation(SortParam.class);
        return sortParamParamsAnnotation != null;
    }
    
    /**
     *
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return this method can return [PageRequest,Unpaged,SortedUnpaged]
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        int skip = 0;
        int take = 10;

        Sort sort = null;
        if (isValid(request.getParameter("sort"))) {
            sort = Sort.by(RequestService.getOrders(request.getParameter("sort")));
        }

        if (isValid(request.getParameter("customQueryParams"))) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> params = mapper.readValue(request.getParameter("customQueryParams"), Map.class);
            if ("ALL".equalsIgnoreCase(params.getOrDefault("lazyData", ""))) {
                return sort != null
                        ? PageRequest.of(0, Integer.MAX_VALUE, sort)
                        : Pageable.unpaged();
            }
        }

        try {
            if (isValid(request.getParameter("skip"))) {
                skip = Integer.parseInt(request.getParameter("skip"));
            }
            if (isValid(request.getParameter("take"))) {
                take = Integer.parseInt(request.getParameter("take"));
            }
        } catch (NumberFormatException ignored) {
            // use defaults
        }

        int page = skip / take;
        return sort != null
                ? PageRequest.of(page, take, sort)
                : PageRequest.of(page, take);
    }

    private boolean isValid(String param) {
        return hasText(param) && !"undefined".equalsIgnoreCase(param.trim());
    }
}
