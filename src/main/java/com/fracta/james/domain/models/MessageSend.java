package com.fracta.james.domain.models;

import java.util.Objects;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class MessageSend {
	
	private final long chatId;
	private final String text;
	private final ReplyKeyboard keyboard;
	
	public MessageSend(long chatId, String text, ReplyKeyboard keyboard) {
		this.chatId = chatId;
		this.text = text;
		this.keyboard = keyboard;
	}
	
	public MessageSend(long chatId, String text) {
		this.chatId = chatId;
		this.text = text;
		this.keyboard = null;
	}

	public long getChatId() {
		return chatId;
	}

	public String getText() {
		return text;
	}

	public ReplyKeyboard getKeyboard() {
		return keyboard;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chatId, text, keyboard);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (MessageSend) o;
		return Objects.equals(chatId, other.chatId)
				&& Objects.equals(text, other.text)
				&& Objects.equals(keyboard, other.keyboard);
	}

	@Override
	public String toString() {
		return "MessageSend{chatId=" + chatId
				+ ", text=" + text
				+ ", keyboard=" + keyboard
				+ "}";
	}
}
