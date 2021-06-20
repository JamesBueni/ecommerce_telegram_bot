package com.fracta.james.services;

import com.fracta.james.domain.entities.Order;

public interface OrderStepService {
	
	void previousStepOrder(long chatId);
	void nextStepOrder(long chatId);
	void revokeStepOrder(long chatId);
	
	Order findCachedOrderByChatId(long chatId);
	void saveCachedOrder(long chatId, Order order);
	void updateCachedOrder(long chatId, Order order);
	void deleteCachedOrderByChatId(long chatId, Order order);
}