package org.ares.cdi.hibernate;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.ares.cdi.hibernate.dao.ProductDao;
import org.ares.cdi.hibernate.entity.Product;

@Stateless
public class ProductSessionBean implements ProductRemote{

	@Inject
	private ProductDao productDao;
	
	@Override
	public int addProduct(String name, double price) {
		return productDao.addProduct(name, price);
	}

	@Override
	public void updateProduct(int id, String name, double price) {
		productDao.updateProduct(id, name, price);
	}

	@Override
	public Product get(int id) {
		return productDao.get(id);
	}

	@Override
	public List<Product> listAll() {
		return productDao.listAll();
	}


	
}
