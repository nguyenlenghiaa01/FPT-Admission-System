package com.example.report_service.Config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor feignClientInterceptor() {
        return new OpenFeignClientInterceptor();
    }
}

