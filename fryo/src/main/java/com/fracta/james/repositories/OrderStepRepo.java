package com.fracta.james.repositories;

import java.util.Map;

import com.fracta.james.commands.Command;
import com.fracta.james.domain.entities.Order;

public interface OrderStepRepo {
	
	void setOrderStepNumber(long chatId, int orderStep);
	int findOrderStepNumberByChatId(long chatId);
	Map<Long, Command<Long>> getOrderSteps();
	
	Order findCachedOrderByChatId(long chatId);
	void saveCachedOrder(long chatId, Order order);
	void updateCachedOrder(long chatId, Order order);
	void deleteCachedOrderByChatId(long chatId);

}
