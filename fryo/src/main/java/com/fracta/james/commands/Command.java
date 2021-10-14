package com.fracta.james.commands;

public interface Command<T> {
	
	void execute(T t);
}
