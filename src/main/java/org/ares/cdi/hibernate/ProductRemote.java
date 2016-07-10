package org.ares.cdi.hibernate;

import java.util.List;

import javax.ejb.Remote;

import org.ares.cdi.hibernate.entity.Product;

@Remote
public interface ProductRemote {

	public int addProduct(String name, double price);
	
	public void updateProduct(int id, String name, double price);
	
    public Product get(int id);
    
    public List<Product> listAll();
	
}
