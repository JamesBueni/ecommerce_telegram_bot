package com.fracta.james.domain.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
	@SequenceGenerator(name = "orders_seq", sequenceName = "orders_id_seq", allocationSize = 1)
	private long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@Column(nullable = false)
	private float amount;
	
	@Column(nullable = false)
	private LocalDateTime date;
	
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<OrderItem> items = new ArrayList<>();
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	public Order() {
		
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}

	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order{"
				+ "id=" + id 
				+ ", customer=" + customer
				+ ", amount=" + amount
				+ ", date=" + date
				+ ", items=" + items
				+ ", status=" + status
				+ "}";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, customer, amount, date, items, status);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (Order) o;
		return id == other.id
				&& Objects.equals(amount, other.amount)
				&& Objects.equals(date, other.date)
				&& Objects.equals(customer, other.customer)
				&& status == other.status
				&& Objects.equals(items, other.items);
	}
	
	
	

}
