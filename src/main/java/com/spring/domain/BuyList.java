package com.spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.List;
import java.util.Date;

@Entity
public class BuyList {
	
	@Id
	@GeneratedValue
	private Long oid;
	private int mid;
	private String mname;
	private List<Integer> prodList;
	private List<Integer> amountList;
	private List<Integer> priceList;
	private int totalPrice;
	@Temporal(TemporalType.DATE)
	private Date buyDate;
	public BuyList() {
	}
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public List<Integer> getProdList() {
		return prodList;
	}
	public void setProdList(List<Integer> prodList) {
		this.prodList = prodList;
	}
	public List<Integer> getAmountList() {
		return amountList;
	}
	public void setAmountList(List<Integer> amountList) {
		this.amountList = amountList;
	}
	public List<Integer> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<Integer> priceList) {
		this.priceList = priceList;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Date getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}
	
}
