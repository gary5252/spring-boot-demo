package com.spring.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.List;
//import com.spring.domain.Product;
//import java.util.Map;

@Entity
public class ArticleTest {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    /**
     * 類別的 List 屬性因為沒有初始化所以開始還是 null 所以沒辦法用 GETTER，
     * 除非 GETTER 中有先定義好初始化 (new ArrayList<>();)
     */
    private List<String> goods;
    private List<Integer> goodsAmount;
    // 可以轉成 XMLTYPE，但配置好麻煩不用了
//    private String[] goodsTest;
//    private int[] goodsAmountTest;
    
    //-------------------------------------
    // 實體類 List<> 也沒辦法轉換
//    private List<Product> prodList;
    
    // Map 測試轉不了實體屬性
//    private List<Map<String, Object>> goodsList;
//    private Map<String, Object> testMap;

    public ArticleTest() {
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

    public List<String> getGoods() {
        return goods;
    }

    public void setGoods(List<String> goods) {
        this.goods = goods;
    }
    
	public List<Integer> getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(List<Integer> goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

//	public String[] getGoodsTest() {
//		return goodsTest;
//	}
//
//	public void setGoodsTest(String[] goodsTest) {
//		this.goodsTest = goodsTest;
//	}
//
//	public int[] getGoodsAmountTest() {
//		return goodsAmountTest;
//	}
//
//	public void setGoodsAmountTest(int[] goodsAmountTest) {
//		this.goodsAmountTest = goodsAmountTest;
//	}

//	public List<Product> getProdList() {
//		return prodList;
//	}
//
//	public void setProdList(List<Product> prodList) {
//		this.prodList = prodList;
//	}
	
	
//	public List<Map<String, Object>> getGoodsList() {
//		return goodsList;
//	}
//
//	public void setGoodsList(List<Map<String, Object>> goodsList) {
//		this.goodsList = goodsList;
//	}
	
//	public Map<String, Object> getTestMap() {
//		return testMap;
//	}
//
//	public void setTestMap(Map<String, Object> testMap) {
//		this.testMap = testMap;
//	}

}
