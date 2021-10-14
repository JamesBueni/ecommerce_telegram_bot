package com.fracta.james.services;

import com.fracta.james.domain.entities.Customer;

public interface CustomerService {
	
	void save(Customer customer);
	void update(Customer customer);
	Customer findByChatId(long chatId);
	
	void setActionForChatId(long chatId, String action);
	String findActionByChatId(long chatId);
}
