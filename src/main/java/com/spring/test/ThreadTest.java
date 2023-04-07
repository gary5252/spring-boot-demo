package com.spring.test;

public class ThreadTest extends Thread {

    private String message;
    public ThreadTest(String message) {
      this.message = message;
    }
    public void run() {
      System.out.println("Thread message is : " + this.message);
    }
}
