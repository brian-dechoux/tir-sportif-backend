package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetCategoryResponse;
import com.tirsportif.backend.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public GetCategoryResponse mapClubToResponse(Category category) {
        return new GetCategoryResponse(
                category.getId(),
                category.getLabel(),
                category.getCode(),
                category.getGender(),
                category.getAgeMin(),
                category.getAgeMax(),
                category.getGccMax()
        );
    }

}
