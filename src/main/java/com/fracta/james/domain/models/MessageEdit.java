package com.fracta.james.domain.models;

import java.util.Objects;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class MessageEdit {
	
	private final long messageId;
	private final long chatId;
	private final String inlineMessageId;
	private final String text;
	private final InlineKeyboardMarkup keyboard;
	
	public MessageEdit(long messageId, long chatId, String text) {
		this.messageId = messageId;
		this.chatId = chatId;
		this.inlineMessageId = null;
		this.text = text;
		this.keyboard = null;
	}
	
	public MessageEdit(long messageId, long chatId, String text, InlineKeyboardMarkup keyboard) {
		this.messageId = messageId;
		this.chatId = chatId;
		this.inlineMessageId = null;
		this.text = text;
		this.keyboard = keyboard;
	}
	
	public MessageEdit(String text, String inlineMessageId, InlineKeyboardMarkup keyboard) {
		this.messageId = (Long) null;
		this.chatId = (Long) null;
		this.inlineMessageId = inlineMessageId;
		this.text = text;
		this.keyboard = keyboard;
	}

	public long getMessageId() {
		return messageId;
	}

	public long getChatId() {
		return chatId;
	}

	public String getInlineMessageId() {
		return inlineMessageId;
	}

	public String getText() {
		return text;
	}

	public InlineKeyboardMarkup getKeyboard() {
		return keyboard;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId, chatId, inlineMessageId, text, keyboard);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (MessageEdit) o;
		return Objects.equals(messageId, other.messageId)
				&& Objects.equals(chatId, other.chatId)
				&& Objects.equals(inlineMessageId, other.inlineMessageId)
				&& Objects.equals(text, other.text)
				&& Objects.equals(keyboard, other.keyboard);
	}

	@Override
	public String toString() {
		return "MessageEdit{messageId=" + messageId
				+ ", chatId=" + chatId
				+ ", inlineMessageId=" + inlineMessageId
				+ ", text=" + text
				+ ", keyboard=" + keyboard
				+ "}";
	}
	
	
	

}
