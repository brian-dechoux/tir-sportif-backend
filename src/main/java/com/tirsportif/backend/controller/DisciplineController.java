package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.GetDisciplineResponse;
import com.tirsportif.backend.service.DisciplineService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/disciplines", produces = "application/json;charset=UTF-8")
public class DisciplineController {

    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public List<GetDisciplineResponse> getDisciplines() {
        return disciplineService.getDisciplines();
    }

    @GetMapping("/{disciplineId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetDisciplineResponse getDiscipline(@PathVariable Long disciplineId) {
        return disciplineService.getDiscipline(disciplineId);
    }

}
