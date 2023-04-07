package com.spring.Timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

import com.spring.domain.Item;
import com.spring.service.AuctionService;
import com.spring.service.AuctionServiceImpl;


public class BidTimer {
	/**
	 * 這邊 service 層不能透過 @Autowired 注入，會無效，
	 * 要使用構造函數代入，測試完成可以運行 service 方法。
	 */
	
//	@Autowired
//	private AuctionService auctionService;
	private AuctionServiceImpl auctionService;
	
    private ScheduledExecutorService timer;
    private Item item;
    public int timeLeft = 200;
    private Boolean bidded;	// 是否有人出價競標
    
    
    public BidTimer(Item item, Boolean bidded, AuctionServiceImpl impl) {
		this.item = item;
		this.bidded = bidded;
		this.auctionService = impl;
//		this.timer = Executors.newSingleThreadScheduledExecutor();
	}
    
    public int getTimeLeft() {
    	return this.timeLeft;
    }

	public void init() {
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }
	
//	Callable<Integer> timerTask = () -> {
//		timeLeft --;
//		if (timeLeft < 0) {
//            stop();
//            System.out.println(item.getName() + " > Time Over!");
//            if (bidded) {
//            	auctionService.statusUpdate(2, item.getId()); // 2 結標
//            }else {
//            	auctionService.statusUpdate(3, item.getId()); // 3 流標
//            }
//        }
//		return timeLeft;
//	};
	
//	public int countDownAndReturn() {
////		ScheduledFuture<Integer> future = this.timer
//		return 0;
//	}
    
    public void countDown() {
        // try {
        // Thread.sleep(1000);
        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }
        this.timer.scheduleAtFixedRate(() -> {
//        	p.call(timeLeft);
            timeLeft--;
            if (timeLeft < 0) {
//                System.out.println(item.getName() + " > Time Over!");
                if (bidded) {
                	int action = auctionService.statusUpdate(2, item.getId()); // 2 結標
                	System.out.println(item.getName() + " > Time Over! 結標!");
                }else {
                	int action = auctionService.statusUpdate(3, item.getId()); // 3 流標
                	System.out.println(item.getName() + " > Time Over! 流標!");
                }
//                stop();	// 根本沒調用到 > 成功了 > service 方法卡住的關係
                this.stop();
            }
        }, 1, 1, TimeUnit.SECONDS);	
        // scheduleAtFixedRate(Task, Initial Delay, Period, Time Unit)
    }

    // 停止 Timer
    public void stop() {
        this.timer.shutdown();
        System.out.println("shut down~~~~");
    }

}