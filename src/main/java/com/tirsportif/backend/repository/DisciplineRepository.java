package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Discipline;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DisciplineRepository extends PagingAndSortingRepository<Discipline,Long> {

}
