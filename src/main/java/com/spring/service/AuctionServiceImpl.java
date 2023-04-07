package com.spring.service;

import com.spring.domain.AuctionRepository;
import com.spring.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.messaging.simp.SimpMessagingTemplate;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
import java.util.*;
import com.spring.Timer.BidTimer;
import com.spring.Timer.BidTimerList;
import com.spring.domain.BidRoomList;
import com.spring.domain.BidderStatus;
import com.spring.domain.Member;
import com.spring.domain.BidRoom;
import com.spring.response.RoomListResponse;

/**
 * 雖然納入事務會造成 DB 那邊有點問題，
 * 但反正還是能運行就先納入事務管理試試看功能
 */

@Service
public class AuctionServiceImpl implements AuctionService {
	
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	public BidTimerList bidTimerList;
	
	@Autowired
	private BidRoomList bidRoomList;
	@Autowired
	private BidderStatus bidderStatus;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


	// 查找所有拍賣品並設成分頁
	@Override
	public Page<Item> findAllByPage(Pageable pageable) {

		return auctionRepository.findAll(pageable);
	}
	
	// [重新]上架拍賣品(新增、修改)
	@Transactional
	@Override
	public Item save(Item item) {
		// 原本 Timer 寫在這一直發生 NPE(nullPointerException) 空指針報錯的問題，
		// 處理了很久最後才發現問題在這邊 item 的 Id 根本還沒產生(要到 DB 自主產生)，
		// 因此存進 Map 的 Key 是 Null，所以一堆 Timer 的方法都發出 NPE，而 Map
		// 的 containsKey 為 True，因為此時存進 Map 的 Key 就是 Null，而用 keySet
		// 迭代出來的結果為 Null 也是因為這個原因。
		return auctionRepository.save(item);
	}
	
	// 用關鍵字以及狀態查找所有拍賣品並設成分頁
	@Override
	public Page<Item> searchItem(String search, int status, Pageable pageable) {
		if (status == 0) {
			return auctionRepository.searchAllItem(search, pageable);
		}else {
			return auctionRepository.searchItem(search, status, pageable);
		}
	}

	@Override
	public Item findOne(Long id) {
		return auctionRepository.findById(id).get();
	}
	
	@Transactional
	@Override
	public int bidding(int bidPrice, int bid, String bidder, Long id) {
		Item item = findOne(id);
		if (item.getAuctioneer().equals(bidder)) {
			return 0;
		}
		if (item.validBidPrice(bidPrice)) {
			return auctionRepository.bidding(bidPrice, bid, bidder, id);
		}else {
			return 0;
		}
	}
	
//	@Scheduled(fixedRate = 5000)
//	public void scheduledTest() {
//		System.out.println("Code is being executed..." + new Date());
//	}
	
	@Transactional
	@Override
	public int statusUpdate(int status, Long id) {
		return auctionRepository.statusUpdate(status, id);
	}

	@Transactional
	@Override
	public int reShelf(Long id) {
		// 重新上架的話先 save()，讓拍賣者重新確定資料後存入，再來改狀態。
		return auctionRepository.reShelf(id);
	}

	@Transactional
	@Override
	public int offShelf(Long id, int mid) {
		int aid = auctionRepository.findAid(id);
		if (aid == mid) {
			// 要拍賣者的帳號才能下架該流標品
			int offShelfAction = auctionRepository.offShelf(id);
			if (offShelfAction == 1) {
				this.bidTimerList.remove(id);
				return 1;
			}
			return 0;
		}
		return 0;
	}
	
	@Override
	public void countDownStart(Item item, Boolean reShelf) {
		BidTimer bidTimer1 = this.bidTimerList.get(item.getId());
		if (bidTimer1 != null) {
			bidTimer1.stop();
			this.bidTimerList.remove(item.getId());
		}
		BidTimer bidTimer = new BidTimer(item, reShelf, this);
		bidTimer.init();
//		bidTimer.countDown(System.out::println);
		bidTimer.countDown();
		System.out.println(">>>>>>>>>>>>>");
		System.out.println(bidTimer);
		System.out.println(">>>>>>>>>>>>>");
		bidTimerList.add(item.getId(), bidTimer);
		
//		System.out.println("------------------------------");
//		System.out.println("timer > Key : " + bidTimerList.checkKey(item.getId()));
//		System.out.println("------------------------------");
//		bidTimerList.getKeys();
//		System.out.println("------------------------------");
		
	}
	
	@Override
	public int timeLeft(Long id) {
//		this.timeLeft = bidTimerList.get(id).getTimeLeft();
//		return timeLeft;
		System.out.println("timeLeft > Key : " + bidTimerList.checkKey(id));
		return bidTimerList.get(id).getTimeLeft();
	}

	@Override
	public BidTimer findTimer(Long id) {
//		this.bidTimer = bidTimerList.get(id); 
//		return bidTimer;
		System.out.println("timer > Key : " + bidTimerList.checkKey(id));
		return bidTimerList.get(id);
	}
	
	// 拍賣品新上架、重新上架都給一個拍賣房間
	@Override
	public boolean createRoom(Item item) {
		String roomId = bidRoomList.create(item);
		return true;
	}
	
	@Override
	public boolean joinRoom(Member member, String roomId) {
		
//		if (this.bidderStatus.containsBidder(member.getMname()) && this.bidderStatus.isBidderInRoom(member.getMname())) {
//            return false;
//        }
        // 將使用者加入該房間
        BidRoom bidRoom = bidRoomList.getRoomById(roomId);
        if (bidRoom.addGuest(member.getMname())) {
            if (!this.bidderStatus.containsBidder(member.getMname())) {
                this.bidderStatus.initialize(member.getMname());
            }
            this.bidderStatus.setBidderInRoom(member.getMname(), true);
            this.bidderStatus.setBidderRoomId(member.getMname(), roomId);

            return true;
        }
        return false;
	}
	
	@Override
	public boolean leaveRoom(Member member, String roomId) {
		if ((!this.bidderStatus.containsBidder(member.getMname()))
				&& (!this.bidderStatus.isBidderInRoom(member.getMname()))) {
            return false;
        }
        BidRoom bidRoom = bidRoomList.getRoomById(roomId);
        bidRoom.removeGuest(member.getMname());
        if (this.bidderStatus.containsBidder(member.getMname())) {
            this.bidderStatus.initialize(member.getMname());
        }

        return true;
	}
	
	@Override
	public void broadcastRoomList() {
		RoomListResponse roomListResponse = new RoomListResponse();
		roomListResponse.setBidRooms(bidRoomList.getRooms());
//		System.out.println("Brodcast ~~~~~ " + roomListResponse.toString());
		// SimpMessagingTemplate：用於發送訊息的方法，可以設定目的代理，將資料傳送過去
		// simpMessagingTemplate.convertAndSend()：第一個參數是目的代理，第二個參數是資料
		this.simpMessagingTemplate.convertAndSend("/topic/room-list",roomListResponse);
		
	}
	
//	@Override
//	public void sendMessageToRoom(String roomId, String destination, Object message) {
//		
//	    // 取得指定房間
//	    BidRoom bidRoom = bidRoomList.getRoomById(roomId);
//
//	    // 取得房間內的所有成員
//	    ArrayList<String> roomMembers = bidRoom.getAllMembers();
//
//	    // 發送資訊給所有房間內的成員
//	    for (String member : roomMembers) {
//	        simpMessagingTemplate.convertAndSendToUser(member, destination, message);
//	    }
//	}
	
	@Override
	public void sendBidInfo(String itemId) {
		Long id = Long.parseLong(itemId);
		Item item = this.findOne(id);
	    BidRoom bidRoom = bidRoomList.getRoomById(itemId);
	    Map<String, Object> response = new HashMap<>();
	    response.put("itemInfo", item);

	    // 取得房間內的所有成員
	    ArrayList<String> roomMembers = bidRoom.getAllMembers();

	    // 發送資訊給所有房間內的成員
	    for (String member : roomMembers) {
	        simpMessagingTemplate.convertAndSendToUser(member, "/queue/room-info", response);
	    }
	    // 發送給該房間的所有人
//	    sendMessageToRoom(item.getId().toString(), "/queue/room-info", response);
	}
}
