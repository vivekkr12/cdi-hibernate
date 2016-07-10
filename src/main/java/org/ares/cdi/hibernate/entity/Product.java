package org.ares.cdi.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product implements Serializable{

	private static final long serialVersionUID = 5245577327086935320L;
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private double price;
	
	public Product(){
		
	}

	public Product(String name, double price) {
		super();
		this.name = name;
		this.price = price;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price
				+ "]";
	}
	
}
