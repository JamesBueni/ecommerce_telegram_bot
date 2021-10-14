package com.fracta.james.repositories.impl;

import org.hibernate.SessionFactory;

import com.fracta.james.domain.entities.Message;
import com.fracta.james.repositories.MessageRepo;

public class MessageRepoDefault implements MessageRepo {

	private final SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
	
	@Override
	public Message findByName(String messageName) {
		var session = sessionFactory.openSession();
		var msg = (Message) session.createQuery("from Message where name = :name").setParameter("name", messageName);
		session.close();
		return msg;
	}

}
