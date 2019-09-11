package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Shooter;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Long> {

}
