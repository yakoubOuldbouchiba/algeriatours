package com.yakoub.ea.config;

import com.yakoub.ea.handlerMethodArgumentResolver.FilterParamsArgumentResolver;
import com.yakoub.ea.handlerMethodArgumentResolver.OrFilterParamsArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@Configuration
public class CommonsWebServiceConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FilterParamsArgumentResolver());
        resolvers.add(new OrFilterParamsArgumentResolver());
    }
}
