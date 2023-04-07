package com.spring.service;

import com.spring.test.Topic;

public interface TopicService {

	Topic saveTopic(Topic topic);
	
	Topic findTopic(Long id);

	Topic updateTopic(Topic topic);
	
	Topic includeArticle(Long topicId, Long articleId);
	
	Topic removeArticle(Long topicId, Long articleId);
	
	void deleteTopic(Long id);
}
