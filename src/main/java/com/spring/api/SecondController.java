package com.spring.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/apiTest2")
public class SecondController {

	@Autowired
	private TestServiceImpl testServiceImpl;
	
//	@GetMapping("/GetClasses2")
//	@PostMapping("/GetClasses")
//    public List<TestClass> findClasses2() {
//		
//        var classes = (List<TestClass>) secondTestService.findAll();
//
//        return classes;
//    }
	
	@GetMapping("/anotherAPI")
	public List<TestClass> getAnotherAPI(){
		return testServiceImpl.useAnotherServer();
	}
}
