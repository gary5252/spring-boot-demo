package com.spring.service;

import com.spring.domain.Member;
import com.spring.domain.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
	
	@Autowired
	private LoginRepository loginRepository; 
	
	// 查詢所有member，給admin看資料用
	public List<Member> findAll(){
		return loginRepository.findAll();
	}
	
	// 查找單一member資料，登入驗證用
	// 下面有建另一個用帳號密碼找資料的，用那個方法比較好
	public Optional<Member> findOne(int mid) {
		return loginRepository.findById(mid);
	}
	
	// Create/Update，會員註冊用
	@Transactional
	public Member save(Member member) {
		return loginRepository.save(member);
	}

	public String findUsername(String mname) {
		return loginRepository.findUsername(mname);
	}
	
	// 透過帳號和密碼查找會員資料
	public Member findByMnameAndPassword(String mname,String password) {
		return loginRepository.findByMnameAndPassword(mname, password);
	}
	
	// 驗證使用者名稱有無重複
	public String checkUniqueMname(int mid,String mname) {
		return loginRepository.checkUniqueMname(mid, mname);
	}
	
	// 驗證使用者密碼有無重複
	public String checkUniquePassword(int mid,String password) {
		return loginRepository.checkUniquePassword(mid, password);
	}
	
	// 驗證使用者電話有無重複
	public String checkUniquePhone(int mid,String phone) {
		return loginRepository.checkUniquePhone(mid, phone);
	}
	
	// 驗證使用者信箱有無重複
	public String checkUniqueEmail(int mid,String email) {
		return loginRepository.checkUniqueEmail(mid, email);
	}
	
	// 檢查註冊時是否已有該名稱
	public String checkRegisterMname(String mname) {
		return loginRepository.checkRegisterMname(mname);
	}
	
	// 檢查註冊時是否已有該密碼
	public String checkRegisterPassword(String password) {
		return loginRepository.checkRegisterPassword(password);
	}
	
	// 檢查註冊時是否已有該電話
	public String checkRegisterPhone(String phone) {
		return loginRepository.checkRegisterPhone(phone);
	}
	
	// 檢查註冊時是否已有該信箱
	public String checkRegisterEmail(String email) {
		return loginRepository.checkRegisterEmail(email);
	}
	
	// 修改密碼
	public int updatePwd(int mid, String password) {
		return loginRepository.updatePwd(password, mid);
	}
	
	
}
