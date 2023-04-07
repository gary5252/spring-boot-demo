package com.spring.service;

import com.spring.test.Article;

public interface ArticleService {

	Article saveArticle(Article article);
	
	Article updateArticle(Article article);
	
	Article findArticle(Long id);
	
	void deleteArticle(Long id);
	
}
