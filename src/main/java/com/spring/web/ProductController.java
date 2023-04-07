package com.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.service.ProdService;
import com.spring.domain.Product;
import com.spring.domain.Member;
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

//下面這個包含了 HttpServletRequest/Response,Cookie,HttpSession
import jakarta.servlet.http.*;

import java.util.Timer;
import java.util.TimerTask;

@Controller
public class ProductController {
	
	// 日誌紀錄
//	private final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProdService prodService;
	
	// 作業 : 查詢複合表格資料後(List<Object>)，再從查詢結果的列表取其中的值
	@GetMapping("/products/hw")
	public String hw(Model model) {
		// name,description,address,amount
		// -------------- 抓單一值 ----------------------
		List<Object[]> test = prodService.findBySQLHW();
		Object[] test1 = test.get(0);
		
		// -------------- 抓全部值的列表 -------------------
		// 最後透過 List<Map<String, Object>> xxx = new ArrayList<>(); 成功完成
		
		List<Map<String, Object>> testlist = new ArrayList<>();
		for(int i = 0 ; i < test.size(); i++ ){
			Map<String, Object> map = new HashMap<>();
			map.put("name", test.get(i)[0]);
			map.put("description", test.get(i)[1]);
			map.put("address", test.get(i)[2]);
			map.put("amount", test.get(i)[3]);
			testlist.add(map);
		}
		model.addAttribute("test1", test1[2]);
		model.addAttribute("testlist", testlist);
		int testInt = model.hashCode();
		System.out.println(">>>>>>>>>>>>>>>> " + testInt);
		
		// 使用陣列失敗 >  分開存入 model 會在前端沒辦法同時在同一張表格迭代
		// 使用 Map 的想法是正確的，但迭代的部分搞錯了，應該要使用上面的 List<Map>
		// 從外層先行迭代再抓取每一次迭代裡的值，而不是 Map<String, Object[]>這樣
		// 要迭代的東西在 Map 裡，雖然可能有能解決的寫法，但粗略的想沒意外比較沒效率
		
//		String[] name = new String[test.size()];
//		String[] description = new String[test.size()];
//		String[] address = new String[test.size()];
//		int[] amount = new int[test.size()];
//		for(int i = 0 ; i < test.size(); i++) {
//			Object[] test2 = test.get(i);
//			name[i] = (String)test2[0];
//			description[i] = (String)test2[1];
//			address[i] = (String)test2[2];
//			amount[i] = (int)test2[3];
//		}
//		Map<String, Object[]> testmap = new HashMap<>();
//		testmap.put("name", name);
//		testmap.put("description", description);
//		testmap.put("address", address);
//		testmap.put("amount", amount);

//		model.addAttribute("names",name);
//		model.addAttribute("descriptions",description);
//		model.addAttribute("address",address);
//		model.addAttribute("amount",amount);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            int sec = 20;
            int min = 2;

            public void run() {

                model.addAttribute("min",min);
                model.addAttribute("sec",sec);
                sec--;

                if (sec < 0) {
                    min -= 1;
                    sec = 59;
                    if (min < 0) {
                        timer.cancel();
                        model.addAttribute("timeout", true);
                    }
                }
            }
        }, 0, 1000);
		return "test";
	}
	
	// -----------------------------------------------------------
	
	// 首頁 > 商品頁面(分頁)
	@GetMapping("/products")
	public String list(@PageableDefault(size = 10,sort = {"pid"},direction = Sort.Direction.DESC) Pageable pageable,
//					   @RequestParam(defaultValue="0") int page,
//			  		   @RequestParam(defaultValue="5") int size,
			// 另有使用註解的取值方式 > @SessionAttribute("member")
//			@SessionAttribute("member") Member member1,
					   HttpSession session,
					   @RequestParam(defaultValue="all") String amountSelect,
					   @RequestParam(defaultValue="") String search,
//					   HttpServletRequest request,
			  		   Model model) {
//		model.addAttribute("products", products);
//		session = request.getSession();  // 測試過了不用request也能成功取值，可以成功抓到該資料類別所有屬性
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return "redirect:/login";
		}
		model.addAttribute("member", member);
//		System.out.println("---- session : " + member.getMname());
		model.addAttribute("amountSelect", amountSelect);
		model.addAttribute("search", search);
//		List<Product> products = prodService.findAll();
//		Sort sort = Sort.by(Sort.Direction.DESC, "pid");
//		Page<Product> page1 = prodService.findAllByPage(PageRequest.of(page, size, sort));
		switch(amountSelect) 
		{
			case "all" :
				Page<Product> page1 = prodService.searchProduct(search, pageable);
				model.addAttribute("page", page1);
				break;
			case "normal" :
				Page<Product> page2 = prodService.searchProductWithNormal(search, pageable);
				model.addAttribute("page", page2);
				break;
			case "over" :
				Page<Product> page3 = prodService.searchProductWithOver(search, pageable);
				model.addAttribute("page", page3);				
				break;
			case "out" :
				Page<Product> page4 = prodService.searchProductWithOut(search, pageable);
				model.addAttribute("page", page4);
				break;
			default :
				Page<Product> page5 = prodService.findAllByPage(pageable);
				model.addAttribute("page", page5);
		}
//		if (amountSelect.equals("all")) {
//			Page<Product> page1 = prodService.findAllByPage(pageable);
//			model.addAttribute("page", page1);
//		}else {
//			Page<Product> page1 = prodService.searchProductWithNormal(pageable);
//			model.addAttribute("page", page1);
//		}
		
		return "products";
	}
	
	// 單一商品詳細資料
	@GetMapping("/products/{pid}")			// Model 就是一個給前端用的模板/模型
	public String detail(@PathVariable int pid, Model model, HttpSession session) 
		throws ProductNotFoundException{
		// findOne()方法回傳Optional<Product>，但此型態沒辦法做為另一型態宣告，得套用他的get()方法
		// 獲取Product型態資料
		Product product = prodService.findOne(pid).get();
		if (product == null) {
			// 儘管是手動拋出例外，但他仍然屬於http 500 的錯誤，所以仍會被導入500錯誤模
			// 板頁面(若沒自定義錯誤網頁就是去spring WhiteLabel Error Page 那裏)
			 throw new ProductNotFoundException("商品資料不存在");
		}
		model.addAttribute("product", product);
		// Interceptor 攔截器設置完成不用每一個方法都加登入驗證了
//		Member member = (Member) session.getAttribute("member");
//		if (member == null) {
//			return "redirect:/login";
//		}
//		model.addAttribute("member", member);
		return "product";
	}
	
	@GetMapping("/products/input")
	public String inputPage(Model model) {
		// 傳入一個新建的空物件，因為更新跟新增更用同個模板，然後模板為了初始化有設定
		// 模型資料，所以新增這邊傳入一個空的以免報錯
		model.addAttribute("product", new Product());
		return "input";
	}
	
	// ----------- 新增 --------------------
	// 前端POST過來的資訊由Spring轉成實體類別再將實體類別存入
	@PostMapping("/products")
	public String post(@Valid Product product,
					   BindingResult result,
					   final RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "input";
		}
		// 因為 oracle DB PK的自動增長很難建所以沒用因此新增資料都要有pid的資料，然後這邊使用
		// save() 就會導致新增時已經有該PK的資料就會變成 update
		// 新增跟修改都共同掉用這個方法
		Product product1 = prodService.save(product);
		if (product1 != null) {
			attributes.addFlashAttribute("message", ">>>" + product1.getName() + "<<<");
		}
		
//		if (product1 != null) {
//			model.addAttribute("message", ">>>>" + product1.getName() + "<<<<");
//		} 
		// 這邊 model 將會無法顯示，因 model 生命週期只在一個request 請求中，這邊的流程卻要
		// 走三個request: POST ---> redirect ---> GET，所以在後面回到總列表頁面時將會
		// 無法帶入此 model。得用 FlashAttribute 才能讓模型的生命週期持續到後面的 request 請求
		return "redirect:/products";
	}
	
	// --------- 修改 ----------
	// 先抓單一資料將該資料屬性帶入model準備寫回前端的表單欄位
	@GetMapping("/products/{pid}/produpdate")
	public String inputEditPage(@PathVariable int pid,Model model) {
		Product product = prodService.findOne(pid).get();
		model.addAttribute("product", product);
		return "produpdate";
	}
	
	// ---------- 刪除 -----------
	@GetMapping("/products/{pid}/delete")
	public String delete(@PathVariable int pid, final RedirectAttributes attributes,HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		if (!member.getMname().equals("admin")) {
			return "redirect:/products";
		}
		prodService.deleteOne(pid);
		attributes.addFlashAttribute("message", "--- 刪除成功 ---");
		return "redirect:/products";
	}
	
	// -----------------------------------------------------
	
	// 例外處理
//	@ExceptionHandler({Exception.class})
//	public ModelAndView handleException(HttpServletRequest request, Exception e) throws Exception {
//	
//		logger.error("Request URL : {} , Exception : {}",request.getRequestURL(),e.getMessage());
//		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
//			throw e;
//		}
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("url",request.getRequestURL());
//		mv.addObject("exception",e);
////		mv.setViewName("error/error");
//		mv.setViewName("error/500");
//		
//		return mv;
//	}
	
}
