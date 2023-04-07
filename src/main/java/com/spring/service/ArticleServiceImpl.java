package com.spring.service;

import com.spring.test.Article;
import com.spring.test.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 實作的 service 層才要加此註解
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Override
	public Article saveArticle(Article article) {
		// TODO Auto-generated method stub
		return articleRepository.save(article);
	}

	@Override
	public Article updateArticle(Article article) {
		// TODO Auto-generated method stub
		return articleRepository.save(article);
	}

	@Override
	public Article findArticle(Long id) {
		// TODO Auto-generated method stub
		return articleRepository.findById(id).get();
	}

	@Override
	public void deleteArticle(Long id) {
		// TODO Auto-generated method stub
		articleRepository.deleteById(id);
	}

} 
