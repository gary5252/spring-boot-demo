package com.spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
//import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Item {

	@Id
	@GeneratedValue
	private Long id;	// 拍賣品主鍵
	@NotBlank
	private String name;	// 拍賣品名稱
	private int aid;	// 拍賣者關聯會員主鍵
	private String auctioneer;	// 拍賣者名稱
	@Min(0)
	private int basicPrice;	// 起標價
	private String description;	// 拍賣品敘述說明
	private int bid;	// 競標者關聯會員主鍵
	private String bidder;	// 競標者名稱
	private int bidPrice;	// 競標者出價
	private int status;	// 1:競標中 2: 已結標 3: 已流標
//	private Date shelfDate;		// thymeleaf 轉換有問題不知道要怎處理就先換個格式
	private String shelfDate;	// 拍賣品上架日期
	public Item() {
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuctioneer() {
		return auctioneer;
	}
	public void setAuctioneer(String auctioneer) {
		this.auctioneer = auctioneer;
	}
	public int getBasicPrice() {
		return basicPrice;
	}
	public void setBasicPrice(int basicPrice) {
		this.basicPrice = basicPrice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBidder() {
		return bidder;
	}
	public void setBidder(String bidder) {
		this.bidder = bidder;
	}
	public int getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(int bidPrice) {
		this.bidPrice = bidPrice;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getShelfDate() {
		return shelfDate;
	}
	public void setShelfDate(String shelfDate) {
		this.shelfDate = shelfDate;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	
	public Boolean validBidPrice(int price) {
		float currentPrice;
		if (this.bidPrice == 0) {
			currentPrice = (float) this.basicPrice;
		}else {
			currentPrice = (float) this.bidPrice;
		}
		// 競價需大於等於前一有效出價的 5% (四捨五入至整數位)
		int validBidPrice = Math.round(currentPrice * 1.05f);
		if (price >= validBidPrice) {
			return true;
		}
		 
		return false;
	}
	
}
