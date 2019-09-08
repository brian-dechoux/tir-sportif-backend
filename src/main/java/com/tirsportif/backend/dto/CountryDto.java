package com.tirsportif.backend.dto;

import com.tirsportif.backend.utils.Regexes;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CountryDto {

    @NotNull
    @Pattern(regexp = Regexes.COUNTRY_CODE_ALPHA2)
    String value;

}
