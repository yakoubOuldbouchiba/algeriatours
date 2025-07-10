package com.yakoub.ea.filters.handlerMethodArgumentResolver;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriUtils;

import java.util.Map;

public class PathVariableEnCodeResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(PathVariableEnCode.class);

    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        String variableName = getVariableName(methodParameter);

        String encodedValue = getEncodedValue(nativeWebRequest, variableName);

        return UriUtils.decode(encodedValue, "UTF-8");

    }

    private String getVariableName(MethodParameter parameter) {
        PathVariable pathVariable = parameter.getParameterAnnotation(PathVariable.class);
        if (pathVariable != null && StringUtils.hasText(pathVariable.value())) {
            return pathVariable.value();
        }
        return parameter.getParameterName();
    }

    private String getEncodedValue(NativeWebRequest webRequest, String variableName) {
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        if (uriTemplateVars != null) {
            return uriTemplateVars.get(variableName);
        }
        return null;
    }


}
