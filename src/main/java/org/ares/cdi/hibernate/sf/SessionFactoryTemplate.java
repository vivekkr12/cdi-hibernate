package org.ares.cdi.hibernate.sf;

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
