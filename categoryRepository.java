package com.vnrit.mykidsdrawing.repository;

import org.springframework.data.jpa.repository.Query;

import com.vnrit.mykidsdrawing.model.category;

public interface categoryRepository<category,Long> {

	void save(category category);

	com.vnrit.mykidsdrawing.model.category getOne(java.lang.Long subcategoryid);
}
