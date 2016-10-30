# Hibernate with CDI (Contextual Dependency Injection)

#####Using CDI to reduce Hibernate boilerplate like session open / close and transaction begin / commit / rollback

This is an example to demonstrate how CDI can be used to reduce hibernate boilerplate. Since [Hibernate Sessions](http://docs.jboss.org/hibernate/orm/5.1/javadocs/org/hibernate/Session.html) are non thread safe, in most scenarios, Session per thread or Session per request is desired. This method creates a wrapper for [Hibernate SessionFactory](http://docs.jboss.org/hibernate/orm/5.1/javadocs/org/hibernate/SessionFactory.html) and provides most commonly used APIs. Using CDI, [`@RequestScoped`](http://docs.oracle.com/javaee/6/api/javax/enterprise/context/RequestScoped.html) Sessions are opned and closed automcatically. Transactions are managed using an Interceptor.

To use the API, create an implementation of [`SessionFactoryTemplate`](https://github.com/vivekkr12/cdi-hibernate/blob/master/src/main/java/org/ares/cdi/hibernate/sf/SessionFactoryTemplate.java).

```
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

@RequestScoped
public class MySessionFactory implements SessionFactoryTemplate{

	@Inject
	private SessionFactory sessionFactory;// Inject after creating the singleton instance
	
	private Session currentSession;
	
	public Session openSession(){
		return sessionFactory.openSession();
	}
	
	public Session getCurrentSession(){
		if(currentSession == null){
			currentSession = sessionFactory.openSession();
		}
		return currentSession;
	}

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
```
Current Session is initialized lazily when it is called for the first time. When the request scope ends, current session is closed if it was opened.

This imlementation is then injected in database layer and used to obtain sessions.

```
import org.ares.cdi.hibernate.sf.MySessionFactory;
import org.ares.cdi.hibernate.interceptors.Transactional;

public class Dao {
  
  @Inject
  private MySessionFactory sf;
  
  public void get(int id){
    sf.getCurrentSession().get(clazz,id);
  }
  
  @Transactional
  public void add(Object entity){
    sf.getCurrentSesion().add(entity);
  }
}
```
Transactions are handled by `TransactionManager` interceptor

```
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.ares.cdi.hibernate.sf.MySessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

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
