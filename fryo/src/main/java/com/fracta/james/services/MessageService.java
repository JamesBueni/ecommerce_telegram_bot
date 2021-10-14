package com.fracta.james.services;

import com.fracta.james.domain.entities.Message;

public interface MessageService {

	Message findByName(String messageName);
}
