package com.spring.Timer;

import org.springframework.stereotype.Component;
//import org.springframework.context.annotation.Scope;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 創建此元件來儲存 BidTimer 實例，
 * 才能個別指定及調用。
 * 目前問題為不知道為什麼沒有存取到 Timer 實例(save 的當下有存取到，另外的方法調用就空指針報錯)
 */

@Component
public class BidTimerList {

	Map<Long, BidTimer> timerList = new HashMap<>();
	
    public BidTimer get(Long itemId) {
        return this.timerList.get(itemId);
    }

    public void add(Long itemId, BidTimer timer) {
        this.timerList.put(itemId, timer);
    }

    public void remove(Long itemId) {
        this.timerList.remove(itemId);
    }
    
    public boolean checkKey(Long itemId) {
    	return this.timerList.containsKey(itemId);
    }
    
    public void getKeys() {
    	System.out.println("timer列表" + this.timerList);
		for (Long i:this.timerList.keySet()) {
			System.out.println("Key here > " + i);	
		}
		
    }
}
