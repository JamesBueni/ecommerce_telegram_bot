package com.fracta.james.domain.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_items_seq")
	@SequenceGenerator(name = "order_items_seq", sequenceName = "order_items_id_seq", allocationSize = 1)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	@Column(nullable = false)
	private int quantity;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id" ,nullable = false)
	private Product product;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "product_price", nullable = false)
	private float productPrice;

	public OrderItem() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public float getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, order, quantity, product, productName, productPrice);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (OrderItem) o;
		return Objects.equals(id, other.id)
				&& Objects.equals(order, other.order)
				&& Objects.equals(quantity, other.quantity)
				&& Objects.equals(product, other.product)
				&& Objects.equals(productName, other.productName)
				&& Objects.equals(productPrice, other.productPrice);
	}

	@Override
	public String toString() {
		return "OrderItem{id=" + id 
				+ ", order=" + order
				+ ", quantity=" + quantity
				+ ", product=" + product
				+ ", productName=" + productName
				+ ", productPrice=" + productPrice
				+ "}";
	}
}
