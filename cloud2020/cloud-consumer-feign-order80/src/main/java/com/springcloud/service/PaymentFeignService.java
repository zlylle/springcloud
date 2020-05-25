package com.springcloud.service;

import com.springcloud.entities.CommonResult;
import com.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE") //目标微服务的名称
public interface PaymentFeignService {
	//Feign接口中的方法要与目标服务中的Controller中的方法完全一致
	@GetMapping(value = "/payment/get/{id}")
	public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

	@GetMapping(value = "/payment/feign/timeout")
	public String paymentFeignTimeout();
}
