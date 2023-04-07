package com.spring.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.HashMap;
import java.util.Map;
import com.spring.domain.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class RoomController {
//    @Autowired
//    private LoginService loginService;

    @Autowired
    private UserStatus userStatus;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private RoomList roomList;

    @GetMapping("/rooms")
    public String viewAllRoomsPage(Model model, HttpSession session) {
    	
    	Member member = (Member) session.getAttribute("member");
        String username = member.getMname();
        // 判斷是否已經在房間，是的話重新導向到對應的房間，解決 Day 17 提到的其中一種狀況，`/room` 還沒設定對應分配，所以目前會 404
        if (this.userStatus.containsUser(username) && this.userStatus.isUserInRoom(username)) {
            return "redirect:/room/" + this.userStatus.getUserRoomId(username);
        }

        // 否則就初始化他的狀態
        this.userStatus.initialize(username);
            
        return "rooms";
    }
    
    @PostMapping("/api/room/join")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> joinRoomProcess(UserJoinRoomMessage userJoinRoomMessage,HttpSession session) {
    	Member member = (Member) session.getAttribute("member");
    	Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus;
        String message;

        // 檢驗是否登入
        if (member == null) {
            httpStatus = HttpStatus.FORBIDDEN;
            message = "未登入";
            response.put("message", message);
            return ResponseEntity.status(httpStatus).body(response);
        }

        String username = member.getMname();
        String action = userJoinRoomMessage.getAction();
        String roomId = userJoinRoomMessage.getRoomId();
        if (action.equals("create") && roomService.createRoom(username)) {
            httpStatus = HttpStatus.OK;
            message = "建立成功";
        }
        else if (action.equals("join") && roomService.joinInRoom(username, roomId)) {
            httpStatus = HttpStatus.OK;
            message = "加入成功";
        }
        else {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Error";
        }

        if (httpStatus == HttpStatus.OK) {
            this.roomService.broadcastRoomList();
            roomId = this.userStatus.getUserRoomId(username);
        }
        else {
            roomId = "";
        }
        response.put("message", message);
        response.put("roomId", roomId);
        return ResponseEntity.status(httpStatus).body(response);
    }
    
    @GetMapping("/room/{roomId}")
    public String viewRoomPage(@PathVariable("roomId") String roomId, Model model, HttpSession session) {
    	Member member = (Member) session.getAttribute("member");
        // 如果房號不存在
        if (!this.roomList.containRoomId(roomId)) {
            return "redirect:/rooms";
        }

        // 使用者必須先登入
        if (member == null) {
            String username = member.getMname();
            model.addAttribute("username", username);
            // 如果使用者有狀態，而且儲存的房號跟網址相同
            if (this.userStatus.containsUser(username) && this.userStatus.getUserRoomId(username).equals(roomId)) {
                return "room";
            }

            // 否則，讓使用者加入該房間
            if (roomService.joinInRoom(username, roomId)) {
                return "room";
            }
        }

        // 沒有登入的話，就不給進
        return "redirect:/rooms";
    }
}
