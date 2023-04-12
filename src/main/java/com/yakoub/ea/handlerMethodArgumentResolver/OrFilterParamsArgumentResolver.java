package com.yakoub.ea.handlerMethodArgumentResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yakoub.ea.filter.execptions.ClauseStructureExecption;
import com.yakoub.ea.filter.execptions.OperationNotFoundExecption;
import com.yakoub.ea.filter.execptions.ParamsRequirededExeception;
import com.yakoub.ea.filter.factory.ClauseFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

public class OrFilterParamsArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        OrFilterParams filterParamsAnnotation = parameter.getParameterAnnotation(OrFilterParams.class);
        if (filterParamsAnnotation != null) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        if (hasText(httpRequest.getParameter("orFilter"))) {
            String[] filters = httpRequest.getParameterValues("orFilter");
            return  Arrays.stream(filters).map(filter -> {
                try {
                    return ClauseFactory.getClause(filter);
                } catch (ClauseStructureExecption | ParamsRequirededExeception | OperationNotFoundExecption e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
        return null;
    }
}
