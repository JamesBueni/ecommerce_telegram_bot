package com.fracta.james.repositories;

import com.fracta.james.domain.entities.Message;

public interface MessageRepo {

	Message findByName(String messageName);
}
