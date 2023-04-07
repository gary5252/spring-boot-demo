package com.spring;

import java.util.*;

//import java.util.Date;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.test.Article;
import com.spring.test.EntityTest;
import com.spring.test.UserTest;
import com.spring.test.WalletTest;
import com.spring.test.TestTask;
import com.spring.test.ThreadTest;
import com.spring.test.CommentTest;
import com.spring.test.Topic;
import com.spring.service.ProdService;
import com.spring.service.ArticleService;
import com.spring.service.CommentService;
import com.spring.service.TopicService;

@SpringBootTest
class SpringDemoApplicationTests {

	@Autowired
	EntityTest repository;
	
	@Autowired
	ProdService prodService;
	
	@Autowired
	private ArticleService articleService; 
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private TopicService topicService;

	@Test
	public void saveTopic() {
		Topic topic = new Topic();
		topic.setName("主題1");
		topicService.saveTopic(topic);
	}
	
	@Test
	public void updateTopic() {
		Topic topic = topicService.findTopic(2L);
		topic.setName("主題2");
		topicService.saveTopic(topic);
	}
	
	@Test
	public void includeArticle() {
		topicService.includeArticle(1L, 2L);
	}
	
	@Test
	public void findTopic() {
		Topic topic = topicService.findTopic(1L);
//		topic.getArticles();
//		System.out.println(" >>>>> " + topic.getArticles());
		// 為了獲取 LAZY 加載的資料，將程式寫到事務管理內
	}
	
	@Test
	public void removeArticle() {
		topicService.removeArticle(1L, 2L);
	}
	
	@Test
	public void deleteTopic() {
		// Topic 作為關係主維護類別，刪除後也會一併將關聯資料也刪除，
		// 而 Article 作為關係被維護者，刪除前得先手動解除關係。
		topicService.deleteTopic(1L);
	}
	
	@Test
	public void saveArticle() {
		Article article = new Article();
		article.setTitle("文章標題1");
		article.setContent("文章內容1......");
		
//		List<CommentTest> comments = new ArrayList<>();
		CommentTest comment1 = new CommentTest("評論1");
//		comment1.setArticle(article);	// 記得將關聯屬性寫回去 > 已將這部分寫入Bean 的 addComment 方法內了
		CommentTest comment2 = new CommentTest("評論2");
//		comment2.setArticle(article);	// 記得將關聯屬性寫回去 > 已將這部分寫入Bean 的 addComment 方法內了

		// 將寫入方法封裝進實體類中直接調用
		article.addComment(comment1);
		article.addComment(comment2);
		
//		comments.add(comment1);
//		comments.add(comment2);
		
//		article.setComments(comments);
		articleService.saveArticle(article);
		
	}
	
	@Test
	public void updateArticle() {
		Article article = articleService.findArticle(52L);
		article.setContent("所以說那個醬汁呢");
		articleService.updateArticle(article);
	}
	
	@Test
	public void findArticle() {
		Article article = articleService.findArticle(52L);
		System.out.println(" >>>>>> " + article.toString());
	}
	
	@Test
	public void deleteArticle() {
		articleService.deleteArticle(102L);
	}
	
	@Test
	public void saveComment() {
		Article article = articleService.findArticle(52L);
		
		CommentTest comment = new CommentTest();
		comment.setArticle(article);
		comment.setContent("再給我一分鐘就能完成了");
		
		commentService.saveComment(comment);
	}
	
	@Test
	public void deleteComment() {

		commentService.deleteComment(153L);
	}
	
	
	@Test
	void testList() {
//		List<Integer> test = new ArrayList<Integer>();
//		for (int i=1;i<5;i++) {
//			test.add(i);
//		}
//		System.out.println(test);
//		System.out.println(test.size());
//		System.out.println(test.get(3));
//		System.out.println(test.indexOf(1));
//		for (int j:test) {
//			System.out.println(j);
//		}
		int i = prodService.updateAmount(96, 6666);
		System.out.println(">>>>>>>>>> " + i);
	}
	
//	@Test
//	void contextLoads() {
//	}

	// 一對一關聯新增 > 測試成功
	// @Test
	// public void saveTestO2O() {
	// UserTest user = new UserTest();
	// user.setName("test2");
	// user.setPhone("00241342242");
	// user.setCreateDate(new Date());
	// user.setWallet(new WalletTest(300));
	// repository.save(user);
	// }

	// @Test
	// public void updateTestO2O() {
	// UserTest user = repository.findById(1);
	// user.setPhone("09000000000");
	// WalletTest wallet = user.getWallet();
	// wallet.setBalance(88888);
	// user.setWallet(wallet);
	// repository.save(user);
	// }

//	@Test
//	public void findTestO2O() {
//		UserTest user = repository.findById(1);
//		System.out.println(user.toString());
//	}

	@Test
	public void deleteTestO2O() {
		repository.deleteById(152L);
	}
	
	// ==================================================================
	// 測試多執行緒
	@Test
	public void testThread() {
	      ThreadTest thread1 = new ThreadTest("message1");
	      ThreadTest thread2 = new ThreadTest("message2");
	      ThreadTest thread3 = new ThreadTest("message3");
	      ThreadTest thread4 = new ThreadTest("message4");
	      ThreadTest thread5 = new ThreadTest("message5");
	      ThreadTest thread6 = new ThreadTest("message6");
	      ThreadTest thread7 = new ThreadTest("message7");
	      ThreadTest thread8 = new ThreadTest("message8");
	      ThreadTest thread9 = new ThreadTest("message9");
	      ThreadTest thread10 = new ThreadTest("message10");
	      thread1.start();
	      thread2.start();
	      thread3.start();
	      thread4.start();
	      thread5.start();
	      thread6.start();
	      thread7.start();
	      thread8.start();
	      thread9.start();
	      thread10.start();
	}
	
	
	// ==================================================================
	// 測試 Timer 用法

	// 測試在 delay 3秒後啟動工作
	@Test
    void testScheduleDelay(){
        Timer timer = new Timer();
        System.out.println("Delay：3秒");
        System.out.println("In testScheduleDelay：" + new Date());

        // schedule(TimerTask task, long delay)
        timer.schedule(new TestTask(), 3000);
        
        try {
            Thread.sleep(10000);
        }
            catch(InterruptedException e) {
        }

        timer.cancel();
        System.out.println("End testScheduleDelay：" 
            + new Date() + "\n");
    }
	
	
	
	
	// ==================================================================

}
