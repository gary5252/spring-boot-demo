package com.spring.Timer;

import java.text.SimpleDateFormat;
import java.util.*;

public class BidTask extends TimerTask {
    SimpleDateFormat sdf = new SimpleDateFormat("hh時mm分ss秒 yyyy年MM月dd日");
    SimpleDateFormat sdf1 = new SimpleDateFormat("hh時mm分ss秒");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
    int secs = 0;
    int mins = 0;
    int hours = 1;

    @Override
    public void run() {
        // System.out.println(i + ":" + sdf1.format(new Date()) + " - " +
        // sdf2.format(new Date()));
        // System.out.println(i + " Task execute time : " + new Date().getTime());
//        System.out.println(Thread.currentThread().getName() + " Time left: " + min + " min : " + sec + " sec \t > "
//                + sdf1.format(new Date()) + " - " + sdf2.format(new Date()));
        secs--;
        if (secs < 0) {
            mins -= 1;
            secs = 59;
            if (mins < 0) {
                // timer.cancel();
            	hours -= 1;
            	mins = 59;
            	if (hours < 0) {
            		this.cancel();
                	System.out.println(Thread.currentThread().getName() + " :  Time Over! > " + sdf.format(new Date()));
            	}
            }
        }
    }

}
