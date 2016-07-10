package org.ares.cdi.hibernate;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.ares.cdi.hibernate.entity.Product;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductRemoteTest {

	private static EJBContainer container;
	private static Context context;

	private static ProductRemote productRemote;
	
	private static Connection conn;
	
	@BeforeClass
	public static void initializeContainerAndDatabase() throws Exception{
		
		container = EJBContainer.createEJBContainer();
		context = container.getContext();
		productRemote = (ProductRemote) context.lookup("java:global/cdi-hibernate/ProductSessionBean!org.ares.cdi.hibernate.ProductRemote");
		
		// create tables and sequences
		Statement st;
		conn = DriverManager.getConnection("jdbc:hsqldb:mem:cdihibernate", "sa", "");
		st = conn.createStatement();
		st.executeUpdate("create sequence hibernate_sequence start with 1 increment by 1");
		
		st = conn.createStatement();
		st.executeUpdate("create table Product (id integer not null, name varchar(255), price double not null, primary key (id))");
		
		st = conn.createStatement();
		st.executeUpdate("insert into Product (id, name, price) values (20, 'Books', 100.00)");
		
		st = conn.createStatement();
		st.executeUpdate("insert into Product (id, name, price) values (21, 'Phone', 200.00)");
		conn.close();
		
	}

	@Test
	public void testLookup(){
		assertNotNull(productRemote);
	}
	
	
	@Test
	public void testAddProduct(){
		int id = -1;
		id = productRemote.addProduct("toothbrush", 60.00);
		assertNotEquals(id, -1);
	}

	@Test
	public void testUpdateProduct(){
		productRemote.updateProduct(20, "Books", 150.00);
		Product books = productRemote.get(20);
		assertEquals(150.00, books.getPrice(),0);
	}
	
	@Test
	public void testGetProduct(){
		productRemote.get(21);
		Product phone = productRemote.get(21);
		assertEquals(200.00, phone.getPrice(),0);
		assertEquals("Phone", phone.getName());
	}
	
	
	@AfterClass
	public static void tearDown() throws Exception{
		Statement st;
		conn = DriverManager.getConnection("jdbc:hsqldb:mem:cdihibernate", "sa", "");
		st = conn.createStatement();
		st.executeUpdate("drop sequence hibernate_sequence");
		
		st = conn.createStatement();
		st.executeUpdate("drop table Product");
		conn.close();
		
		container.close();
	}

}
