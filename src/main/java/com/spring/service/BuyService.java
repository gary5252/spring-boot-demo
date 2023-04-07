package com.spring.service;

import com.spring.domain.BuyList;
import com.spring.domain.BuyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BuyService {

	@Autowired
	private BuyRepository buyRepository;
	
	public BuyList save(BuyList buyList) {
		return buyRepository.save(buyList);
	}
}
