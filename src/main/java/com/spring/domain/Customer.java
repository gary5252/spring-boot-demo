package com.spring.domain;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="CUSTOMER")
public class Customer {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="CID")
	private int cid;
	
	@Column(name="CNAME")
	private String cname;
	
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="TEL")
	private String tel;

	public Customer() {
		super();
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
