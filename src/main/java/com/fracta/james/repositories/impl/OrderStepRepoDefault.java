package com.fracta.james.repositories.impl;

import java.util.HashMap;
import java.util.Map;

import com.fracta.james.commands.Command;
import com.fracta.james.commands.impl.OrderCreateCommand;
import com.fracta.james.commands.impl.OrderEnterAddressCommand;
import com.fracta.james.commands.impl.OrderEnterCityCommand;
import com.fracta.james.commands.impl.OrderEnterNameCommand;
import com.fracta.james.commands.impl.OrderEnterPhoneCommand;
import com.fracta.james.commands.impl.OrderProcessCommand;
import com.fracta.james.domain.entities.Order;
import com.fracta.james.repositories.OrderStepRepo;
import com.fracta.james.utils.ClonerUtils;

public class OrderStepRepoDefault implements OrderStepRepo {

	private final Map<Long, Integer> orderStepNumbers = new HashMap<>();
	private final Map<Long, Command<Long>> orderSteps = new HashMap<>();
	
	private final Map<Long, Order> cachedOrders = new HashMap<>();
	
	@Override
	public void setOrderStepNumber(long chatId, int orderStep) {
		orderStepNumbers.put(chatId, orderStep);
	}

	@Override
	public int findOrderStepNumberByChatId(long chatId) {
		return orderStepNumbers.getOrDefault(chatId, 0);
	}

	@Override
	public Map<Long, Command<Long>> getOrderSteps() {
		if (orderSteps.isEmpty()) {
			orderSteps.put(1L, OrderProcessCommand.getInstance());
			orderSteps.put(2L, OrderEnterNameCommand.getInstance());
			orderSteps.put(3L, OrderEnterPhoneCommand.getInstance());
			orderSteps.put(4L, OrderEnterCityCommand.getInstance());
			orderSteps.put(5L, OrderEnterAddressCommand.getInstance());
			orderSteps.put(6L, OrderCreateCommand.getInstance());
		}
		return orderSteps;
	}

	@Override
	public Order findCachedOrderByChatId(long chatId) {
		return ClonerUtils.cloneObject(cachedOrders.get(chatId));
	}

	@Override
	public void saveCachedOrder(long chatId, Order order) {
		cachedOrders.put(chatId, ClonerUtils.cloneObject(order));		
	}

	@Override
	public void updateCachedOrder(long chatId, Order order) {
		cachedOrders.put(chatId, ClonerUtils.cloneObject(order));
	}

	@Override
	public void deleteCachedOrderByChatId(long chatId) {
		cachedOrders.remove(chatId);
		
	}

}
