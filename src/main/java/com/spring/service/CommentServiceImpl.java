package com.spring.service;

import com.spring.test.CommentTest;
import com.spring.test.CommentRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	public CommentTest findOne(Long id) {
		return commentRepository.findById(id).get();
	}

	@Transactional
	@Override
	public CommentTest saveComment(CommentTest comment) {
		// TODO Auto-generated method stub
		return commentRepository.save(comment);
	}

	@Transactional
	@Override
	public void deleteComment(Long id) {
		CommentTest comment = commentRepository.findById(id).get();
//		comment.setArticle(null); 只用這個還刪不掉，他還是保存在對一的實體類中的集合裡，只能用下面幾行的方式來刪
//		List<CommentTest> comments = comment.getArticle().getComments();
//		for (CommentTest comment1:comments) {
//			if (id == comment1.getId()) {
//				comments.remove(comment1);
//				break;
//			}
//		}
		comment.clearComment();		// 一樣將上面的清除關聯程式封裝進類裡面
		commentRepository.deleteById(id);
	}

}
