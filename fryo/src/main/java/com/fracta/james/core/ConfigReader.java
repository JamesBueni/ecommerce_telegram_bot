package com.fracta.james.core;

import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	private static final ConfigReader INSTANCE = new ConfigReader();
	
	private final Properties properties;
	
	private ConfigReader() {
		properties = new Properties();
		this.load();
	}
	
	public static ConfigReader getInstance() {
		return INSTANCE;
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}
	
	private void load() {
		try (var stream = ConfigReader.class.getClassLoader().getResourceAsStream("application.properties")) {
			properties.load(stream);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load the config file.");
		}
	}
}