package com._roomthon.irumso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 로컬 디렉토리 경로 설정
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/Users/space/OneDrive/바탕 화면/changhwan space/DEVELOP/PROJECT(9room)/board_image/");
    }
}
