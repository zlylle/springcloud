package com.zzu.springcloud.service.impl;

import com.zzu.springcloud.dao.PaymentDao;
import com.springcloud.entities.Payment;
import com.zzu.springcloud.service.PaymentService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Resource
	private PaymentDao paymentDao;

	public int create(Payment payment) {
		return paymentDao.create(payment);
	}

	public Payment getPaymentById(Long id) {
		return paymentDao.getPaymentById(id);
	}
}
