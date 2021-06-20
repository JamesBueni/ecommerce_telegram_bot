package com.fracta.james.repositories;

import com.fracta.james.domain.entities.Order;

public interface OrderRepo {

	void save(Order order);
}
