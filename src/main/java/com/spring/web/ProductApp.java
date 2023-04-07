package com.spring.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.service.ProdService;
import com.spring.domain.Product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/v2")
public class ProductApp {

	@Autowired
	private ProdService prodService;
	
	
//	查詢所有 ----------------------------------
	@GetMapping("/products")
	public List<Product> getAll() {
		return prodService.findAll();
	}
	
	// 分頁查詢所有列表
	@GetMapping("/products/page")
	public Page<Product> getAllByPage(@PageableDefault(size = 5,sort = {"pid"},direction = Sort.Direction.DESC) Pageable pageable){
//									  @RequestParam(defaultValue="0") int page,
//									  @RequestParam(defaultValue="10") int size){
//		Sort sort = Sort.by(Sort.Direction.DESC, "pid");
//		return prodService.findAllByPage(PageRequest.of(page, size, sort));
		return prodService.findAllByPage(pageable);
	}

	
	/**
	 * 若屬性資料太多為求便利可以直接寫入物件，但要記得傳遞的參數名稱一定要能對上
	 * 不知為啥原本用 SQL 直接寫進 DB 的資料就算刪除了還是沒辦法使用他們的 PK 建資料
	 * 找到原因了，用 oracle devOP 寫 SQL 的要再重新連線一次就可以成功至抓到了
	 * @param product
	 * @return
	 */
//	新增 ----------------------------------
	@PostMapping("/products")
	public Product post(Product product) {
		return prodService.save(product);
	}
//	public Product post(@RequestParam int pid, 
//						@RequestParam String name, 
//						@RequestParam int price,
//						@RequestParam int amount, 
//						@RequestParam String description) {
//		Product product = new Product();
//		product.setPid(pid);
//		product.setName(name);
//		product.setPrice(price);
//		product.setAmount(amount);
//		product.setDescription(description);
//
//		return prodService.save(product);
//	}

	/**
	 * 找不到資料一直報 500 錯，感覺是 method 的問題，
	 * findAll() 可以找到並顯示資料代表與 DB 的連線正常，
	 * 但這邊單一搜尋就一直出包
	 * 成功執行了，現在問題只剩下他抓不到 DB 原本用 SQL 寫進去的資料
	 * 找到原因了，用 oracle devOP 寫 SQL 的要再重新連線一次就可以成功至抓到了
	 * @param pid
	 * @return
	 */
//	單一查詢 ----------------------------------
	@GetMapping("/products/{pid}")
	public Optional<Product> getOne(@PathVariable("pid") int pid) {
		return prodService.findOne(pid);
	}
	

	/**
	 * 跟 POST 一樣，而影片說不要用 POSTMAN 的 form-data，要用
	 * x-www-form-urlencoded 來操作，但我用這個不管是 POST 還是 PUT 模式都會報 500 的 error，
	 * 反而是用 form-data 兩個模式都能正常運行。
	 * @param pid
	 * @param name
	 * @param price
	 * @param amount
	 * @param description
	 * @return
	 */
//	修改 ----------------------------------
	@PutMapping("/products")
	public Product update(@RequestParam int pid, 
						  @RequestParam String name, 
						  @RequestParam int price,
						  @RequestParam int amount, 
						  @RequestParam String description) {
		Product product = new Product();
		product.setPid(pid);
		product.setName(name);
		product.setPrice(price);
		product.setAmount(amount);
		product.setDescription(description);

		return prodService.save(product);
	}

//	刪除 ----------------------------------
	@DeleteMapping("/products/{pid}")
	public void deleteOne(@PathVariable int pid) {
		prodService.deleteOne(pid);
	}
	
//	條件查詢(自定義) ----------------------------------
	// 這個查詢一樣參數要帶齊，可以考慮賦予預設值減少參數限制
	@PostMapping("/products/by")
	public List<Product> findByName(@RequestParam int len){
//									@RequestParam("description") String des){
//									@RequestParam("price") int price){
//		return prodService.findByName(name);
//		return prodService.findByNameAndPrice(name, price);
//		return prodService.findByDescriptionEndsWith(des);
//		return prodService.findByDescriptionContains(des);
		return prodService.findByJPQL(len);
//		return prodService.findBySQL(price);
	}
	
	@GetMapping("/products/test/{price}")
	public List<Object> findTest(@PathVariable int price){
		return prodService.findBySQLtest(price);
	}
	
	@GetMapping("/products/hw")
	public List<Object[]> findHW(){
//	public String findHW(){
		List<Object[]> test = prodService.findBySQLHW();
		Object[] test1 = test.get(0);
		System.out.println("------- 單一值 ------" + test1[2]);
		System.out.println("樓下為用迭代取全部資料列的某欄位值");
		for(int i = 0 ; i < test.size(); i++ ){
			// 需要特定的資料列就從 .get()設定定值在抓[]要哪一個欄位
			Object[] testO = test.get(i);
			System.out.println(">>>>>>>>>>>>>>" + testO[0]);
		}
		return test;
//		return (String) test1[2];
	}
	
	// 自定義更新，有一些注意事項詳見介面和service
	@PostMapping("/products/update")
	public int updateByJPQL(@RequestParam int amount, @RequestParam int pid) {
		return prodService.updateAmount(amount, pid);
	}
	
	// 自定義刪除，有一些注意事項詳見介面和service
	@PostMapping("/products/delete")
	public int deleteByJPQL(@RequestParam int pid) {
		return prodService.deleteByJPQL(pid);
	}
	
	// 測試事務管理
	@PostMapping("/products/transactional")
	public int transactionalTest(@RequestParam int pid, 
								 @RequestParam int amount,
								 @RequestParam int uid) {
		return prodService.deleteAndUpdate(pid, amount, uid);
	}
}
