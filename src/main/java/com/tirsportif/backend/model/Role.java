package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    @NotNull
    @Enumerated(value = EnumType.STRING)
    Authority authority;

    @Override
    public String getAuthority() {
        return authority.name();
    }

}
