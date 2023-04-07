package com.spring.test;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.spring.test.CommentTest;


@Entity
public class Article {

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private String content;
	
	// 通常像這種集合(List,Set,Map) 對多關聯屬性，系統都是預設延遲加載(fetch = FetchType.LAZY)以免大量占用記憶體
	// 設置級聯新增、刪除 PERSIST, REMOVE
	@OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
	private List<CommentTest> comments = new ArrayList<>();
	
	// 被維護端加上 mappedBy = "主維護端關聯屬性名稱" 的參數
	@ManyToMany(mappedBy = "articles")
	private List<Topic> topics = new ArrayList<>();
	
	public Article() {
	}
	
	public void addComment(CommentTest comment) {
		comment.setArticle(this);
		comments.add(comment);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<CommentTest> getComments() {
		return comments;
	}
	public void setComments(List<CommentTest> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", content=" + content + ", comments=" + comments + "]";
	}
	
	
}
