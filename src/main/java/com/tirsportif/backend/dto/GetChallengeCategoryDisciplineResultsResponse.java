package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class GetChallengeCategoryDisciplineResultsResponse {

    long categoryId;

    String categoryLabel;

    long disciplineId;

    String disciplineLabel;

    List<GetChallengeCategoryDisciplineResultResponse> results;

}
