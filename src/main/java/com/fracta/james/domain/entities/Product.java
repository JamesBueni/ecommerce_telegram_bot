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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
	@SequenceGenerator(name = "products_seq", sequenceName = "products_id_seq", allocationSize = 1)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@Column(name = "photo_url", nullable = false)
	private String photoUrl;
	
	@Column(nullable = false)
	private String name;
	
	@Column(length = 1024, nullable = false)
	private String desc;

	@Column(nullable = false)
	private float price;

	public Product() {
		
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, category, photoUrl, name, desc, price);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (Product) o;
		return Objects.equals(id, other.id)
				&& Objects.equals(category, other.category)
				&& photoUrl == other.photoUrl
				&& name == other.name
				&& desc == other.desc
				&& Objects.equals(price, other.price);
	}

	@Override
	public String toString() {
		return "Product{id=" + id 
				+ ", category=" + category
				+ ", photoUrl=" + photoUrl
				+ ", name=" + name
				+ ", desc=" + desc 
				+ ", price=" + price
				+ "]";
	}
	
	
	
	
}
