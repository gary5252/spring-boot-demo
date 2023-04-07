package com.spring.domain;

//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//@Component
//@ConfigurationProperties(prefix = "product")
@Entity
@Table(name="PRODUCT")
public class Product {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="PID")
	private int pid;
	
	@Column(name="NAME")
	@NotBlank
	private String name;
	
	@Column(name="PRICE")
	@Min(0)
	private int price;
	
	@Column(name="AMOUNT")
	@Min(0)
	private int amount;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="CID")
	@NotNull
	private int cid;
	
	public Product() {
		super();
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public String amountCheck() {
		if(this.amount <= 100) {
			return "out";
		}else if(this.amount >= 2000) {
			return "over";
		}else {
			return "normal";
		}
	}
}
