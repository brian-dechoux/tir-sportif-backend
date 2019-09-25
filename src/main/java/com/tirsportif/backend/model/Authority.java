package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity(name = "authority")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    @Enumerated(value = EnumType.STRING)
    AuthorityType label;

    public String getAuthority() {
        return label.name();
    }

}
