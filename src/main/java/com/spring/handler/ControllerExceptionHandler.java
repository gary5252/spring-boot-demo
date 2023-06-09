package com.spring.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.ModelAndView;

// 設置攔截器
@ControllerAdvice
public class ControllerExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	// 例外處理
	@ExceptionHandler({Exception.class})
	public ModelAndView handleException(HttpServletRequest request, Exception e) throws Exception {
	
		logger.error("Request URL : {} , Exception : {}",request.getRequestURL(),e.getMessage());
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("url",request.getRequestURL());
		mv.addObject("exception",e);
//			mv.setViewName("error/500");
		mv.setViewName("error/error");
		
		return mv;
	}
}
