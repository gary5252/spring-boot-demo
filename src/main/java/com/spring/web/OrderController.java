package com.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import com.spring.service.OrderService;
import com.spring.service.ProdService;
import com.spring.service.BuyService;
import jakarta.validation.Valid;

// 下面這個包含了 HttpServletRequest/Response,Cookie,HttpSession
import jakarta.servlet.http.*;

import com.spring.domain.Member;
import com.spring.domain.Product;
import com.spring.domain.BuyTemp;
import com.spring.domain.BuyList;
import com.spring.exception.ProductNotFoundException;
import java.util.*;
import java.util.stream.*;

@Controller
@RequestMapping("/products")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private BuyService buyService;

	@Autowired
	private ProdService prodService;

	// 每個商品的購買頁面
	@GetMapping("/{pid}/buy")
	public String buyPage(@PathVariable int pid, Model model, HttpSession session)
			throws ProductNotFoundException {

		// findOne()方法回傳Optional<Product>，但此型態沒辦法做為另一型態宣告，得套用他的get()方
		// 法來獲取Product型態資料
		Product product = prodService.findOne(pid).get();
		if (product == null) {
			// 儘管是手動拋出例外，但他仍然屬於Http 500 的錯誤，所以仍會被導入500錯誤模
			// 板頁面(若沒自定義錯誤網頁就是去spring WhiteLabel Error Page 那裏)
			throw new ProductNotFoundException("商品資料不存在");
		}
		// 檢查是否已有選購紀錄，有的話就跳轉到更改頁面
		int exist = orderService.findPidExist(pid);
		if (exist != 0) {
			return "redirect:/products/buylist/" + pid + "/buy/update";
		}
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("product", product);
		model.addAttribute("member", member);
		return "buy";
	}

	// 先將要購買的單一商品資訊先寫入TEMP順便做個數量驗證
	@PostMapping("/addBuy")
	public String addBuy(HttpSession session,
			@Valid BuyTemp temp,
			BindingResult result,
			final RedirectAttributes attributes,
			Model model) {
		try {
			int currentAmount = prodService.findAmount(temp.getPid());
			if (temp.getAmount() == 0) {
				return "redirect:/products";
			}
			if (temp.getAmount() < 0) {
				// result.rejectValue("amount", "numInvalidError", "輸入值有誤");
				attributes.addFlashAttribute("error", "輸入值有誤");
				// model.addAttribute("error", "輸入值有誤");
				return "redirect:/products/" + temp.getPid() + "/buy";
			}
			if (temp.getAmount() > currentAmount) {
				// result.rejectValue("amount", "numInvalidError", "輸入值超出目前庫存");
				attributes.addFlashAttribute("error", "輸入值超出目前庫存");
				// model.addAttribute("error", "輸入值超出目前庫存");
				return "redirect:/products/" + temp.getPid() + "/buy";
			}

			// if (result.hasErrors()) {
			// return "buy";
			// }

			BuyTemp temp1 = orderService.save(temp);
			if (temp1 != null) {
				attributes.addFlashAttribute("message", "品項已加入購物車");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return "buy";
		}

		return "redirect:/products";
	}

	// ==============================================================
	// 抓出所有要購買的商品資訊並顯示出來(購物車)，確定好後就可以寫進真的購物車實體類
	@GetMapping("/buylist")
	public String buyList(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		// Member member = loginService.findOne(mid).get();
		// 下面為測試拋出例外變更狀態種類，結果無法成功變換，同商品單一查詢結果
		if (member == null) {
			return "redirect:/login";
		}
		model.addAttribute("member", member);
		// 抓所有選購商品的資料
		List<BuyTemp> temps = orderService.findByMid(member.getMid());
		// 看有無選購
		if (temps.isEmpty()) {
			model.addAttribute("none", true);
		}
		model.addAttribute("temps", temps);

		// 彙總商品 id, amount, price 進 List，並計算訂單總金額
		List<Integer> prodList = new ArrayList<>();
		List<Integer> amountList = new ArrayList<>();
		List<Integer> priceList = new ArrayList<>();
		int total = 0;
		for (BuyTemp temp : temps) {
			int singlePrice = (temp.getPrice() * temp.getAmount());
			total += singlePrice;
			prodList.add(temp.getPid());
			amountList.add(temp.getAmount());
			priceList.add(temp.getPrice());
		}
		model.addAttribute("total", total);
		// model.addAttribute("prodList",prodList);
		// model.addAttribute("amountList",amountList);
		// model.addAttribute("priceList",priceList);
		// Date buyDate = new Date();
		// model.addAttribute("buyDate", buyDate);

		return "buyList";
	}

	// 多寫一層確認訂購者資訊的頁面優化體驗
	@GetMapping("/checkOrder")
	public String pay(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		// Member member = loginService.findOne(mid).get();
		// 下面為測試拋出例外變更狀態種類，結果無法成功變換，同商品單一查詢結果
		if (member == null) {
			return "redirect:/login";
		}
		model.addAttribute("member", member);
		// 抓所有選購商品的資料
		List<BuyTemp> temps = orderService.findByMid(member.getMid());
		// 看有無選購
		if (temps.isEmpty()) {
			model.addAttribute("none", true);
		}
		model.addAttribute("temps", temps);

		// 彙總商品 id, amount, price 進 List，並計算訂單總金額
		List<Integer> prodList = new ArrayList<>();
		List<Integer> amountList = new ArrayList<>();
		List<Integer> priceList = new ArrayList<>();
		int total = 0;
		for (BuyTemp temp : temps) {
			int singlePrice = (temp.getPrice() * temp.getAmount());
			total += singlePrice;
			prodList.add(temp.getPid());
			amountList.add(temp.getAmount());
			priceList.add(temp.getPrice());
		}
		model.addAttribute("total", total);
		model.addAttribute("prodList", prodList);
		model.addAttribute("amountList", amountList);
		model.addAttribute("priceList", priceList);
		Date buyDate = new Date();
		model.addAttribute("buyDate", buyDate);

		return "payment";
	}

	// 寫入訂單實體
	@PostMapping("/buy")
	public String buy(@RequestParam(value = "mid", defaultValue = "0") int mid,
			@RequestParam(value = "mname", defaultValue = "0") String mname,
			@RequestParam(value = "prodList", defaultValue = "0") String prodList,
			@RequestParam(value = "amountList", defaultValue = "0") String amountList,
			@RequestParam(value = "priceList", defaultValue = "0") String priceList,
			@RequestParam(value = "totalPrice", defaultValue = "0") int total,
			@RequestParam(value = "buyDate", defaultValue = "2023-02-16") Date buyDate,
			final RedirectAttributes attributes) {

		// prodList 轉換 String -> List<Integer>
		prodList = prodList.replace("]", "");
		prodList = prodList.replace("[", "");
		prodList = prodList.replaceAll(" ", "");
		String[] items = prodList.split(",");
		List<String> test1 = Arrays.asList(items);
		List<Integer> newProdList = test1.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

		// amountList 轉換 String -> List<Integer>
		amountList = amountList.replace("]", "");
		amountList = amountList.replace("[", "");
		amountList = amountList.replaceAll(" ", "");
		String[] items1 = amountList.split(",");
		List<String> test2 = Arrays.asList(items1);
		List<Integer> newAmountList = test2.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

		// priceList 轉換 String -> List<Integer>
		priceList = priceList.replace("]", "");
		priceList = priceList.replace("[", "");
		priceList = priceList.replaceAll(" ", "");
		String[] items2 = priceList.split(",");
		List<String> test3 = Arrays.asList(items2);
		List<Integer> newPriceList = test3.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

		// 寫入購物車實體
		BuyList buyList = new BuyList();
		buyList.setMid(mid);
		buyList.setMname(mname);
		buyList.setTotalPrice(total);
		buyList.setBuyDate(buyDate);
		buyList.setProdList(newProdList);
		buyList.setAmountList(newAmountList);
		buyList.setPriceList(newPriceList);
		BuyList buyList1 = buyService.save(buyList);
		List<Map<Integer, Integer>> errorList = new ArrayList<>();
		if (buyList1 != null) {
			attributes.addFlashAttribute("message", "訂單已送出");
			// 將數量更正回Product
			for (int i : newProdList) {
				int oldAmount = prodService.findAmount(i); // 先找出各 pid 原有的數量
				// 算出各 pid 原有的數量扣掉訂單量
				int newAmount = oldAmount - (newAmountList.get(newProdList.indexOf(i)));
				int updateStatus = prodService.updateAmount(i, newAmount); // 將新數量寫回去
				if (updateStatus != 1) { // 寫回去的過程有狀況就先將 pid 記下來之後看要怎麼處理
					Map<Integer, Integer> map = new HashMap<>();
					// 記一下 pid 跟 訂單數量
					map.put(i, newAmountList.get(newProdList.indexOf(i)));
					errorList.add(map);
					continue;
				}

			}

			orderService.deleteByMid(mid); // 刪除temp實體
		}
		return "redirect:/products";
	}

	// 測試用
	// @PostMapping("/buy")
	// public String test(@RequestParam("mid") int mid,
	// @RequestParam("mname") String mname,
	// @RequestParam("prodList") String prodList,
	// @RequestParam("amountList") String amountList,
	// @RequestParam("priceList") String priceList,
	// @RequestParam("totalPrice") int total,
	// @RequestParam("buyDate") Date buyDate) {
	// // Can't String to List Error
	// prodList = prodList.replace("]", "");
	// prodList = prodList.replace("[", "");
	// prodList = prodList.replaceAll(" ", "");
	// String[] items = prodList.split(",");
	// List<String> test1 = Arrays.asList(items);
	// List<Integer> newList = test1.stream().map(s ->
	// Integer.parseInt(s)).collect(Collectors.toList());
	//// List<Integer> prodList1 = Stream.of(items).map(Integer::parseInt)
	// .collect(Collectors.toList());
	// System.out.println(">>>>>>>>>>>> " + newList);
	//
	//// BuyList buyList1 = buyService.save(buyList);
	// return "redirect:/products";
	// }

	// ===========================================================
	// 修改單一購買數量的頁面，因主鍵空值問題所以與新增部分的模板分離
	@GetMapping("/buylist/{pid}/buy/update")
	public String buyUpdatePage(@PathVariable int pid, Model model)
			throws ProductNotFoundException {

		// findOne()方法回傳Optional<Product>，但此型態沒辦法做為另一型態宣告，得套用他的get()方
		// 法來獲取Product型態資料
		Product product = prodService.findOne(pid).get();
		if (product == null) {
			// 儘管是手動拋出例外，但他仍然屬於Http 500 的錯誤，所以仍會被導入500錯誤模
			// 板頁面(若沒自定義錯誤網頁就是去spring WhiteLabel Error Page 那裏)
			throw new ProductNotFoundException("商品資料不存在");
		}
		// 檢查是否已有選購紀錄，沒的話就跳轉到選購頁面
		int exist = orderService.findPidExist(pid);
		if (exist == 0) {
			return "redirect:/products/" + pid + "/buy";
		}
		model.addAttribute("product", product);
		BuyTemp temp = orderService.findOne(pid);
		model.addAttribute("buyTemp", temp);
		return "buyupdate";
	}

	// 修改單一選購商品數量，為 0 就刪除該選購
	@PostMapping("/updateBuy")
	public String updateBuy(HttpSession session,
			@Valid BuyTemp temp,
			BindingResult result,
			final RedirectAttributes attributes,
			Model model) {
		try {
			int currentAmount = prodService.findAmount(temp.getPid());
			if (temp.getAmount() == 0) {
				return "redirect:/products/buylist/" + temp.getPid() + "/buy/delete";
			}
			if (temp.getAmount() < 0) {
				// result.rejectValue("amount", "numInvalidError", "輸入值有誤");
				attributes.addFlashAttribute("error", "輸入值有誤");
				// model.addAttribute("error", "輸入值有誤");
				return "redirect:/products/buylist/" + temp.getPid() + "/buy/update";
			}
			if (temp.getAmount() > currentAmount) {
				// result.rejectValue("amount", "numInvalidError", "輸入值超出目前庫存");
				attributes.addFlashAttribute("error", "輸入值超出目前庫存");
				// model.addAttribute("error", "輸入值超出目前庫存");
				return "redirect:/products/buylist/" + temp.getPid() + "/buy/update";
			}

			// if (result.hasErrors()) {
			// return "buy";
			// }

			BuyTemp temp1 = orderService.save(temp);
			if (temp1 != null) {
				attributes.addFlashAttribute("message", "數量已修改");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return "redirect:/products/buylist/" + temp.getPid() + "/buy/update";
		}
		return "redirect:/products/buylist";
	}

	@GetMapping("/buylist/{pid}/buy/delete")
	public String buydelete(@PathVariable int pid, final RedirectAttributes attributes, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		orderService.deleteByMidAndPid(member.getMid(), pid);
		attributes.addFlashAttribute("message", "--- 品項刪除成功 ---");
		return "redirect:/products/buylist";
	}
}
