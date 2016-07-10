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
