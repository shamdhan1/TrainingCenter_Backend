package com.trainingcenter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpaWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root URL to index.html
        registry.addRedirectViewController("/", "/index.html");

        // Map explicit SPA client-side routes to forward to index.html
        registry.addViewController("/dashboard").setViewName("forward:/index.html");
        registry.addViewController("/centers").setViewName("forward:/index.html");
        registry.addViewController("/courses").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/index.html");
    }
}
