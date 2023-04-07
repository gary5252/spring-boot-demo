package com.spring.service;

import com.spring.test.CommentTest;

public interface CommentService {

	CommentTest saveComment(CommentTest comment);
	
	void deleteComment(Long id);
	
	CommentTest findOne(Long id);
}
