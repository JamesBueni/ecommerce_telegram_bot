package com.fracta.james.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.common.base.Objects;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
	@SequenceGenerator(name = "categories_seq", sequenceName = "categories_id_seq", allocationSize = 1)
	private long id;
	
	@Column(nullable = false)
	private String name;

	public Category() {
	
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (Category) o;
		return Objects.equal(id, other.id)
				&& name == other.name;
	}

	@Override
	public String toString() {
		return "Category{id=" + id
				+ ", name=" + name
				+ "}";
	}
	
	
	
	
}
