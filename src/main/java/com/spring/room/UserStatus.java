package com.spring.room;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

class Status {
    private boolean inRoom = false;
    private String roomId = "";
    private boolean ready = false;
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
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}
    
}

@Component
public class UserStatus {
    Map<String, Status> userStatus = new HashMap<>();

    public void setUserInRoom(String username, boolean isInRoom) {
        this.userStatus.get(username).setInRoom(isInRoom);
    }

    public boolean isUserInRoom(String username) {
        return this.userStatus.get(username).isInRoom();
    }

    public void setUserRoomId(String username, String roomId) {
        this.userStatus.get(username).setRoomId(roomId);
    }

    public String getUserRoomId(String username) {
        return this.userStatus.get(username).getRoomId();
    }

    public void setUserReady(String username, boolean isReady) {
        this.userStatus.get(username).setReady(isReady);
    }

    public boolean isUserReady(String username) {
        return this.userStatus.get(username).isReady();
    }

    public boolean containsUser(String username) {
        return this.userStatus.containsKey(username);
    }

    public void initialize(String username) {
        this.userStatus.put(username, new Status());
    }
}
