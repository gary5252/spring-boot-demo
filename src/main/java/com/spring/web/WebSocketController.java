package com.spring.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.spring.domain.BidRoomList;
import com.spring.domain.BidderStatus;
import com.spring.response.RoomListResponse;
import com.spring.service.AuctionService;
import com.spring.domain.Item;


@Controller
public class WebSocketController {

    @Autowired
    private BidRoomList bidRoomList;
    
    @Autowired
    private BidderStatus bidderStatus;
    
    @Autowired
    private AuctionService auctionService;
    
    /**
     * @MessageMapping("/room-list")：定義接收資料的 url
	 * @SendTo("/topic/room-list")：定義 Server 將傳遞訊息至哪個代理
     * 
     */
    @MessageMapping("/room-list")
    @SendTo("/topic/room-list")
    public RoomListResponse getAllRooms() {
        RoomListResponse roomListResponse = new RoomListResponse();
        roomListResponse.setBidRooms(bidRoomList.getRooms());
        return roomListResponse;
    }
    
    @MessageMapping("/room-info")
    public void getRoomInfo(Principal principal) {
        String username = principal.getName();
        String roomId = this.bidderStatus.getBidderRoomId(username);
        this.auctionService.sendBidInfo(roomId);
//    	this.auctionService.sendBidInfo(item);
    }
    
    
}
