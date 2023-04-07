package com.spring.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component	// 給 Bean 一個身分把它標記為組件讓 Spring Boot 識別並管理才能透過 @Autowired 自動建構並注入
public class LogAspect {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 註解參數帶切入點，execution(* ...) * 代表最終的返回可以是任意返回，
	// log(..)代表不管這類別該方法傳入任何的參數都將她設作切入點
//	@Pointcut("execution(* com.spring.api.LogTestApi.log(..))")
	// 此路徑代表 com.spring.api 裡所有的類別的所有方法(不管任何參數)，都設作切入點
	@Pointcut("execution(* com.spring.api.*.*(..))")
	public void log() {
	}
	
	// 設定在切入點之前要做的動作，該註解參數帶入切入點(名稱或是參數路徑)
//	@Before("execution(* com.spring.api.LogTestApi.log(..))") 
	@Before("log()")
	public void doBefore(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		//	.getDeclaringTypeName() 類別名稱  .getName() 方法名稱
		String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(); 
		RequestLog requestLog = new RequestLog(
					request.getRequestURL().toString(),
					request.getRemoteAddr(),
					classMethod,
					joinPoint.getArgs()
				);
//		logger.info("-------------- do before 1 -----------------");
		logger.info(" Request -----> {} ",requestLog);
	}
	
	// 同 @Before，不過執行是在切入點之後
	@After("log()")
	public void doAfter( ) {
		logger.info("-------------- do after 2 -----------------");
	}
	
	// 在接收到切入點傳回值後才會執行，影片中是在 @After 後執行的，但我實際執行卻是在 @After 之前
	@AfterReturning(returning = "result", pointcut = "log()")
	public void doAfterReturning(Object result) {
		logger.info("-- After Returning ----- : {}", result);
	}
	
	private class RequestLog {
		private String url;
		private String ip;
		private String classMethod;
		private Object[] args;
		public RequestLog(String url, String ip, String classMethod, Object[] args) {
			super();
			this.url = url;
			this.ip = ip;
			this.classMethod = classMethod;
			this.args = args;
		}
		@Override
		public String toString() {
			return "RequestLog [url=" + url + ", ip=" + ip + ", classMethod=" + classMethod + ", args="
					+ Arrays.toString(args) + "]";
		}
		
	}
	
	
}
