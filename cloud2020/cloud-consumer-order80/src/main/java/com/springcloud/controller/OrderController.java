package com.springcloud.controller;

import com.springcloud.entities.CommonResult;
import com.springcloud.entities.Payment;
import com.springcloud.lb.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

	//public static final String PAYMENT_URL = "http://localhost:8001";
	public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE"; //加入Eureka后通过注册中心里的服务名来查找

	@Resource
	private RestTemplate restTemplate;
	@Resource
	private LoadBalance loadBalance;
	@Resource
	private DiscoveryClient discoveryClient;


	@GetMapping("/consumer/payment/create")
	public CommonResult<Payment> create(Payment payment) {
		return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
		//return restTemplate.postForEntity(PAYMENT_URL+"/payment/create",payment,CommonResult.class).getBody();
	}

	/**
	 * getForObject：返回对象为响应体中数据转化成的对象，可以理解为Json
	 * getForEntity：返回对象为ResponseEntity，包含了响应中的一些重要信息，比如响应头、响应状态码、响应体等
	 * @param id
	 * @return
	 */
	@GetMapping("/consumer/payment/get/{id}")
	public CommonResult<Payment> getPayment(@PathVariable("id") Long id) {
		return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
	}

	@GetMapping("/consumer/payment/getForEntity/get/{id}")
	public CommonResult<Payment> getPayment1(@PathVariable("id") Long id) {
		ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
		if(entity.getStatusCode().is2xxSuccessful()) {
			return entity.getBody();
		}else {
			return new CommonResult<>(444,"操作失败");
		}
	}

	@GetMapping(value = "/consumer/payment/lb")
	public String getPaymentLB() {
		List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
		if(instances==null || instances.size()<=0) {
			return null;
		}
		ServiceInstance serviceInstance = loadBalance.instances(instances);
		URI uri = serviceInstance.getUri();

		return restTemplate.getForObject(uri+"/payment/lb",String.class);
	}

}
