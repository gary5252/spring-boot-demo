package com.spring.test;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Topic {
	
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	@ManyToMany
	@JoinTable(		// 自定義設置多對多關聯的關係表，系統會另外生成一張資料表儲存關聯內容，主要設置在主維護類別這
			name = "t_topic_article",	// 設置該虛擬關係資料表名稱
			joinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"),
			// 設置關聯內容欄位 > name = 欄位名稱, re... = 要使用作為外鍵(FK)的實體類別屬性
			inverseJoinColumns = @JoinColumn(name = "article_id")
			// 不設置FK的話預設使用 Id
			)
	private List<Article> articles = new ArrayList<>();
	
	public Topic() {
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	

}
