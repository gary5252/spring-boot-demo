package com.spring.service;

import com.spring.domain.Item;
import com.spring.domain.Member;
import com.spring.Timer.BidTimer;

//import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionService {
	
	Page<Item> findAllByPage(Pageable pageable);
	
//	Page<Item> searchAllItem(String search, Pageable pageable);
	
	Page<Item> searchItem(String search, int status, Pageable pageable);
	
	Item save(Item item);
	
	Item findOne(Long id);
	
	// 競標(更新金額、競標者ID和名稱)
	int bidding(int bidPrice, int bid, String bidder, Long id);
	
	// 結標、流標(更新狀態)
	int statusUpdate(int status, Long id);
	
	// 重新上架(更新狀態)
	int reShelf(Long id);
	
	// 下架(刪除)
	int offShelf(Long id, int mid);
	
	// 抓出 CountDown Timer 剩下的時間
	int timeLeft(Long id);
	
	BidTimer findTimer(Long id);
	
	void countDownStart(Item item, Boolean reShelf);
	
	boolean createRoom(Item item);
	
	boolean joinRoom(Member member, String roomId);
	
	boolean leaveRoom(Member member, String roomId);
	
	void broadcastRoomList();
	
//	void sendMessageToRoom(String roomId, String destination, Object message);
	
	void sendBidInfo(String itemId);
}
