package com.spring.room;

public class UserJoinRoomMessage {
	
    private String action;
    private String roomId;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
    

}
