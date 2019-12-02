package com.tirsportif.backend.dto;

import lombok.Value;

import java.util.List;

@Value
public class GetCategoryAndDisciplineResultsResponse {

    List<CategoryAndDisciplineResultDto> results;

}
