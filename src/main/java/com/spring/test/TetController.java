package com.spring.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/test")
public class TetController {

    @Autowired
    private EntityTest entityTest;
    
    @Autowired
    private TestUuidRepository uuRepository;
    
    @GetMapping("/findalluuid")
    public List<TestUuid> findAllUuid(){
    	return uuRepository.findAll();
    }
    
    @PostMapping("/insertuuid")
    public TestUuid insertUuid() {
    	TestUuid test = new TestUuid();
    	test.setName("second");
    	test.setValue(2);
    	return uuRepository.save(test);
    	
    }

    @GetMapping("/findall")
    public List<ArticleTest> findAll() {
        return entityTest.findAll();
    }

    @PostMapping("/insert")
    public ArticleTest post() {
    	// 類別的 List 屬性因為沒有初始化所以開始還是 null 所以沒辦法用 GETTER，除非 GETTER 中有先定義好初始化
        ArticleTest test = new ArticleTest();
        List<String> stringList = new ArrayList<>();
//        List<String> stringList = test.getGoods();
        for (int i = 0; i < 3; i++) {
            String value1 = "testList" + i;
            stringList.add(value1);
//            test.getGoods().add(value1);
        }
        List<Integer> intList = new ArrayList<>();
//        List<Integer> intList = test.getGoodsAmount();
        for (int i = 0; i < 3; i++) {
        	intList.add(i);
//            test.getGoodsAmount().add(i);
        }

        test.setTitle("title1");
        test.setContent("content1");
        test.setGoods(stringList);
        test.setGoodsAmount(intList);

        return entityTest.save(test);
    }
}
