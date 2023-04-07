package com.spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<BuyTemp, Integer> {

	List<BuyTemp> findByMid(int mid);
	
	BuyTemp findByPid(int pid);
	
	@Query(value = "select count(*) from BUY_TEMP where pid = ?1 ", nativeQuery = true)
	int findPidExist(int pid);
	
	// 刪除單一列入購買品項
	// 記得加入 modifying 註解
	@Transactional
	@Modifying
	void deleteByMidAndPid(int mid, int pid);
	
	// 訂單送出後刪除 temp 實體
	@Transactional
	@Modifying
	void deleteByMid(int mid);
	
	// 自定義更新語句，在這邊使用JPQL是因為若是使用repository的save()，只要沒有傳遞的欄位資料它都會
	// 一律更新為null，所以當只要更新其中幾個欄位的時候用這邊會比較方便
	// 更新以及刪除回傳值都是整數(未更新 0/更新成功 1)所以方法這邊回傳值設int
	// 要操作DML語句(update/delete)需要加上@Modifying這個註解不然會報錯，
	// 並且還要加入事務管理的註解@Transactional，那這邊就設在service層對應的方法上，當然要加在這邊也可以
	@Transactional
	@Modifying
	@Query("update BuyTemp t set t.amount = :amount where pid = :pid ")
	int updateAmount(@Param("amount") int amount, @Param("pid") int pid);
	
	
}
