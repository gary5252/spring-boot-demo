package com.spring.interceptor;

//import org.springframework.web.context.request.WebRequestInterceptor;
//import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 攔截器類別，影片是繼承 HandlerInterceptorAdapter，但一樣的
 * import 路徑只找到這個 WebRequestHandlerInterceptorAdapter，就先頂著用
 * 看後面會不會有什麼問題。
 * 然後這個父類別要求建構式要傳這個參數 WebRequestInterceptor，也不知道後面會不會有什麼影響，
 * 反正先做再說。
 * ****
 * 後面找到 2022 年 Spring Boot 攔截器相關的文章，直接套用，如下:
 * ****
 * 測試完成，此登入驗證攔截器運作正常
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
//	public LoginInterceptor(WebRequestInterceptor requestInterceptor) {
//		super(requestInterceptor);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response,
							 Object handler) throws Exception {
		if (request.getSession().getAttribute("member") == null) {
			response.sendRedirect("/login");
			return false;
		}
		return true;
	}
	
	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("2.攔截器：postHandle");
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
//        System.out.println("3.攔截器：afterCompletion");
    }

}
