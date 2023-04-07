package com.spring.resource;


/**
 * 這邊負責封裝例外訊息並發送至調用的地方
 * 
 */

public class InvalidErrorResource {

	private String message;
	private Object errors;
	public InvalidErrorResource(String message, Object errors) {
		super();
		this.message = message;
		this.errors = errors;
	}
	public String getMessage() {
		return message;
	}
	public Object getErrors() {
		return errors;
	}
	
}
