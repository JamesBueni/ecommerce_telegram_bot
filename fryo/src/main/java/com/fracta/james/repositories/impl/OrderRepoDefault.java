package com.fracta.james.repositories.impl;

import org.hibernate.SessionFactory;

import com.fracta.james.domain.entities.Order;
import com.fracta.james.repositories.OrderRepo;

public class OrderRepoDefault implements OrderRepo {

	private final SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
	
	@Override
	public void save(Order order) {
		var session = sessionFactory.openSession();
		session.save(order);
		var tx = session.beginTransaction();
		tx.commit();
		session.close();
	}

}
