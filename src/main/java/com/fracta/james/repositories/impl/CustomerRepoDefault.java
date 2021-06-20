package com.fracta.james.repositories.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.fracta.james.domain.entities.Customer;
import com.fracta.james.domain.models.CustomerAction;
import com.fracta.james.repositories.CustomerRepo;

public class CustomerRepoDefault implements CustomerRepo {

	private final SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
	private final Map<Long, CustomerAction> customerActions = new HashMap<>();
	
	@Override
	public void update(Customer customer) {
		var session = sessionFactory.openSession();
		session.update(customer);
		var tx = session.beginTransaction();
		tx.commit();
		session.close();
	}

	@Override
	public void save(Customer customer) {
		var session = sessionFactory.openSession();
		session.save(customer);
		var tx = session.beginTransaction();
		tx.commit();
		session.close();	
	}

	@Override
	public String findActionByChatId(long chatId) {
		var customerAction = customerActions.get(chatId);
		if (customerAction != null && LocalDateTime.now().isAfter(customerAction.getCreationDateTime().plusMinutes(5)))
			customerAction = null;
		return customerAction == null? null : customerAction.getAction();
	}

	@Override
	public void setActionForChatId(long chatId, String action) {
		customerActions.put(chatId, new CustomerAction(LocalDateTime.now(), action));
	}

	@Override
	public Customer findByChatId(long chatId) {
		var session = sessionFactory.openSession();
		var customer = (Customer) session.createQuery("from Customer where chatId = :chatId")
				.setParameter("chatId", chatId);
		session.close();
		return customer;
	}
	

}
