package com.ice;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月18日
 * @Desc 部署时候使用
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.sources(IceEquiApplication.class);/*启动配置*/
		return application;
	}

}
