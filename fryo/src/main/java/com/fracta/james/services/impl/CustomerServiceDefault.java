package com.fracta.james.services.impl;

import com.fracta.james.domain.entities.Customer;
import com.fracta.james.exceptions.ValidationException;
import com.fracta.james.repositories.CustomerRepo;
import com.fracta.james.repositories.impl.CustomerRepoDefault;
import com.fracta.james.services.CustomerService;

public class CustomerServiceDefault implements CustomerService {

	private static final CustomerServiceDefault INSTANCE = new CustomerServiceDefault();
	private final CustomerRepo repo = new CustomerRepoDefault();
	
	public CustomerServiceDefault() {
	}

	public static CustomerServiceDefault getInstance() {
		return INSTANCE;
	}

	@Override
	public void save(Customer customer) {
		if (customer == null)
			throw new IllegalArgumentException("customer shouldn't be null");
		repo.save(customer);
	}

	@Override
	public void update(Customer customer) {
		if (customer == null)
			throw new IllegalArgumentException("customer shouldn't be null");
		if (Long.valueOf(customer.getChatId()) == null)
			throw new ValidationException("customer's chatId shouldn't be null");
		repo.update(customer);
	}

	@Override
	public Customer findByChatId(long chatId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		return repo.findByChatId(chatId);
	}

	@Override
	public void setActionForChatId(long chatId, String action) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		repo.setActionForChatId(chatId, action);
	}

	@Override
	public String findActionByChatId(long chatId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		return repo.findActionByChatId(chatId);
	}

}
