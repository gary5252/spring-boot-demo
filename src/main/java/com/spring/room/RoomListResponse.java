package com.spring.room;

import java.util.Set;
import com.spring.domain.BidRoom;

public class RoomListResponse {
	
    private Set<Room> rooms;
    private Set<BidRoom> bidRooms;

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	public Set<BidRoom> getBidRooms() {
		return bidRooms;
	}

	public void setBidRooms(Set<BidRoom> bidRooms) {
		this.bidRooms = bidRooms;
	}

	
}
