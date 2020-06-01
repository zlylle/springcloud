package com.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {
	/**
	 * 配置了一个id为path_baidu_news的路由规则：
	 * 当访问地址http://localhost:9527/guonei的时候会自动转发到地址：
	 * @param builder
	 * @returnhttp://news.baidu.com/guonei
	 */
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		RouteLocatorBuilder.Builder routes = builder.routes();
		routes.route("path_baidu_news",
				r->r.path("/guonei")
						.uri("http://news.baidu.com/guonei")).build();
		return routes.build();
	}
}
