package com.spring.domain;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 創建拍賣房間列表，設成實例以及相關方法
 * 上架物品同時加入此列表
 */

@Component
public class BidRoomList {
    private final Map<String, BidRoom> bidRoomList = new HashMap<>();

    public boolean add(String roomId, BidRoom bidRoom) {
        if (containRoomId(roomId)) {
            return false;
        }
        bidRoomList.put(roomId, bidRoom);
        return true;
    }
    
    public void removeBidRoom(String roomId, BidRoom bidRoom) {
    	if (containRoomId(roomId)) {
    		bidRoomList.remove(roomId, bidRoom);
    	}
    }

    public String create(Item item) {
        String roomId = item.getId().toString();
        BidRoom newRoom = new BidRoom(roomId, item);
        add(roomId, newRoom);
        return roomId;
    }

    public boolean containRoomId(String roomId) {
        return bidRoomList.containsKey(roomId);
    }

    public BidRoom getRoomById(String roomId) {
        if (containRoomId(roomId)) {
            return bidRoomList.get(roomId);
        }
        return null;
    }

    public HashSet<BidRoom> getRooms() {
        return new HashSet<>(bidRoomList.values());
    }

}
