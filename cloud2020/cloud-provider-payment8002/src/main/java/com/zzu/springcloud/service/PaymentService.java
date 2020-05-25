package com.zzu.springcloud.service;

import com.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

public interface PaymentService {
	public int create(Payment payment); //增

	public Payment getPaymentById(@Param("id") Long id); //查
}
