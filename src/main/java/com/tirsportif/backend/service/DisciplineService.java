package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.GetDisciplineResponse;
import com.tirsportif.backend.mapper.DisciplineMapper;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.DisciplineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class DisciplineService extends AbstractService {

    private final DisciplineMapper disciplineMapper;
    private final DisciplineRepository disciplineRepository;

    public DisciplineService(ApiProperties apiProperties, DisciplineMapper disciplineMapper, DisciplineRepository disciplineRepository) {
        super(apiProperties);
        this.disciplineMapper = disciplineMapper;
        this.disciplineRepository = disciplineRepository;
    }

    public List<GetDisciplineResponse> getDisciplines() {
        log.info("Looking for all disciplines");
        List<GetDisciplineResponse> disciplines = StreamSupport.stream(disciplineRepository.findAll().spliterator(), false)
                .map(disciplineMapper::mapDisciplineToResponse)
                .collect(Collectors.toList());
        log.info("Found {} disciplines", disciplines.size());
        return disciplines;
    }

}
