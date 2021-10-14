package com.fracta.james.repositories;

import java.util.List;

import com.fracta.james.domain.entities.Category;

public interface CategoryRepo {
	
	List<Category> findAll();

}
