package com.fracta.james.handlers;

public interface Handler<T> {
	
	void handle(T t);
}
