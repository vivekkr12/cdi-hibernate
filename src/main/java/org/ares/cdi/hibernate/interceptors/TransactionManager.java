package org.ares.cdi.hibernate.interceptors;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Vivek Kumar
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
