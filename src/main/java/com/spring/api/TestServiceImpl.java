package com.spring.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestServiceImpl {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<TestClass> useAnotherServer(){
		String url = "http://localhost:8888/api2/test/GetClasses2";
		return this.restTemplate.getForObject(url, List.class);
	}

	public List<TestClass> findAll() {

		var classes = new ArrayList<TestClass>();
		classes.add(new TestClass(1L, "test1", 23));
		classes.add(new TestClass(2L, "test2", 23253));
		classes.add(new TestClass(3L, "test3", 32));
		classes.add(new TestClass(4L, "test4", 1));
		classes.add(new TestClass(5L, "test5", 7878));
		classes.add(new TestClass(6L, "test6", 54));
		classes.add(new TestClass(7L, "test7", 3));
		classes.add(new TestClass(8L, "test8", 666));
		
		return classes;
	}
	


}
