package com.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.service.AuctionService;
import com.spring.service.AuctionServiceImpl;
import com.spring.domain.Item;
import com.spring.domain.Member;
import com.spring.domain.Product;
import com.spring.domain.BidderStatus;
import com.spring.exception.ProductNotFoundException;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import jakarta.servlet.http.*;
//import java.util.Timer;
//import java.util.TimerTask;
//import com.spring.Timer.BidTimer;
//import com.spring.Timer.BidTimerList;

@Controller
public class AuctionController {

	@Autowired
	private AuctionService auctionService;
	
	@Autowired
	private BidderStatus bidderStatus;
	
	// 移到 Service 層
//	@Autowired
//	private BidTimerList bidTimerList;
	
	@GetMapping("/auctionRoomList")
	public String viewAllRoom(HttpSession session, Model model) {
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
			model.addAttribute("login", true);
			model.addAttribute("member", member);
            if (this.bidderStatus.containsBidder(member.getMname()) && this.bidderStatus.isBidderInRoom(member.getMname())) {
                return "redirect:/auction/" + this.bidderStatus.getBidderRoomId(member.getMname()) + "/bid";
            }

            // 否則就初始化他的狀態
            this.bidderStatus.initialize(member.getMname());
		}else {
			model.addAttribute("member", null);
			model.addAttribute("login", false);
		}
		return "auctionroomlist";
	}
	
	// 拍賣列表(分頁)
	@GetMapping("/auctionlist")
	public String list(@PageableDefault(size = 10,sort = {"shelfDate","id"},direction = Sort.Direction.DESC) Pageable pageable,
					   HttpSession session,
					   @RequestParam(defaultValue="0") int statusSelect,
					   @RequestParam(defaultValue="") String search,
//					   HttpServletRequest request,
			  		   Model model) {
//		session = request.getSession();  // 測試過了不用request也能成功取值，可以成功抓到該資料類別所有屬性
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
			// 為了 thymeleaf 加的屬性，以防未登入造訪列表時抓不到member資料進而報錯
			model.addAttribute("login", true);
			model.addAttribute("member", member);
		}else {
			model.addAttribute("member", null);
			model.addAttribute("login", false);
		}
		
		model.addAttribute("statusSelect", statusSelect);
		model.addAttribute("search", search);
		if (statusSelect == 0) {
			Page<Item> page = auctionService.findAllByPage(pageable);
			model.addAttribute("page", page);
		}else {
			Page<Item> page1 = auctionService.searchItem(search, statusSelect, pageable); 
			model.addAttribute("page", page1);
		}
		
		return "auctionlist";
	}
	
	// ===============================================
	// 上架拍賣品 
	@GetMapping("/auction/onshelf")
	public String onShelfPage(Model model, HttpSession session) {
		model.addAttribute("item", new Item());
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("member", member);
		return "onshelf";
	}
	
//    @PostMapping("/api/room/join")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> joinRoomProcess(Item item, HttpSession session) {
//    	Member member = (Member) session.getAttribute("member");
//    	Map<String, Object> response = new HashMap<>();
//        HttpStatus httpStatus;
//        String message;
//
//        // 檢驗是否登入
//        if (member == null) {
//            httpStatus = HttpStatus.FORBIDDEN;
//            message = "未登入";
//            response.put("message", message);
//            return ResponseEntity.status(httpStatus).body(response);
//        }
//
//        String username = member.getMname();
//        String roomId = item.getId().toString();
//        if (auctionService.createRoom(item)) {
//            httpStatus = HttpStatus.OK;
//            message = "建立成功";
//        }
//        else if (auctionService.joinRoom(member, roomId)) {
//            httpStatus = HttpStatus.OK;
//            message = "加入成功";
//        }
//        else {
//            httpStatus = HttpStatus.BAD_REQUEST;
//            message = "Error";
//        }
//
//        if (httpStatus == HttpStatus.OK) {
//            this.auctionService.broadcastRoomList();
//            roomId = this.bidderStatus.getBidderRoomId(username);
//        }
//        else {
//            roomId = "";
//        }
//        response.put("message", message);
//        response.put("roomId", roomId);
//        return ResponseEntity.status(httpStatus).body(response);
//    }
	
	@PostMapping("/auction/onshelf")
	public String onShelf(@Valid Item item,
			   			  BindingResult result,
			   			  Model model,
			   			  final RedirectAttributes attributes) {
	
		if (item.getName() == null) {
			model.addAttribute("error", "名稱不能為空!");
			return "onshelf";
		}
		if (item.getBasicPrice() < 0) {
			model.addAttribute("error", "起標價不能小於零!");
			return "onshelf";
		}
//		if (result.hasErrors()) {
//			 List<FieldError> fieldErrors = result.getFieldErrors();
//			 for (FieldError error : fieldErrors) {
//			 System.out.println(error.getField() + " > " + error.getDefaultMessage() + " > " + error.getCode());
//			 }
//			return "onshelf";
//		}
		Item item1 = auctionService.save(item);
		if (item1 != null) {
			auctionService.createRoom(item1);
			auctionService.countDownStart(item1, false);
			auctionService.broadcastRoomList();
			attributes.addFlashAttribute("message", ">>> 拍賣品 : " + item1.getName() + " , 上架成功 <<<");
		}
		
		return "redirect:/auctionlist";
	}
	
	// ======================================================
	// 競標
	@GetMapping("/auction/{id}/bid")
	public String bidPage(@PathVariable Long id, Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		Item item = auctionService.findOne(id);
//		if (member.getMid() == item.getAid()) {
//			return "redirect:/auctionlist";
//		}
		int timeLeft = auctionService.timeLeft(id);
//		BidTimer bidTimer = auctionService.findTimer(id); // nullPointerException **已解決
		model.addAttribute("item", item);
		model.addAttribute("member", member);
		model.addAttribute("timeLeft", timeLeft);
		auctionService.joinRoom(member, id.toString());
		 // 如果使用者有狀態，而且儲存的房號跟網址相同
//        if (this.bidderStatus.containsBidder(member.getMname()) && this.bidderStatus.getBidderRoomId(member.getMname()).equals(id)) {
//            return "bid";
//        }

        // 否則，讓使用者加入該房間
//        if (auctionService.joinRoom(member, id.toString())) {
            return "bid";
//        }
//		return "redirect:/auctionRoomList";
	}
	
	@PostMapping("/auction/bid")
	public String bid(@RequestParam("myBidPrice") int myBidPrice,
					  @RequestParam("itemId") Long itemId, 
					  HttpSession session,
					  final RedirectAttributes attributes) {
		Member member = (Member) session.getAttribute("member");
		int bidAction = auctionService.bidding(myBidPrice, member.getMid(), member.getMname(), itemId);
		if (bidAction != 0) {
			Item item = auctionService.findOne(itemId);
			auctionService.countDownStart(item, true);
			attributes.addFlashAttribute("message", "競標出價成功!");
//			auctionService.sendBidInfo(item);
			return "redirect:/auction/" + itemId + "/bid";
		}else {
			attributes.addFlashAttribute("message", "競標出價失敗!");
			return "redirect:/auction/" + itemId + "/bid"; 
		}
		
	}
	
	@PostMapping("/api/room/leave")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> quitRoomProcess(HttpSession session, @RequestParam("roomId") String roomId) {
		Member member = (Member) session.getAttribute("member");
	    Map<String, Object> response = new HashMap<>();
	    HttpStatus httpStatus;
	    String message;

	    if (member == null) {
	        httpStatus = HttpStatus.FORBIDDEN;
	        message = "未登入";
	        response.put("message", message);
	        return ResponseEntity.status(httpStatus).body(response);
	    }

	    // 將使用者移除
	    if (auctionService.leaveRoom(member, roomId)) {
	        httpStatus = HttpStatus.OK;
	        message = "成功";
	    }
	    else {
	        httpStatus = HttpStatus.BAD_REQUEST;
	        message = "Error";
	    }
	    response.put("message", message);
	    return ResponseEntity.status(httpStatus).body(response);
	}
	
	// ======================================================
	// 重新上架流標品(更新拍賣狀態並重製時限 Timer)
	@GetMapping("/auction/{id}/reshelf")
	public String reShelfPage(@PathVariable Long id, 
							  Model model,
							  final RedirectAttributes attributes,
							  HttpSession session) {
		Item item = auctionService.findOne(id);
		if (item == null) {
			attributes.addFlashAttribute("message", "無此項拍賣品!");
			return "redirect:/auctionlist";
		}
		Member member = (Member) session.getAttribute("member");
		if (member.getMid() != item.getAid()) {
			attributes.addFlashAttribute("message", "只有原拍賣者可以重新上架此項拍賣品!");
			return "redirect:/auctionlist";
		}
		model.addAttribute("item", item);
		model.addAttribute("member", member);
		// 這邊可以共用寫入的模板
		return "onshelf";
	}
	
	// ======================================================
	// 下架已流標拍賣品(刪除)
	@GetMapping("/auction/{id}/offshelf")
	public String offShelf(@PathVariable Long id, final RedirectAttributes attributes,HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		int action = auctionService.offShelf(id, member.getMid());
		if (action == 1) {
			attributes.addFlashAttribute("message", "該流標品已下架成功!");
		}else {
			attributes.addFlashAttribute("message", "非原拍賣者或該拍賣品尚未流標!");
		}
		return "redirect:/auctionlist";
	}
	
}
