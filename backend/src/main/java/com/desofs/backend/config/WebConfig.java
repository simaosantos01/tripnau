package com.desofs.backend.config;

import com.desofs.backend.interceptors.RateLimitingInterceptor;
import com.desofs.backend.services.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtDecoder jwtDecoder;
    private final RateService rateService;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new RateLimitingInterceptor(jwtDecoder,rateService));
    }
}
