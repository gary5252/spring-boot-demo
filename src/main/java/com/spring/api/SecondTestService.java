package com.spring.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SecondTestService {
	public List<TestClass> findAll() {

		var classes = new ArrayList<TestClass>();
		classes.add(new TestClass(10L, "second1", 10000));
		classes.add(new TestClass(20L, "second2", 20000));
		classes.add(new TestClass(30L, "second3", 30000));
		classes.add(new TestClass(40L, "second4", 40000));
		classes.add(new TestClass(50L, "second5", 50000));
		
		return classes;
	}

}
