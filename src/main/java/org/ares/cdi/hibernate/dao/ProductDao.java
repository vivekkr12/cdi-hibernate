package org.ares.cdi.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.ares.cdi.hibernate.entity.Product;
import org.ares.cdi.hibernate.interceptors.Transactional;
import org.ares.cdi.hibernate.sf.MySessionFactory;

public class ProductDao {

	@Inject
	private MySessionFactory sessionFactory;
	
	@Transactional
	public int addProduct(String name, double price){
		Product product = new Product(name, price);
		int id = (int) sessionFactory.getCurrentSession().save(product);
		return id;
	}
	
	@Transactional
	public void updateProduct(int id, String name, double price){
		Product product = sessionFactory.getCurrentSession().get(Product.class, id);
		product.setName(name);
		product.setPrice(price);
	}
	
    public Product get(int id) {
        Product product = sessionFactory.getCurrentSession().get(Product.class, id);
        return product;
    }
	
    @SuppressWarnings("unchecked")
    public List<Product> listAll() {
        List<Product> products = sessionFactory.getCurrentSession().createCriteria(Product.class).list();
        return products;
    }
	
}
