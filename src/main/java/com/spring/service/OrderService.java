package com.spring.service;

import com.spring.domain.BuyTemp;
import com.spring.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public List<BuyTemp> findByMid(int mid) {
		return orderRepository.findByMid(mid);
	}

	public int findPidExist(int pid) {
		return orderRepository.findPidExist(pid);
	}
	
	public BuyTemp findOne(int pid) {
		return orderRepository.findByPid(pid);
	}

	public BuyTemp save(BuyTemp temp) {
		return orderRepository.save(temp);
	}

	// 刪除一個列入品項
	@Transactional
	public void deleteByMidAndPid(int mid, int pid) {
		orderRepository.deleteByMidAndPid(mid, pid);
	}

	// 訂單送出後刪除當前使用者的 temp 實體 
	@Transactional
	public void deleteByMid(int mid) {
		orderRepository.deleteByMid(mid);
	}

	// 更改要購買的數量
	@Transactional
	public int updateAmount(int pid, int amount) {
		return orderRepository.updateAmount(amount, pid);
	}
}
