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
import java.util.Optional;

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
        int skip = 0;
        int take = 10;
        String sort;
        Optional<Sort> sortOptional = Optional.ofNullable(null);
        HttpServletRequest httpRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        
        if (hasText(httpRequest.getParameter("sort")) && !httpRequest.getParameter("sort").equals("undefined")) {
            sort = httpRequest.getParameter("sort");
            sortOptional = Optional.ofNullable(Sort.by(RequestService.getOrders(sort)));
        }
        
        if (hasText(httpRequest.getParameter("customQueryParams")) && !httpRequest.getParameter("customQueryParams").equals("undefined")) {
            ObjectMapper mapper = new ObjectMapper();
            Optional<Map<String, String>> params = Optional.ofNullable(mapper.readValue(httpRequest.getParameter("customQueryParams"), Map.class));
            Optional<String> lazyData = params.map(mapParams -> mapParams.get("lazyData"));
            if (lazyData.isPresent() && lazyData.orElse("LAZY").equals("ALL")) {
                if (sortOptional.isPresent()) {
                    return PageRequest.of(0, Integer.MAX_VALUE,sortOptional.get());
                    //return SortedUnpaged.getInstance(sortOptional.get());
                }
                return Pageable.unpaged();
            }
        }
        if (hasText(httpRequest.getParameter("skip")) && !httpRequest.getParameter("skip").equals("undefined")) {
            skip = Integer.parseInt(httpRequest.getParameter("skip"));
        }
        if (hasText(httpRequest.getParameter("take")) && !httpRequest.getParameter("take").equals("undefined")) {
            take = Integer.parseInt(httpRequest.getParameter("take"));
        }else {
            if (sortOptional.isPresent()) {
                //PageRequest.of(0, Integer.MAX_VALUE,sortOptional.get());
                return PageRequest.of(0, Integer.MAX_VALUE,sortOptional.get());
            }
            return Pageable.unpaged();
        }
        if (sortOptional.isPresent()) {
            return PageRequest.of(skip / take, take, sortOptional.get());
        }
        return PageRequest.of(skip / take, take);
    }
}
