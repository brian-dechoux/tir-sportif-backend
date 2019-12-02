package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GetShooterResultsRequest {

    /**
     * Might be a firstname, a lastname, or full name.
     */
    @NotEmpty
    String shooterName;

}
