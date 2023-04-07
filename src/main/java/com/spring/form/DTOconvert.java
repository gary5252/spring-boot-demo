package com.spring.form;



public interface DTOconvert<S, T> {
	T convert(S s,T t);
	
	T convert(S s);
}
