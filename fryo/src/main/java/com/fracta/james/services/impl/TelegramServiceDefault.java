package com.fracta.james.services.impl;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.fracta.james.core.FryoTelegramBot;
import com.fracta.james.domain.models.InlineQuerySend;
import com.fracta.james.domain.models.MessageEdit;
import com.fracta.james.domain.models.MessageSend;
import com.fracta.james.exceptions.FailedSendMessageException;
import com.fracta.james.services.TelegramService;

public class TelegramServiceDefault extends DefaultAbsSender implements TelegramService {

	private static final TelegramService INSTANCE = new TelegramServiceDefault();
	
	public TelegramServiceDefault() {
		super(new DefaultBotOptions());
	}

	public static TelegramService getInstance() {
		return INSTANCE;
	}

	@Override
	public void sendMessage(MessageSend messageSend) {
		var sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(messageSend.getChatId()));
		sendMessage.setText(messageSend.getText());
		sendMessage.setParseMode("HTML");
		if (messageSend.getKeyboard() != null)
			sendMessage.setReplyMarkup(messageSend.getKeyboard());
		
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			throw new FailedSendMessageException("Failed to send txt message: " + messageSend, e);
		}
	}

	@Override
	public void editMessageText(MessageEdit messageEdit) {
		var editMessageText = new EditMessageText();
		if (Long.valueOf(messageEdit.getMessageId()) != null) {
			editMessageText.setChatId(String.valueOf(messageEdit.getChatId()));
			editMessageText.setMessageId((int) messageEdit.getMessageId());
		}
		else
			editMessageText.setInlineMessageId(messageEdit.getInlineMessageId());
		
		editMessageText.setText(messageEdit.getText());
		editMessageText.setParseMode("HTML");
		if (messageEdit.getKeyboard() != null)
			editMessageText.setReplyMarkup(messageEdit.getKeyboard());
		
		try {
			execute(editMessageText);
		} catch (TelegramApiException e) {
			throw new FailedSendMessageException("Failed to edit txt message: " + messageEdit, e);
		}
	}

	@Override
	public void sendInlineQuery(InlineQuerySend inlineQuerySend) {
		var answerInlineQuery = new AnswerInlineQuery();
		answerInlineQuery.setInlineQueryId(Long.toString(inlineQuerySend.getId()));
		answerInlineQuery.setResults(inlineQuerySend.getResults());
		answerInlineQuery.setCacheTime(1);
		if (inlineQuerySend.getOffset() != null)
			answerInlineQuery.setNextOffset(inlineQuerySend.getOffset());

		try {
			execute(answerInlineQuery);
		} catch (TelegramApiException e) {
			throw new FailedSendMessageException("Failed to send inline query:" + inlineQuerySend, e);
		}
	}

	@Override
	public String getBotToken() {
		return FryoTelegramBot.TELEGRAM_BOT_TOKEN;
	}

}
