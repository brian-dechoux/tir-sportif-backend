package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.GetCategoryResponse;
import com.tirsportif.backend.mapper.CategoryMapper;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CategoryService extends AbstractService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryService(ApiProperties apiProperties, CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        super(apiProperties);
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<GetCategoryResponse> getCategories() {
        log.info("Looking for all categories");
        List<GetCategoryResponse> categories = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(categoryMapper::mapCategoryToResponse)
                .collect(Collectors.toList());
        log.info("Found {} categories", categories.size());
        return categories;
    }

}
