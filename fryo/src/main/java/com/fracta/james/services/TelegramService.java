package com.fracta.james.services;

import com.fracta.james.domain.models.InlineQuerySend;
import com.fracta.james.domain.models.MessageEdit;
import com.fracta.james.domain.models.MessageSend;

public interface TelegramService {

	void sendMessage(MessageSend messageSend);
	void editMessageText(MessageEdit messageEdit);
	
	void sendInlineQuery(InlineQuerySend inlineQuerySend);

}
