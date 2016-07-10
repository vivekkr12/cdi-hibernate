package org.ares.cdi.hibernate.sf;

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
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

@RequestScoped
public class MySessionFactory implements SessionFactoryTemplate{

	@Inject
	private SessionFactory sessionFactory;
	
	private Session currentSession;
	
	@Override
	public Session openSession(){
		return sessionFactory.openSession();
	}
	
	@Override
	public Session getCurrentSession(){
		if(currentSession == null){
			currentSession = sessionFactory.openSession();
		}
		return currentSession;
	}
	
	@Override
	public StatelessSession openStatelessSession() {
		return sessionFactory.openStatelessSession();
	}
	
	@PreDestroy
	private void closeSession(){
		if(currentSession!=null && currentSession.isOpen()) {
			currentSession.close();
		}
	}
}
