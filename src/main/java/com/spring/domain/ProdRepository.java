package com.spring.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import org.springframework.data.repository.CrudRepository;
import java.util.List;

//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

//public interface ProdRepository extends CrudRepository<Product, Integer> {
public interface ProdRepository extends JpaRepository<Product, Integer> {
	
	/**
	 * 詳細的SQL方法命名規則可以去Spring官網查詢
	 * 網址 : https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
	 * 5.1.3. Query Methods > Query Creation
	 */

	// Hibernate 會藉由方法的命名生成對應的 SQL 語句，所以方法名要訂好。
	List<Product> findByName(String name);
	
	// 自定義複數條件查詢，一樣名稱要對應好映射的 Table，參數的順序影片是沒說有沒有影響，但為防萬一
	// 還是跟方法名比照順序。
	List<Product> findByNameAndPrice(String name, int price);
	
	// 模糊查詢之一，內容以什麼結尾來做查詢 > SQL : LIKE '%xxx'
	List<Product> findByDescriptionEndsWith(String des);
	
	// 模糊查詢之一，內容包含什麼值(Contains/Containing)兩個都測過了結果都一樣 > SQL : LIKE '%xxx%'
	List<Product> findByDescriptionContaining(String des);
	
	// 使用 JPQL 做自定義SQL語句查詢，藉由@Query生成帶入的字串SQL > 
	// p 為變數名稱可以依需求定義，from Product p 一定要是實體類別所以名稱不能錯，
	// ?1為要帶入的參數而1代表參數順序所以像 ?2 就是要帶入的第二個參數
	@Query("select p from Product p where length(p.name) > ?1")
	List<Product> findByJPQL(int len);
	
	// 使用原生SQL語句查詢方式:
	// 這邊的PRODUCT是Table名稱所以不用賦予變數名，然後記得要把nativeQuery設為true
	@Query(value = "select * from PRODUCT where price > ?1", nativeQuery = true)
	List<Product> findBySQL(int price);
	
	// Object很方便，字串整數都可以使用
	@Query(value = "select name,sum(price) from PRODUCT where price > ?1 group by name", nativeQuery = true)
	List<Object> findBySQLtest(int price);
	
	// 作業 : 查詢複合表格資料後(List<Object>)，再從查詢結果的列表取其中的值
	// 使用.get()取得Object[]，透過迭代取所有資料列的該欄位值或是直接限定.get()中的索引再從[]取單一值
	@Query(value = "SELECT NAME,DESCRIPTION,ADDRESS,AMOUNT FROM PRODUCT,CUSTOMER WHERE PRODUCT.CID = CUSTOMER.CID ORDER BY CUSTOMER.CID", nativeQuery = true)
	List<Object[]> findBySQLHW();
	
	
	// 自定義更新語句，在這邊使用JPQL是因為若是使用repository的save()，只要沒有傳遞的欄位資料它都會
	// 一律更新為null，所以當只要更新其中幾個欄位的時候用這邊會比較方便
	// 更新以及刪除回傳值都是整數(未更新 0/更新成功 1)所以方法這邊回傳值設int
	// 要操作DML語句(update/delete)需要加上@Modifying這個註解不然會報錯，
	// 並且還要加入事務管理的註解@Transactional，那這邊就設在service層對應的方法上，當然要加在這邊也可以
	@Transactional
	@Modifying
	@Query("update Product p set p.amount = ?1 where pid = ?2")
	int updateAmount(int amount, int pid);
	
	// 自定義刪除的部分基本跟更新一樣，但刪除就該筆資料全刪了其實就直接用原生的簡單刪除方法就好沒必要特地來這邊自定義
	@Transactional
	@Modifying
	@Query("delete from Product p where p.pid = ?1")
	int deleteByJPQL(int pid);
	
	// 分頁查詢列表
	Page<Product> findAll(Pageable pageable);
	
	// ===========================================================================
	// SQL分頁條件查詢(存貨多寡以及輸入框模糊搜尋) 這個似乎不能使用函數 (upper(),lower(),...)
//	@Query(value = "select * from PRODUCT where UPPER(name) LIKE UPPER(%:search%) or UPPER(description) LIKE UPPER(%:search%) ", nativeQuery = true)
//	Page<Product> searchProduct(@Param("search") String search, Pageable pageable);
//	
//	@Query(value = "select * from PRODUCT where amount > 100 and amount < 2000 and (UPPER(name) LIKE UPPER(%:search%) or UPPER(description) LIKE UPPER(%:search%)) ", nativeQuery = true)
//	Page<Product> searchProductWithNormal(@Param("search") String search, Pageable pageable);
//	
//	@Query(value = "select * from PRODUCT where amount >= 2000 and (UPPER(name) LIKE UPPER(%:search%) or UPPER(description) LIKE UPPER(%:search%)) ", nativeQuery = true)
//	Page<Product> searchProductWithOver(@Param("search") String search, Pageable pageable);
//	
//	@Query(value = "select * from PRODUCT where amount <= 100 and (UPPER(name) LIKE UPPER(%:search%) or UPPER(description) LIKE UPPER(%:search%)) ", nativeQuery = true)
//	Page<Product> searchProductWithOut(@Param("search") String search, Pageable pageable);
	// ===========================================================================
	// JPQL分頁條件查詢(存貨多寡以及輸入框模糊搜尋) 找到報錯原因了最後少加一個 ")"，最後測試運行成功
	@Query("select p from Product p where lower(p.name) LIKE lower(concat('%', :search,'%')) or lower(p.description) LIKE lower(concat('%', :search,'%')) ")
	Page<Product> searchProduct(@Param("search") String search, Pageable pageable);
	
	@Query("select p from Product p where p.amount > 100 and p.amount < 2000 and (lower(p.name) LIKE lower(concat('%', :search,'%')) or lower(p.description) LIKE lower(concat('%', :search,'%'))) ")
	Page<Product> searchProductWithNormal(@Param("search") String search, Pageable pageable);
	
	@Query("select p from Product p where p.amount >= 2000 and (lower(p.name) LIKE lower(concat('%', :search,'%')) or lower(p.description) LIKE lower(concat('%', :search,'%'))) ")
	Page<Product> searchProductWithOver(@Param("search") String search, Pageable pageable);
	
	@Query("select p from Product p where p.amount <= 100 and (lower(p.name) LIKE lower(concat('%', :search,'%')) or lower(p.description) LIKE lower(concat('%', :search,'%'))) ")
	Page<Product> searchProductWithOut(@Param("search") String search, Pageable pageable);
	// ===========================================================================
	
	// 找某商品的目前數量
	@Query("select p.amount from Product p where pid = :pid")
	int findAmount(@Param("pid") int pid);
}
