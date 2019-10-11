package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetDisciplineResponse;
import com.tirsportif.backend.model.Discipline;
import org.springframework.stereotype.Component;

@Component
public class DisciplineMapper {

    public GetDisciplineResponse mapDisciplineToResponse(Discipline discipline) {
        return new GetDisciplineResponse(
                discipline.getId(),
                discipline.getLabel(),
                discipline.getCode(),
                discipline.isDecimalResult()
        );
    }

}
