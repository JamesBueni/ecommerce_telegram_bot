package com.fracta.james.domain.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_seq")
	@SequenceGenerator(name = "customers_seq", sequenceName = "customers_id_seq", allocationSize = 1)
	private long id;
	
	@Column(name = "chat_id", unique = true, nullable = false)
	private long chatId;
	
	@Column
	private String name;
	
	@Column
	private String phone;
	
	@Column
	private String city;
	
	@Column
	private String address;
	
	@Column(nullable = false)
	private boolean active;

	public Customer() {
		
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getChatId() {
		return chatId;
	}
	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, chatId, name, phone, city, address, active);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (Customer) o;
		return id == other.id
				&& Objects.equals(chatId, other.chatId)
				&& name == other.name
				&& phone == other.phone
				&& city == other.city
				&& address == other.address
				&& Objects.equals(active, other.active);
	}

	@Override
	public String toString() {
		return "Customer"
				+ "{id=" + id 
				+ ", chatId=" + chatId 
				+ ", name=" + name
				+ ", phone=" + phone
				+ ", city=" + city
				+ ", address=" + address
				+ ", active=" + active 
				+ "}";
	}
	
	
	
	
	
	
}
