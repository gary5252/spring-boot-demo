package com.spring.domain;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

class Status {
    private boolean inRoom = false;
    private String roomId = "";
	public boolean isInRoom() {
		return inRoom;
	}
	public void setInRoom(boolean inRoom) {
		this.inRoom = inRoom;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
    
}

@Component
public class BidderStatus {

    Map<String, Status> bidderStatus = new HashMap<>();

    public void setBidderInRoom(String bidder, boolean isInRoom) {
        this.bidderStatus.get(bidder).setInRoom(isInRoom);
    }

    public boolean isBidderInRoom(String bidder) {
        return this.bidderStatus.get(bidder).isInRoom();
    }

    public void setBidderRoomId(String bidder, String roomId) {
        this.bidderStatus.get(bidder).setRoomId(roomId);
    }

    public String getBidderRoomId(String bidder) {
        return this.bidderStatus.get(bidder).getRoomId();
    }

    public boolean containsBidder(String bidder) {
        return this.bidderStatus.containsKey(bidder);
    }

    public void initialize(String bidder) {
        this.bidderStatus.put(bidder, new Status());
    }
}
