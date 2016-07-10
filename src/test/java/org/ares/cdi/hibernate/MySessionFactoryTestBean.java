package org.ares.cdi.hibernate;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.ares.cdi.hibernate.sf.MySessionFactory;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

@Singleton
public class MySessionFactoryTestBean {

	@Inject
	private MySessionFactory sessionFactory;
	
	private Session session;
	
	@PostConstruct
	private void init() {
		session = sessionFactory.getCurrentSession();
	}
	
	public Session getCurrentSession() {
		return session;
	}
	
	public boolean checkCurrentSession() {
		return session == null;
	}
	
	public boolean compareCurrentSession(){
		return session==sessionFactory.getCurrentSession();
	}
	
	public Session openSession() {
		return sessionFactory.openSession();
	}
	
	
	public TransactionStatus getTranscationStatus(){
		return sessionFactory.getCurrentSession().getTransaction().getStatus();
	}
	
}
