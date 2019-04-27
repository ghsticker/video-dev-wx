package com.test;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @Description 继承SpringBootServletInitializer，使用web.xml的形式就行部署
 * @author ghsticker
 * 2019年4月19日
 */
public class WarStartApplication extends SpringBootServletInitializer{

	/**
	 * 重写配置
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		//web.xml 运行应用程序，指向Application，最后启动SpringBoot
		return builder.sources(Application.class);
	}
	
	
}

