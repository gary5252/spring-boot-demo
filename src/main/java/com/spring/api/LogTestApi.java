package com.spring.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LogTestApi {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/log")
	public String log() {
		// 日誌的字串測試
//		String name = "test1";
//		String email = "blah@email.com";
		
		logger.info("----------- info -------------");
//		logger.error("error --- log");
//		logger.warn("warn --- log");
//		logger.info("info --- log");
//		logger.debug("debug --- log");
//		logger.trace("trace --- log");
//		// 日誌盡量不要使用 "+" 串接字串，可以使用它自帶的方式 : 使用佔位符號 {}
//		logger.info("name : {}, email : {}", name, email);
		
		return "logtest";
	}
}
