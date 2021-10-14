package com.fracta.james.services;

import com.fracta.james.domain.entities.Order;

public interface NotificationService {
	
	void notifyAdminChatAboutNewOrder(Order order);
}
