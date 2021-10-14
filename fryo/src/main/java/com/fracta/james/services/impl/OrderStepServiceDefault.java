package com.fracta.james.services.impl;

import com.fracta.james.domain.entities.Order;
import com.fracta.james.exceptions.ValidationException;
import com.fracta.james.repositories.OrderStepRepo;
import com.fracta.james.repositories.impl.OrderStepRepoDefault;
import com.fracta.james.services.OrderStepService;

public class OrderStepServiceDefault implements OrderStepService {

	private static final OrderStepService INSTANCE = new OrderStepServiceDefault();
	private final OrderStepRepo repo = new OrderStepRepoDefault();
	
	
	public OrderStepServiceDefault() {
	}
	
	public static OrderStepService getInstance() {
		return INSTANCE;
	}

	@Override
	public Order findCachedOrderByChatId(long chatId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("Customer's chatId shouldn't be null.");
		return repo.findCachedOrderByChatId(chatId);
	}

	@Override
	public void saveCachedOrder(long chatId, Order order) {
		if (order == null)
			throw new IllegalArgumentException("Order shouldn't be null.");
		if (order.getCustomer() == null)
			throw new ValidationException("Customer shouldn't be null.");
		repo.saveCachedOrder(chatId, order);
	}

	@Override
	public void updateCachedOrder(long chatId, Order order) {
		if (order == null)
			throw new IllegalArgumentException("Order shouldn't be null.");
		if (order.getCustomer() == null)
			throw new ValidationException("Customer shouldn't be null.");
		repo.updateCachedOrder(chatId, order);
		
	}

	@Override
	public void deleteCachedOrderByChatId(long chatId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("ChatId shouldn't be null.");
		
		repo.deleteCachedOrderByChatId(chatId);
		
	}

	@Override
	public void previousStepOrder(long chatId) {
		var orderStep = repo.findOrderStepNumberByChatId(chatId);
		if (orderStep > 1) {
			orderStep--;
			repo.setOrderStepNumber(chatId, orderStep);
			repo.getOrderSteps().get(orderStep).execute(chatId);
		}	
	}

	@Override
	public void nextStepOrder(long chatId) {
		var orderStep = repo.findOrderStepNumberByChatId(chatId);
		if (orderStep < repo.getOrderSteps().size()) {
			orderStep++;
			repo.setOrderStepNumber(chatId, orderStep);
			repo.getOrderSteps().get(orderStep).execute(chatId);
		}
	}

	@Override
	public void revokeStepOrder(long chatId) {
		repo.setOrderStepNumber(chatId, 0);
	}
}