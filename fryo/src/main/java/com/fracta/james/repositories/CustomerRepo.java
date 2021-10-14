package com.fracta.james.repositories;

import com.fracta.james.domain.entities.Customer;

public interface CustomerRepo {

	void update(Customer customer);
	void save(Customer customer);
	
	String findActionByChatId(long chatId);
	void setActionForChatId(long chatId, String action);
	Customer findByChatId(long chatId);
}
