package com.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.test.controller.interceptor.MiniInterceptor;

/**
 * @Description 配置
 * @author ghsticker
 * 2019年3月4日
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/META-INF/resources/")
		.addResourceLocations("file:C:/videos-dev/");
	}

	@Bean(initMethod="init")
	public ZKCuratorClient zkCuratorClient(){
		return new ZKCuratorClient();
	}
	
	@Bean
	public MiniInterceptor miniInterceptor(){
		return new MiniInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(miniInterceptor())
		.addPathPatterns("/bgm/**")
		.addPathPatterns("/video/upload","/video/uploadCover",
				"/video/userLike","/video/userUnLike",
				"/video/saveComment")
		.addPathPatterns("/user/**")
		.excludePathPatterns("/user/queryPublisher");
		
		super.addInterceptors(registry);
	}
	
	
	
	
	
}

