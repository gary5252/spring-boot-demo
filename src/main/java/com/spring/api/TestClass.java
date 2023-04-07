package com.spring.api;

public class TestClass {
	private Long id;
	private String name;
	private int value;
	public TestClass(Long id, String name, int value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
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
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "TestClass [id=" + id + ", name=" + name + ", value=" + value + "]";
	}
	

}
