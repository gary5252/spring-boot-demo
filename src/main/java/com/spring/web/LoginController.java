package com.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import com.spring.service.LoginService;
import jakarta.validation.Valid;

// 下面這個包含了 HttpServletRequest/Response,Cookie,HttpSession
import jakarta.servlet.http.*;
//import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;

import com.spring.domain.Member;
import com.spring.exception.ProductNotFoundException;
import com.spring.form.RegisterForm;
//import com.spring.domain.Product;

import java.io.IOException;
import java.util.*;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	// 會員登入頁面
	@GetMapping("/login")
	public String login(HttpServletRequest request,
			HttpSession session,
			HttpServletResponse response) throws IOException {
		Cookie[] cks = request.getCookies();
		if (cks != null) {
			boolean flagCookie = false; // true表示有登入過了
			for (int i = 0; i < cks.length; i++) {
				if (cks[i].getName().equals("ACCOUNT")) {
					flagCookie = true;
					// session.setAttribute("ID", cks[i].getValue());
					// System.out.println("---- Cookies : " + cks[i].getValue());
					Member member = loginService.findOne(Integer.parseInt(cks[i].getValue())).get();
					// System.out.println("---- Member : " + member);
					// 這邊要再設一次session
					session.setAttribute("member", member);
					break;
				}
			}
			if (flagCookie) {
				// 已經有登入紀錄，直接導向至首頁
				// response.sendRedirect("products");
				// Member member = (Member) session.getAttribute("member");
				// model.addAttribute("member", member);
				return "redirect:/products";
			}
		}
		return "login";
	}

	// 登入資料表單驗證
	@PostMapping("/login")
	public String verify(@RequestParam String mname,
			@RequestParam String password,
			RedirectAttributes attributes,
			@RequestParam(defaultValue = "No") String keep,
			// HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String username = loginService.findUsername(mname);
			if (username == null) {
				attributes.addFlashAttribute("error", "帳號不存在");
				return "redirect:/login";
			} else {
				Member member = loginService.findByMnameAndPassword(mname, password);
				// System.out.println("------- Member : " + member);
				// System.out.println("------- Mname : " + username);
				// System.out.println("------- pwd : " + member.getPassword());
				if (member == null) {
					attributes.addFlashAttribute("error", "密碼錯誤");
					return "redirect:/login";
				}
				// attributes.addFlashAttribute("member", member);
				if (keep.equals("Yes")) {
					// 登入時勾選記住帳號的選項，透過COOKIE紀錄登入者資訊，讓使用者可以跳過登入頁面
					Cookie ck = new Cookie("ACCOUNT", String.valueOf(member.getMid()));
					ck.setMaxAge(99999);
					response.addCookie(ck);
				}
				// 寫入 session，登出時將前一個 session 失效，這邊要重新抓一個，
				// 雖說瀏覽器發現當前的 session 失效後會立馬創建一個新的，但不確定
				// 程式有沒有馬上跟著抓到，所以這邊抓一次新的比較保險。
				HttpSession session = request.getSession(true);
				session.setAttribute("member", member);
				// 成功登入
				return "redirect:/products";
			}
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "資訊錯誤");
			return "redirect:/login";
		}
	}

	// 登出功能，釋放Cookie/Session，返回登入頁面
	@GetMapping("/logout")
	public String logout(HttpServletResponse response, HttpSession session) {
		Cookie ck = new Cookie("ACCOUNT", null);
		ck.setMaxAge(0);
		response.addCookie(ck);
		// 登出時直接將舊有 session 失效，比起一個一個參數解除綁定更安全且方便
		// session.removeAttribute("member");
		session.invalidate(); // 解除 session 綁定
		// SessionStatus.setComplete() 是清除註解 @SessionAttribute() 的內容用的
		return "redirect:/login";
	}

	// ===============================================================

	// 會員註冊頁面
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "register";
	}

	// 會員資料新增(註冊)提交表單，寫入資料庫，
	@PostMapping("/register")
	public String register(RedirectAttributes attributes,
						   Model model,
						   @Valid RegisterForm registerForm,
						   BindingResult result) {
		// 表單資料驗證 : 規則在資料接收類別設置(registerForm)，功能這邊設置驗證註解 @Valid
		// 並透過BindingResult 接收驗證結果
		// 複合資料表或是契合實體類別表單方式 :
		// 詳見com.spring.form.RegisterForm
		String checkMname = loginService.checkRegisterMname(registerForm.getMname());
		String checkPassword = loginService.checkRegisterPassword(registerForm.getPassword());
		String checkPhone = loginService.checkRegisterPhone(registerForm.getPhone());
		String checkEmail = loginService.checkRegisterEmail(registerForm.getEmail());
		if (!checkMname.equals("0")) {
			result.rejectValue("mname", "uniqueError", "此名稱已存在");
		}
		if (!checkPassword.equals("0")) {
			result.rejectValue("password", "uniqueError", "此密碼已被使用");
		}
		if (!checkPhone.equals("0")) {
			result.rejectValue("phone", "uniqueError", "此號碼已被使用");
		}
		if (!checkEmail.equals("0")) {
			result.rejectValue("email", "uniqueError", "此信箱已被註冊");
		}
		if (!registerForm.confirmPasswordCheck()) {
			// (Field , ErrorCode, DefaultMessage)
			result.rejectValue("confirmPassword", "cofirmError", "確認密碼不一致");
		}
		if (result.hasErrors()) {
			// List<FieldError> fieldErrors = result.getFieldErrors();
			// for (FieldError error : fieldErrors) {
			// System.out.println(error.getField() + " > " + error.getDefaultMessage() + " > " + error.getCode());
			// }
			return "register";
		}

		Member member = registerForm.convertToMember();
		Member member1 = loginService.save(member);
		if (member1 != null) {
			attributes.addFlashAttribute("message", "Member : " + member1.getMname());
		}
		return "redirect:/login";
	}

	// ===============================================================

	// 會員資料修改頁面
	@GetMapping("/member")
	public String memberPage(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		// Member member = loginService.findOne(mid).get();
		// 下面為測試拋出例外變更狀態種類，結果無法成功變換，同商品單一查詢結果
		if (member == null) {
			return "redirect:/login";
		}
		model.addAttribute("member", member);
		return "member";
	}

	// 會員資料修改表單提交，寫入資料庫
	@PostMapping("/member")
	public String memberUpdate(RedirectAttributes attributes,
							   HttpSession session,
							   @Valid Member member,
							   BindingResult result) {
		// 傻眼 @Valid 註釋的參數後面一定要接著 BindingResult 不然根本接不到驗證訊息進而報錯
		String checkMname = loginService.checkUniqueMname(member.getMid(), member.getMname());
		String checkPhone = loginService.checkUniquePhone(member.getMid(), member.getPhone());
		String checkEmail = loginService.checkUniqueEmail(member.getMid(), member.getEmail());
		if (!checkMname.equals("0")) {
			result.rejectValue("mname", "uniqueError", "此名稱已存在");
		}
		if (!checkPhone.equals("0")) {
			result.rejectValue("phone", "uniqueError", "此號碼已被使用");
		}
		if (!checkEmail.equals("0")) {
			result.rejectValue("email", "uniqueError", "此信箱已被註冊");
		}
		if (result.hasErrors()) {
			List<FieldError> fieldErrors = result.getFieldErrors();
			for (FieldError error : fieldErrors) {
				System.out.println(error.getField() + " > " + error.getDefaultMessage() + " > " + error.getCode());
			}
			return "member";
		}
		Member member1 = loginService.save(member);
		if (member1 != null) {
			attributes.addFlashAttribute("message", "Member : " + member1.getMname());
			session.setAttribute("member", member1);
		}
		return "redirect:/products";
	}
	
	// 會員密碼修改頁面
	@GetMapping("/password")
	public String passwordUpdatePage(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		// Member member = loginService.findOne(mid).get();
		// 下面為測試拋出例外變更狀態種類，結果無法成功變換，同商品單一查詢結果
		if (member == null) {
			return "redirect:/login";
		}
		model.addAttribute("member", member);
		return "password";
	}
	
	// 會員密碼修改提交表單，寫入資料庫，
	@PostMapping("/password")
	public String passwordUpdate(RedirectAttributes attributes,
								 @RequestParam("confirmPassword") String confirmPassword,
								 @RequestParam("oldPassword") String oldPassword,
								 @RequestParam("newPassword") String newPassword,
			   					 HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		String checkPassword = loginService.checkUniquePassword(member.getMid(), newPassword);
		if (!oldPassword.equals(member.getPassword())) {
			attributes.addFlashAttribute("error", "舊密碼輸入錯誤");
			return "redirect:/password";
		}
		if (newPassword.length() < 4) {
			attributes.addFlashAttribute("error", "密碼長度不得小於 4 位");
			return "redirect:/password";
		}
		if (!checkPassword.equals("0")) {
			attributes.addFlashAttribute("error", "此密碼已被使用");
			return "redirect:/password";
		}
		if (oldPassword.equals(newPassword)) {
			attributes.addFlashAttribute("error", "新密碼與舊密碼相同");
			return "redirect:/password";
		}
		if (!confirmPassword.equals(newPassword)) {
			attributes.addFlashAttribute("error", "確認密碼不一致");
			return "redirect:/password";
		}
		member.setPassword(newPassword);
		Member member1 = loginService.save(member);
		if (member1 != null) {
			attributes.addFlashAttribute("message", "密碼修改成功，下次登入請使用新密碼");
			session.setAttribute("member", member1);
		}
		return "redirect:/products";
	}

	// ===============================================================

	// 測試錯誤處理頁面
	@GetMapping("/exception")
	public String testException() {
		throw new RuntimeException("測試錯誤處理");
	}

}
