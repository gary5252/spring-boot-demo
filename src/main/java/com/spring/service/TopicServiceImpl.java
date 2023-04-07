package com.spring.service;

import com.spring.test.Topic;
import com.spring.test.Article;
import com.spring.test.TopicRepository;
import com.spring.test.ArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 多對多Service層實作，全部都納入@Transactional事務管理，
 * 但DB似乎會因此有session被占用。目前問題是每次run的時候console會一直有報錯訊息，
 * 但執行結果跟影片教的沒有什麼差。
 */

@Service
public class TopicServiceImpl implements TopicService {
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private ArticleRepository articleRepository;

	@Transactional
	@Override
	public Topic saveTopic(Topic topic) {
		// TODO Auto-generated method stub
		return topicRepository.save(topic);
	}

	@Transactional
	@Override
	public Topic findTopic(Long id) {
		// TODO Auto-generated method stub
		Topic topic = topicRepository.findById(id).get();
		System.out.println(" >>>>> " + topic.getArticles());
		return topic;
	}

	@Transactional
	@Override
	public Topic updateTopic(Topic topic) {
		// TODO Auto-generated method stub
		return topicRepository.save(topic);
	}

	@Transactional
	@Override
	public Topic includeArticle(Long topicId, Long articleId) {
		Topic topic = topicRepository.findById(topicId).get();
		Article article = articleRepository.findById(articleId).get();
		topic.getArticles().add(article);
//		return topicRepository.save(topic); 納入事務管理就不用特地保存了，因為當事務提交時也會同步更新到資料庫
		return topic;	// return 就會事務提交了
	}

	@Transactional
	@Override
	public Topic removeArticle(Long topicId, Long articleId) {
		Topic topic = topicRepository.findById(topicId).get();
		Article article = articleRepository.findById(articleId).get();
		topic.getArticles().remove(article);
//		return topicRepository.save(topic); 理由同上
		return topic;
	}

	@Transactional
	@Override
	public void deleteTopic(Long id) {
		// TODO Auto-generated method stub
		topicRepository.deleteById(id);
	}

}
