package com.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.springcloud.controller.myhandler.CustomerBlockHandler;
import com.springcloud.entities.CommonResult;
import com.springcloud.entities.Payment;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {
	@GetMapping("/byResource")
	@SentinelResource(value = "byResource",blockHandler = "handleException")
	public CommonResult byResource() {
		return new CommonResult(200,"按照资源名称限流测试OK",new Payment(2020L,"serial001"));
	}

	public CommonResult handleException(BlockException exception) {
		return new CommonResult(444,exception.getClass().getCanonicalName()+"服务不可用");
	}

	@GetMapping("/rateLimit/byUrl")
	@SentinelResource(value = "byUrl")
	public CommonResult byUrl() {
		return new CommonResult(200,"按照URL限流测试OK",new Payment(2020L,"serial002"));
	}

	@GetMapping("/rateLimit/customerBlockHandler")
	@SentinelResource(value = "customerBlockHandler",
			blockHandlerClass = CustomerBlockHandler.class,
			blockHandler = "handlerException2")
	public CommonResult customerBlockHandler() {
		return new CommonResult(200,"客户自定义",new Payment(2020L,"serial003"));
	}
}
