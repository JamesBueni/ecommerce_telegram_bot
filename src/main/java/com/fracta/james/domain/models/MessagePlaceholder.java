package com.fracta.james.domain.models;

import java.util.Objects;

public class MessagePlaceholder {

	private final String placeholder;
	private final Object replacement;
	
	private MessagePlaceholder(String placeholder, Object replacement) {
		this.placeholder = placeholder;
		this.replacement = replacement;
	}
	
	public static MessagePlaceholder of(String placeholder, Object replacement) {
		return new MessagePlaceholder(placeholder, replacement);
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public Object getReplacement() {
		return replacement;
	}

	@Override
	public int hashCode() {
		return Objects.hash(placeholder, replacement);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (MessagePlaceholder) o;
		return Objects.equals(placeholder, other.placeholder)
				&& Objects.equals(replacement, other.replacement);
	}

	@Override
	public String toString() {
		return "MessagePlaceholder{placeholder=" + placeholder
				+ ", replacement=" + replacement
				+ "}";
	}
	
	
	
}
