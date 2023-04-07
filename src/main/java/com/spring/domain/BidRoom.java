package com.spring.domain;

/**
 * 創建此類方便 WebSocket 對各拍賣品資訊進行播送，
 * 播送對象為此房內的競標者以及拍賣家。
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BidRoom {

    private final String auctioneer;	// owner
    private final ArrayList<String> bidders;	// guests
    private final String roomId;	// use item.getId() remember transform Long into String
    private final Item item;
    
    public BidRoom(String roomId,Item item) {
        this.roomId = roomId;
        this.auctioneer = item.getAuctioneer();
        this.bidders = new ArrayList<>();
        this.item = item;
    }

	public String getAuctioneer() {
		return auctioneer;
	}

	public ArrayList<String> getBidders() {
		return bidders;
	}

	public String getRoomId() {
		return roomId;
	}

	public Item getItem() {
		return item;
	}
    
	// 競標者加入房間
    public boolean addGuest(String guestName) {
        this.bidders.add(guestName);
        return true;
    }

    // 競標者離開房間
    public void removeGuest(String guestName) {
        this.bidders.remove(guestName);
    }

    // 計算此時有多少人在競標
    public int count() {
        return 1 + this.bidders.size();
    }
    
    public ArrayList<String> getAllMembers() {
        ArrayList<String> allPeople = new ArrayList<>();
        allPeople.add(this.auctioneer);
        allPeople.addAll(this.bidders);
        return allPeople;
    }

    public Map<String, Object> getInfo() {
        Map<String , Object> info = new HashMap<>();
        info.put("roomId", this.roomId);
        info.put("owner", this.auctioneer);
        info.put("guests", this.bidders);
//        info.put("item", this.item);
        info.put("number", this.count());
        return info;
    }
}
