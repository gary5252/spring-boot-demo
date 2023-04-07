package com.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.NoSuchElementException;

/**
 * 測試報錯一直是 500 沒有套用到HttpStatus.NOT_FOUND
 * 換成系統提示的例外類型(NoSuch...)還是無法
 * 在會員資料修改那邊測試也是一樣
 * 可能版本號問題這個annotation已經不能用了，這個異常處理網頁的功能就暫且這樣
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
//public class ProductNotFoundException extends NoSuchElementException {

	public ProductNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProductNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	//public class ProductNotFound extends NoSuchElementException {




}
