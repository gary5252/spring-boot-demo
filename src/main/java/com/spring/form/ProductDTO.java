package com.spring.form;

import com.spring.domain.Product;
import com.spring.util.ProductBeanUtils;
import org.springframework.beans.BeanUtils;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProductDTO {

	@NotBlank
	private String name;
	
	@Min(0)
	private int price;
	
	@Min(0)
	private int amount;
	
	private String description;
	
	@NotBlank
	private int cid;
	
	public ProductDTO() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	// convert(資料複製來源類別, 傳輸目的實體類別)
	public void convert(Product product) {
		new ProductConvert().convert(this, product);
	}
	
	public Product convert() {
		return new ProductConvert().convert(this);
	}
	
	
	private class ProductConvert implements DTOconvert<ProductDTO, Product> {
		@Override
		public Product convert(ProductDTO productDTO, Product product) {
			String[] nullPropertiesNames = ProductBeanUtils.getNullPropertiesNames(productDTO);
			// 可以設置陣列數組作為參數讓BeanUtils不將那些欄位列入複製對象
			BeanUtils.copyProperties(productDTO, product, nullPropertiesNames);
			return product;
		}
		
		@Override
		public Product convert(ProductDTO productDTO) {
			Product product = new Product();
			BeanUtils.copyProperties(productDTO, product);
			return product;
		}
	}
	
}
