package com.spring.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import com.spring.resource.ErrorResource;
import com.spring.resource.InvalidErrorResource;
import com.spring.resource.FieldErrorResource;
import com.spring.exception.*;
import java.util.List;
import java.util.ArrayList;

@RestControllerAdvice
public class ApiExceptionHandler {
	
	// 處理找不到資源發生的例外
	@ExceptionHandler(ProductNotFoundException.class)
	@ResponseBody		// 為了返回 JSON 格式資料要加上這個註解
	public ResponseEntity<?> handleNotFound(RuntimeException e) {
		ErrorResource errorResource = new ErrorResource(e.getMessage());
		return new ResponseEntity<Object>(errorResource,HttpStatus.NOT_FOUND);
	}
	
	// 處理參數驗證失敗導致的例外
	@ExceptionHandler(InvalidRequestException.class)
	@ResponseBody
	public ResponseEntity<?> handleInvalidRequest(InvalidRequestException e) {
		Errors errors = e.getErrors();
		List<FieldErrorResource> fieldErrorResources = new ArrayList<>();
		List<FieldError> fieldErrors = errors.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			FieldErrorResource fieldErrorResource = new FieldErrorResource(fieldError.getObjectName(),
					fieldError.getField(),fieldError.getCode(),fieldError.getDefaultMessage());
			fieldErrorResources.add(fieldErrorResource);
		}
		InvalidErrorResource iER = new InvalidErrorResource(e.getMessage(),fieldErrorResources);
		return new ResponseEntity<Object>(iER,HttpStatus.BAD_REQUEST);
	}
	
	// 處理上述兩個以外的全局例外
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<?> handleException(Exception e) {
		return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
