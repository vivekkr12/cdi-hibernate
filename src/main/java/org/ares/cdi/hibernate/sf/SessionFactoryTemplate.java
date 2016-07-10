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

import javax.enterprise.context.RequestScoped;

import org.ares.cdi.hibernate.interceptors.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

/**
 * A template to define three most used methods from {@link SessionFactory}.
 * Custom implementation for a {@link RequestScoped} session factory 
 * can implement this interface.
 * @author Vivek
 * @see MySessionFactory
 */
public interface SessionFactoryTemplate {

	/**
	 * Returns a hibernate {@link Session} bound to the {@link RequestScoped}.
	 * This ensures only one instance of session per request / thread in Java EE
	 * environment.
	 * There is not need manually close the session. Sessions will be closed 
	 * when request ends.<br>
	 * For operations with transaction demarcation, add {@link Transactional} 
	 * annotation. This will handle transaction begin / commit and rollback
	 * @return current Session
	 */
	public Session getCurrentSession();
	
	/**
	 * Opens and returns a new hibernate {@link Session}. 
	 * This session has to be manually closed and transactions must 
	 * manually begin / commit / rollback.
	 * @return new Session
	 */
	public Session openSession();
	
	/**
	 * Opens and returns a new hibernate {@link StatelessSession}. 
	 * This session has to be manually closed and transactions must 
	 * manually begin / commit / rollback.
	 * @return new StatelessSession
	 */
	public StatelessSession openStatelessSession();
	
}
