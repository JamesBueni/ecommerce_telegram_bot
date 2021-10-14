package com.fracta.james.core;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fracta.james.handlers.Handler;
import com.fracta.james.handlers.impl.UpdateHandler;

public class FryoTelegramBot extends TelegramLongPollingBot {

	private final Handler<Update> updateHandler = new UpdateHandler();
	
	private static final ConfigReader CONFIG = ConfigReader.getInstance();
	public static final String TELEGRAM_BOT_TOKEN = CONFIG.getProperty("telegram.bot.token");
	public static final String TELEGRAM_BOT_USERNAME = CONFIG.getProperty("telegram.bot.username");
	
	
	public FryoTelegramBot() {
	}

	@Override
	public void onUpdateReceived(Update update) {
		updateHandler.handle(update);
	}

	@Override
	public String getBotUsername() {
		return TELEGRAM_BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return TELEGRAM_BOT_TOKEN;
	}
}