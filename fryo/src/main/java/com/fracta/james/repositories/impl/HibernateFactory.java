package com.fracta.james.repositories.impl;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.fracta.james.domain.entities.Category;
import com.fracta.james.domain.entities.Customer;
import com.fracta.james.domain.entities.Message;
import com.fracta.james.domain.entities.Order;
import com.fracta.james.domain.entities.OrderItem;
import com.fracta.james.domain.entities.OrderStatus;
import com.fracta.james.domain.entities.Product;

final class HibernateFactory {

	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
	
	public HibernateFactory() {
	}

	static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

	private static SessionFactory buildSessionFactory() {
		var config = new Configuration();
		config.configure();	
		configureClasses(config);
		return config.buildSessionFactory(createServiceRegistry(config));
	}

	private static ServiceRegistry createServiceRegistry(Configuration config) {
		var builder = new StandardServiceRegistryBuilder();
		builder.applySettings(config.getProperties());
		return builder.build();
	}

	private static void configureClasses(Configuration config) {
		config.addAnnotatedClass(Category.class);
		config.addAnnotatedClass(Customer.class);
		config.addAnnotatedClass(Message.class);
		config.addAnnotatedClass(Order.class);
		config.addAnnotatedClass(Product.class);
		config.addAnnotatedClass(OrderStatus.class);
		config.addAnnotatedClass(OrderItem.class);
	}
	
}
