package com.example.userservice.config;

import com.example.userservice.filter.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FilterConfig {

    @Autowired
    private Environment env;

    @Bean
    //MyFilter 필터 빈 생성하고 등록
    public FilterRegistrationBean<MyFilter> createFilter() {
        FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>(new MyFilter(env));

        //MyFilter의 doFilter() 메서드를 적용할 대상 url
        //token을 가져왔는지 아닌지 확인
        bean.addUrlPatterns("/user2/*");
        //bean.addUrlPatterns("/hello/*");
        bean.setOrder(0);

        return bean;
    }
}
