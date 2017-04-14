package org.ares.cdi.hibernate.interceptors;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.ares.cdi.hibernate.sf.MySessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Interceptor binding to handle transactions marked by 
 * {@link Transactional} annotation. For methods which are 
 * marked as transactional, this method begins a 
 * {@link Transaction} on current session before method execution,
 * commits on method end and rollbacks in case of any
 * exception.
 * @author Vivek
 *
 */
@Interceptor
@Transactional
public class TransactionManager {

	@Inject
	private MySessionFactory sessionFactory;
	
	@AroundInvoke
	public Object handleTransaction(InvocationContext context) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			return context.proceed();
		}
		catch(Exception e){
			tx.rollback();
			throw e;
		}
		finally{
			if(tx.getStatus().equals(TransactionStatus.ACTIVE)){
				try{
					tx.commit();
				}
				catch(Exception e){
					tx.rollback();
					throw e;
				}
			}
		}
	}

}
