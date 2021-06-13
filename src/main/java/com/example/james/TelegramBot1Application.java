package com.example.james;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.james.model.BasicAbilityBot;

@SpringBootApplication
public class TelegramBot1Application {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBot1Application.class, args);
		
		try {
			var botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(new BasicAbilityBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		
	}

}
