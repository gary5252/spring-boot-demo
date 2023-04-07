package com.spring.test;

import jakarta.persistence.*;

@Entity
public class CommentTest {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    
    @ManyToOne
    private Article article;

    // 清除對一關聯來做刪除的動作
    public void clearComment() {
    	// 雖然繞來繞去很煩，但單純將this.article 設 null 還是沒辦法刪除所以就是得這樣做
    	this.getArticle().getComments().remove(this);
    }
    
    public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public CommentTest() {
    }
    
    public CommentTest(String content) {
    	this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
