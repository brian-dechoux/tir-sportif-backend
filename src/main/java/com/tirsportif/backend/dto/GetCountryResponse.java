package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetCountryResponse {

    Long id;

    String code;

    String name;

}
