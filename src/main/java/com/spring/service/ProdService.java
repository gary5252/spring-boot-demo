package com.spring.service;

import com.spring.domain.Product;
import com.spring.domain.ProdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class ProdService {
	
	@Autowired
	private ProdRepository prodRepository;
	
	/**
	 * 這個不知為啥找不到從資料庫寫進去的資料，** 01/13 早上試又可以抓到了
	 * 找到原因了，用 oracle devOP 寫 SQL 的要再重新連線一次就可以成功至抓到了
	 * @return
	 */
	public List<Product> findAll(){
		return prodRepository.findAll();
	}
	
	// 分頁查詢  * 影片用的很多方法都棄用或是改變了，重點在要獲得什麼樣的類型資料以及參數型態
	public Page<Product> findAllByPage(Pageable pageable){
//		Sort sort = new Sort(Sort.Direction.DESC, "pid"); (棄用)
//		Sort sort = Sort.by(Sort.Direction.DESC, "pid");
//		Pageable pageable = new PageRequest(1,5,sort);  (棄用)
//		Pageable pageable = PageRequest.of(1, 5, sort);
		return prodRepository.findAll(pageable);
	}
	
	// =============================================================================
	// 分頁條件查詢 (存貨多寡以及輸入框模糊搜尋)
	public Page<Product> searchProduct(String search, Pageable pageable){
		return prodRepository.searchProduct(search, pageable);
	}
	
	public Page<Product> searchProductWithNormal(String search, Pageable pageable){
		return prodRepository.searchProductWithNormal(search, pageable);
	}
	
	public Page<Product> searchProductWithOver(String search, Pageable pageable){
		return prodRepository.searchProductWithOver(search, pageable);
	}
	
	public Page<Product> searchProductWithOut(String search, Pageable pageable){
		return prodRepository.searchProductWithOut(search, pageable);
	}
	
	// =============================================================================
	public Product save(Product prod) {
		return prodRepository.save(prod);
	}
	
	/**
	 * 影片是使用 repository.findOne(Long id)，但現在好像沒的用了 
	 * @param pid
	 * @return
	 */
	public Optional<Product> findOne(int pid) {
		return prodRepository.findById(pid);
	}
	
	
	public void deleteOne(int pid) {
		prodRepository.deleteById(pid);
	}
	
	// 這個方法是自定義在 repository interface 的，並非 JpaRepository 原生方法
	public List<Product> findByName(String name){
		return prodRepository.findByName(name);
	}
	
	// 在介面自定義的複數條件查詢，同樣並非原生方法
	public List<Product> findByNameAndPrice(String name, int price){
		return prodRepository.findByNameAndPrice(name, price);
	}
	
	// 模糊查詢之一，查找內容結尾
	public List<Product> findByDescriptionEndsWith(String des){
		return prodRepository.findByDescriptionEndsWith(des);
	}
	
	// 模糊查詢之一，查找內容包含
	public List<Product> findByDescriptionContains(String des){
		return prodRepository.findByDescriptionContaining(des);
	}
	
	// 自定義JPQL語句的查詢法，SQL內容見repository介面
	public List<Product> findByJPQL(int len){
		return prodRepository.findByJPQL(len);
	} 
	
	// 自定義原生SQL語句的查詢法，SQL內容見repository介面
	public List<Product> findBySQL(int price){
		return prodRepository.findBySQL(price);
	} 
	
	// Object很好用BJ4
	public List<Object> findBySQLtest(int price){
		return prodRepository.findBySQLtest(price);
	}
	
	// 作業:獲取複合查詢的列表後，在從其中抓取特定值
	public List<Object[]> findBySQLHW(){
		return prodRepository.findBySQLHW();
	}
	
	// 自定義更新，詳見repository
	// DML(update/delete)要加入事務管理的註解不然會報錯，也可以加在repository那邊
	// 納入事務管理是為了確保行動統一，在複數行動中要馬全部行動都正確執行，要馬就會讓所有行動一起失敗
	// 而行動失敗就會全部回到行動前的狀態
	// 因為service層是調用repository那邊的方法，事務管理比較後面才宣告，
	// 如果那邊有設事務管理而這邊也有且參數不一致，這邊的將會覆蓋過repository那邊的事務管理
	@Transactional
	public int updateAmount(int pid, int amount) {
		return prodRepository.updateAmount(amount, pid);
	}
	
	@Transactional
	public int deleteByJPQL(int pid) {
		return prodRepository.deleteByJPQL(pid);
	}
	
	// 測試事務管理，讓其中一個操作失敗，來檢視事務管理的功能:所有操作都要成功執行，否則全部回溯原本的狀態
	@Transactional
	public int deleteAndUpdate(int pid, int amount, int uid) {
		int dcount = prodRepository.deleteByJPQL(pid);
		
		int ucount = prodRepository.updateAmount(amount, uid);
		
		return dcount + ucount;
	}
	
	// 查詢某商品目前的數量
	public int findAmount(int pid) {
		return prodRepository.findAmount(pid);
	}
}
