package com.spring.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
//@Controller
@RequestMapping("/api/v1") // URL 共同前綴可以放在這
public class HelloController {
	
//	@Autowired
//	private Product prod;

//	@RequestMapping(value = "/say", method= RequestMethod.GET)
	@RequestMapping("/say")
	public String hello() {
		return "Hello Spring Boot";
	}

	@GetMapping("/products")
//	@ResponseBody
	public Object getAll(@RequestParam(value = "page", defaultValue = "1") int page,
						 @RequestParam(value = "size", defaultValue = "10") int size) {

		Map<String, Object> product = new HashMap<>();
		product.put("name", "PC");
		product.put("price", 20000);
		product.put("amount", 50);
		Map<String, Object> product1 = new HashMap<>();
		product1.put("name", "Phone");
		product1.put("price", 30000);
		product1.put("amount", 100);
		ArrayList<Map<String, Object>> contents = new ArrayList<>();
		contents.add(product);
		contents.add(product1);
		
		Map<String, Object> pagemap = new HashMap<>();
		pagemap.put("page", page);
		pagemap.put("size", size);
		pagemap.put("content", contents);
		
		return pagemap;
	}

	@GetMapping("/products/{id}/{buyer:[A-Z_]+}")
	public Object getOne(@PathVariable int id, @PathVariable String buyer) {
		// 若變數名稱不同於 URI > (@PathVariable("id") int pid)

		Map<String, Object> product = new HashMap<>();
		product.put("name", "PC");
		product.put("price", 20000);
		product.put("amount", 50);
		product.put("pid", id);
		product.put("buyer", buyer);
		return product;
	}
	
//	前端透過 POST 提交參數到後端，透過此註解接收對應 KEY:VALUE
	@PostMapping("/products")
	public Object post(@RequestParam("name") String name,
					   @RequestParam("price") int price,
					   @RequestParam("amount") int amount) {
		Map<String, Object> product = new HashMap<String, Object>();
		product.put("name", name);
		product.put("price", price);
		product.put("amount", amount);
		
		return product;
	}

}
