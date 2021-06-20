package com.fracta.james.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fracta.james.domain.entities.Message;
import com.fracta.james.repositories.MessageRepo;
import com.fracta.james.repositories.impl.MessageRepoDefault;
import com.fracta.james.services.MessageService;
import com.fracta.james.utils.ClonerUtils;

public class MessageServiceCached implements MessageService {

	private static final MessageService INSTANCE = new MessageServiceCached();
	private final MessageRepo repo = new MessageRepoDefault();
	
	private final Map<String, Message> cachedMessages = new HashMap<>();
	private final ScheduledExecutorService cachedService = Executors.newSingleThreadScheduledExecutor();
	
	public MessageServiceCached() {
	}
	
	public static MessageService getInstance() {
		return INSTANCE;
	}
	
	public void startCacheClearTask() {
		cachedService.scheduleAtFixedRate(cachedMessages::clear, 20, 20, TimeUnit.MINUTES);
	}

	@Override
	public Message findByName(String messageName) {
		if (messageName == null)
			throw new IllegalArgumentException("messageName shouldn't be null");
		
		var msg = cachedMessages.get(messageName);
		if (msg == null) {
			msg = repo.findByName(messageName);
			cachedMessages.put(messageName, msg);
		}
		return Message.newInstanceOf(msg);
	}
}