package org.ares.cdi.hibernate.sf;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


/**
 * Build a single instance of the {@link SessionFactory} for
 * injection to implementations of {@link SessionFactoryTemplate}
 * @author Vivek
 */
@Singleton
public class SessionFactoryBuilder {

	private SessionFactory sessionFactory;
	
	@PostConstruct
	void buildSessionFactory(){
		final StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder().configure().build();
		try{
			Metadata metadata = new MetadataSources(standardServiceRegistry).buildMetadata();
			sessionFactory = metadata.buildSessionFactory();
		}
		catch(Exception e){
			StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}
	
	
	@Produces
	public SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	@PreDestroy
	void destroySessionFactory(){
		sessionFactory.close();
	}
	
}
