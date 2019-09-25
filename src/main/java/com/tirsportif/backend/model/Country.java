package com.tirsportif.backend.model;

import com.tirsportif.backend.utils.Regexes;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Entity(name = "country")
public class Country {

    @Id
    Long id;

    @NonNull
    @NotNull
    @Pattern(regexp = Regexes.COUNTRY_CODE_ALPHA2)
    String code;

    @NonNull
    @NotNull
    @NotEmpty
    String name;

}
