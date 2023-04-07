package com.spring.form;

/**
 * 這個類別是用來接收前端表單資料用的，
 * 因為表單通常比較多會是複合表格資料混雜的欄位，
 * 又或是會有一些用來驗證但不用寫進資料庫的欄位資料(像是註冊時會有的密碼再確認欄位，
 * 要跟真正寫進資料庫的密碼欄位資料比對是否相同，但不會寫進資料庫畢竟不需要儲存兩個相同的欄位資料)，
 * 當這個類別接收完資料後就可以透過其他方法(用各自實體類別的Setter，或是BeanUtils.copyProperties，...等等)
 * 將要寫進資料庫的資料分配給實體類別，再將實體類別存入(save())資料庫。
 *
 * 但使用 @Transient 註解讓實體類別屬性不被納入實體的方式會更輕鬆。
 */

// org.hibernate.validator.constraints.* 需要導入依賴(pom.xml)才能找到

import com.spring.domain.Member;
import org.springframework.beans.BeanUtils;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class RegisterForm {

	// re正則表達式 > / 內容 / , /^ 開頭 , $/ 結尾
	public static final String PHONE_REG = "\\d{10}";
	public static final String EMAIL_REG = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";

	private int mid;
	@NotBlank
	private String mname;
	@Length(min = 4, message = "密碼長度最少四位")
	private String password;
	@NotBlank
	private String confirmPassword;
	@Pattern(regexp = PHONE_REG, message = "請輸入正確的手機號碼")
	private String phone;
	@Email
	private String email;
	private String maddress;

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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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

	// 為了在Controller使用方法時每次都new物件，將轉換方法轉移過來
	public Member convertToMember() {
		Member member = new MemberFormConvert().convert(this);
		// 只寫下面兩行一樣能行而且很快速，但之後若專案比較大型有許多類似功能需要維護、程式重構，
		// 使用統一介面規範作抽象化是一個標準化的方式
		// Member member = new Member();
		// BeanUtils.copyProperties(this, member);
		return member;
	}

	// 檢查密碼與確認密碼是否一致
	public Boolean confirmPasswordCheck() {
		if (this.password.equals(this.confirmPassword)) {
			return true;
		}
		return false;
	}

	/**
	 * 另外設置一個類別實作介面是為了有個統一的規範實現抽象化
	 * 將方法封裝出來讓Controller功能需要時呼叫
	 * BeanUtils.copyProperties(要複製資料內容的類別, 資料目標類別)
	 * > 為避免屬性過多要寫一堆Setter還要篩選，用這個功能可以將符合的屬性欄位資料複製過去
	 */

	private class MemberFormConvert implements FormConvert<RegisterForm, Member> {

		@Override
		public Member convert(RegisterForm registerForm) {
			Member member = new Member();
			BeanUtils.copyProperties(registerForm, member);
			return member;
		}
	}

}
