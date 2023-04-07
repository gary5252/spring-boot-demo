package com.spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Item, Long> {
	
	Page<Item> findAll(Pageable pageable);
	
	@Query("select i from Item i where lower(i.name) LIKE lower(concat('%', :search,'%')) or lower(i.description) LIKE lower(concat('%', :search,'%')) ")
	Page<Item> searchAllItem(@Param("search") String search, Pageable pageable);
	
	@Query("select i from Item i where i.status = :status and (lower(i.name) LIKE lower(concat('%', :search,'%')) or lower(i.description) LIKE lower(concat('%', :search,'%'))) ")
	Page<Item> searchItem(@Param("search") String search, @Param("status") int status, Pageable pageable);
	
	// 查找拍賣者的 ID
	@Query("select i.aid from Item i where id = ?1")
	int findAid(Long id);
	
	// 查看得標品
	@Query("select i from Item i where i.bid = :mid and i.status = 2")
	Page<Item> seeMyBidded(@Param("mid") int mid, Pageable pageable);
	
	
	// 自定義更新語句，在這邊使用JPQL是因為若是使用repository的save()，只要沒有傳遞的欄位資料它都會
	// 一律更新為null，所以當只要更新其中幾個欄位的時候用這邊會比較方便
	// 更新以及刪除回傳值都是整數(未更新 0/更新成功 1)所以方法這邊回傳值設int
	// 要操作DML語句(update/delete)需要加上@Modifying這個註解不然會報錯，
	// 並且還要加入事務管理的註解@Transactional，那這邊就設在service層對應的方法上，當然要加在這邊也可以
	// 競價用 JPQL 多加一個 status = 1 的判斷確保
	@Transactional
	@Modifying
	@Query("update Item i set i.bidPrice = :bidPrice, i.bid = :bid, i.bidder = :bidder where id = :id and status = 1")
	int bidding(@Param("bidPrice") int bidPrice, @Param("bid") int bid, @Param("bidder") String bidder, @Param("id") Long id);
	
	// 更新拍賣狀態(1.競標 > 3.流標/2.結標)
	@Transactional
	@Modifying
	@Query("update Item i set i.status = :status where id = :id ")
	int statusUpdate(@Param("status") int status, @Param("id") Long id);
	
	// 重新上架
	@Transactional
	@Modifying
	@Query("update Item i set i.status = 1 where id = :id and status = 3")
	int reShelf(@Param("id") Long id);
	
	// 流標品下架
	@Transactional
	@Modifying
	@Query("delete from Item i where id = :id and status = 3")
	int offShelf(@Param("id") Long id);
	
}
