package com.kdt_y_be_toy_project2.global.config;

import com.kdt_y_be_toy_project2.global.resolver.LoginInfoArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInfoArgumentResolver loginInfoArgumentResolver;

    public WebMvcConfig(LoginInfoArgumentResolver loginInfoArgumentResolver) {
        this.loginInfoArgumentResolver = loginInfoArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginInfoArgumentResolver);
    }
}
