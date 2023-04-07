package com.spring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="MEMBER")
public class Member {
	public static final String PHONE_REG = "\\d{10}";

	@Id
	@Column(name="MID")
	private int mid;
	
	@NotBlank
	@Column(name="MNAME")
	private String mname;
	
	@Length(min = 4, message = "密碼長度最少四位")
	@Column(name="PASSWORD")
	private String password;
	
	@Pattern(regexp = PHONE_REG, message = "請輸入正確的手機號碼")
	@Column(name="PHONE")
	private String phone;
	
	@Email
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="MADDRESS")
	private String maddress;

	public Member() {
		super();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMaddress() {
		return maddress;
	}

	public void setMaddress(String maddress) {
		this.maddress = maddress;
	}
	
	public Boolean isAdmin() {
		if (this.mid == 1) {
			return true;
		}else {
			return false;
		}
	}

//	@Override
//	public String toString() {
//		return "Member [mid=" + mid + ", mname=" + mname + ", password=" + password + ", phone=" + phone + ", email="
//				+ email + ", maddress=" + maddress + "]";
//	}
}
