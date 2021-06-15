package com.fracta.james.model;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
	
	private final String BOT_USERNAME = "FryoBot";
	private final String BOT_TOKEN = "1741357913:AAHpDPeJBeXkFuy4OGgCngE3m5ovxGhuPA0";

	@Override
	public void onUpdateReceived(Update update) {
		// Check if an update has a msg and the msg contains text
		if (update.hasMessage() && update.getMessage().hasText()) {
			var message = new SendMessage();
			message.setChatId(update.getMessage().getChatId().toString());
			message.setText(update.getMessage().getText());
			
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public String getBotUsername() {
		return BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}
	

}
