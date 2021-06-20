package com.fracta.james.domain.models;

import java.util.Objects;

import com.fracta.james.domain.entities.Product;

public class CartItem {
	
	private long id;
	private Product product;
	private int quantity;
	
	public CartItem() {
	}
	public CartItem(long id, Product product, int quantity) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public float getTotalPrice() {
		return product.getPrice() * quantity;
	}
	
	public static CartItem newInstanceOf(CartItem cartItem) {
		return new CartItem(cartItem.id, cartItem.product, cartItem.quantity);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, product, quantity);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (CartItem) o;
		return Objects.equals(id, other.id)
				&& Objects.equals(product, other.product)
				&& Objects.equals(quantity, other.quantity);
	}
	
	@Override
	public String toString() {
		return "CartItem{id=" + id
				+ ", product=" + product
				+ ", quantity=" + quantity
				+ "}";
	}
}
