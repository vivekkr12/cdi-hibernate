package org.ares.cdi.hibernate;

import static org.junit.Assert.*;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MySessionFactoryTest {

	private static EJBContainer container;
	private static Context context;

	private static MySessionFactoryTestBean sfTestBean;
	
	@BeforeClass
	public static void initializeContainerAndDatabase() throws Exception{
		
		container = EJBContainer.createEJBContainer();
		context = container.getContext();
		sfTestBean = (MySessionFactoryTestBean) context.lookup("java:global/cdi-hibernate/MySessionFactoryTestBean");
		
	}

	@Test
	public void testLookup(){
		assertNotNull(sfTestBean);
	}
	
	@Test
	public void testSessionNotNull(){
		assertFalse(sfTestBean.checkCurrentSession());
	}
	
	@Test
	public void testSessionSame(){
		assertTrue(sfTestBean.compareCurrentSession());
	}
	
	@Test
	public void testOpenSessionDifferent(){
		assertFalse(sfTestBean.getCurrentSession()==sfTestBean.openSession());
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		container.close();
	}


	
}
