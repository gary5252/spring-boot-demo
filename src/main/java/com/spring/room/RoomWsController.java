package com.spring.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.spring.domain.BidRoomList;

//@Controller
public class RoomWsController {
//    @Autowired
//    private RoomList roomList;
//
//    @Autowired
//    private UserStatus userStatus;
//    
//    @Autowired
//    private BidRoomList bidRoomList;
//
//    @MessageMapping("/room-list")
//    @SendTo("/topic/room-list")
//    public RoomListResponse getAllRooms() {
//        RoomListResponse roomListResponse = new RoomListResponse();
//        roomListResponse.setRooms(roomList.getRooms());
//        roomListResponse.setBidRooms(bidRoomList.getRooms());
//        return roomListResponse;
//    }
}