package com.yakoub.ea.config;


import com.yakoub.ea.config.converter.LocalDateConverter;
import com.yakoub.ea.config.converter.LocalDateTimeConverter;
import com.yakoub.ea.filters.handlerMethodArgumentResolver.CritiriaParamsArgumentResolver;
import com.yakoub.ea.filters.handlerMethodArgumentResolver.PathVariableEnCodeResolver;
import com.yakoub.ea.filters.handlerMethodArgumentResolver.SearchValueParamsArgumentResolver;
import com.yakoub.ea.filters.handlerMethodArgumentResolver.SortParamsArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@Configuration
public class CommonsWebServiceConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SortParamsArgumentResolver());
        resolvers.add(new CritiriaParamsArgumentResolver());
        resolvers.add(new SearchValueParamsArgumentResolver());
        resolvers.add(new PathVariableEnCodeResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateConverter());
        registry.addConverter(new LocalDateTimeConverter());
    }
}
