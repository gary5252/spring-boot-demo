package com.spring.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import com.testapi.api.TestServiceTwo;

@Controller
@RequestMapping("/api1/test")
public class TestController {

	@Autowired
	private TestServiceImpl testServiceImpl;
	
//	@Autowired
//	private TestServiceTwo testServiceTwo;
//	
//	@GetMapping("/anotherAPI")
//    public List<TestClassTwo> findClasses2() {
//		
//        var classesTwo = (List<TestClassTwo>) testServiceTwo.findAll();
//
//        return classesTwo;
//    } 
	
    @GetMapping(value = {"/",""})
    public String index() {
        return "testIndex";
    }

    @RequestMapping(path = "/GetClasses", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public List<TestClass> findClasses() {

        var classes = (List<TestClass>) testServiceImpl.findAll();

        return classes;
    }

}
