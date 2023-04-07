package com.spring.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomList roomList;

    @Autowired
    private UserStatus userStatus;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public boolean createRoom(String username) {
        // 檢查使用者已經有狀態，並且目前不在任何房間內
        if (this.userStatus.containsUser(username) && this.userStatus.isUserInRoom(username)) {
            return false;
        }

        // 建立房間
        String roomId = roomList.create(username);

        // 修改使用者目前的狀態
        this.userStatus.setUserInRoom(username, true);
        this.userStatus.setUserRoomId(username, roomId);
        return true;
    }

    public boolean joinInRoom(String username, String roomId) {
        // 檢查使用者已經有狀態，並且目前不在任何房間內
        if (this.userStatus.containsUser(username) && this.userStatus.isUserInRoom(username)) {
            return false;
        }

        // 檢查要加入的房間人數小於 4
        if (roomList.getRoomById(roomId).count() == 4) {
            return false;
        }

        // 將使用者加入該房間
        Room room = roomList.getRoomById(roomId);
        if (room.addGuest(username)) {
            if (!this.userStatus.containsUser(username)) {
                this.userStatus.initialize(username);
            }
            this.userStatus.setUserInRoom(username, true);
            this.userStatus.setUserRoomId(username, roomId);
            return true;
        }
        return false;
    }
    
    public void broadcastRoomList() {
        RoomListResponse roomListResponse = new RoomListResponse();
        roomListResponse.setRooms(roomList.getRooms());
        this.simpMessagingTemplate.convertAndSend("/topic/room-list", roomListResponse);
    }
}
