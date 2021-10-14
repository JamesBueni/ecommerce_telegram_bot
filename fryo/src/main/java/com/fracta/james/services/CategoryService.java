package com.fracta.james.services;

import java.util.List;

import com.fracta.james.domain.entities.Category;

public interface CategoryService {
	
	List<Category> findAll();

}
