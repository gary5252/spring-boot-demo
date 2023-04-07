package com.spring.test;

//import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

/**
 * 測試執行 Timer 排程、延遲、定時執行任務
 * 先創建任務內容類別，記得繼承TimerTask然後要
 * 覆寫 run() 方法，覆寫內容為要執行的任務
 */
public class TestTask extends TimerTask {

	@Override
	public void run() {
		// 測試練習，先印出時間看看
		System.out.println("Task 執行時間：" + new Date());
	}
}
