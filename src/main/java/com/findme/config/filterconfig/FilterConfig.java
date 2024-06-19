package com.findme.config.filterconfig;

import com.findme.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilterConfig {

    private final RedisService redisService;

    @Bean
    public FilterRegistrationBean<RequestIdFilter> requestIdFilter() {
        FilterRegistrationBean<RequestIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestIdFilter(redisService));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
