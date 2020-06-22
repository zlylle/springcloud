package com.springcloud.alibaba.service.impl;

import com.springcloud.alibaba.dao.OrderDao;
import com.springcloud.alibaba.domain.Order;
import com.springcloud.alibaba.service.AccountService;
import com.springcloud.alibaba.service.OrderService;
import com.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
	@Resource
	private OrderDao orderDao;
	@Resource
	private StorageService storageService;
	@Resource
	private AccountService accountService;

	/**
	 * 创建订单->调用库存微服务扣减库存->调用账户微服务扣减账户余额->修改订单状态
	 * @param order
	 */
	@Override
	@GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
	public void creat(Order order) {
		//1.新建订单
		log.info("开始新建订单");
		orderDao.create(order);

		//2.扣减库存
		log.info("订单微服务开始调用库存，做扣减Count");
		storageService.decrease(order.getProductId(),order.getCount());
		log.info("订单微服务开始调用库存做扣减结束");

		//3.扣减金额
		log.info("订单微服务开始调用账户，做扣减Money");
		accountService.decrease(order.getUserId(),order.getMoney());
		log.info("订单微服务开始调用库存做扣减结束");

		//4.修改订单状态，1代表已完成，0代表未完成
		log.info("修改订单状态开始");
		orderDao.update(order.getUserId(),0);
		log.info("修改订单状态完成");

		log.info("下订单操作全部完成~");
	}
}
