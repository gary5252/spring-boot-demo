package com.spring.response;

import java.util.Set;
import com.spring.domain.BidRoom;

/**
 * 定義 WebSocket 回傳的資料格式
 * 轉成 json 之後會變成一個 array
 *
 */

public class RoomListResponse {

	private Set<BidRoom> bidRooms;

	public Set<BidRoom> getBidRooms() {
		return bidRooms;
	}

	public void setBidRooms(Set<BidRoom> bidRooms) {
		this.bidRooms = bidRooms;
	}
	
}
