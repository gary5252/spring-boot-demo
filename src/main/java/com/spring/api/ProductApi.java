package com.spring.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import com.spring.domain.Product;
import com.spring.form.ProductDTO;
import com.spring.service.ProdService;
import com.spring.util.ProductBeanUtils;
import com.spring.exception.ProductNotFoundException;
import com.spring.exception.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.ArrayList;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;


/**
 * 使用RESTful API 達成CRUD
 * 並納入驗證以及Http 狀態
 * 所有的API封裝返回物件類型都要一樣(ResponseEntity<?>)
 */


@RestController
@RequestMapping("/api/v3")
public class ProductApi {
	
	@Autowired
	private ProdService prodService;

	// 查詢全部 Products
	@GetMapping("/products")
	public ResponseEntity<?> listAllProducts() {
		List<Product> products = prodService.findAll();
		if (products.isEmpty()) {
			throw new ProductNotFoundException("Products Not Found");
		}
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	
	// 查詢單一商品訊息
	@GetMapping("/products/{pid}")
	public ResponseEntity<?> getProduct(@PathVariable int pid) {
		Product product = prodService.findOne(pid).get();
		if (product == null) {
			throw new ProductNotFoundException(String.format("Product by ID %s not found", pid));
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	// 新增一個商品
	@PostMapping("/products")
	public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter",result);
		}
		// 客戶端不一定使用網頁傳輸資料，像手機就是傳遞JSON格式的資料，所以設置註解@RequestBody
		// 讓參數能接收JSON資料
		Product product1 = prodService.save(productDTO.convert());
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
	}
	
	// 更新一個商品訊息
	@PutMapping("/products/{pid}")
	public ResponseEntity<?> updateProduct(@PathVariable int pid,@RequestBody ProductDTO productDTO, BindingResult result) {
		// 客戶端不一定使用網頁傳輸資料，像手機就是傳遞JSON格式的資料，所以設置註解@RequestBody
		// 讓參數能接收JSON資料
		Product currentProduct = prodService.findOne(pid).get();
		if (currentProduct == null) {
			throw new ProductNotFoundException(String.format("Product by ID %s not found", pid));
		}
		if (result.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter",result);
		}
//		BeanUtils.copyProperties(productDTO, currentProduct);
		// 除外欄位資料為空的屬性來寫入實體類別
		productDTO.convert(currentProduct);
		Product product = prodService.save(currentProduct);
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/products/{pid}")
	public ResponseEntity<?> deleteProduct(@PathVariable int pid) {
		prodService.deleteOne(pid);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	
}
