package com.tirsportif.backend.model;

import lombok.NonNull;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Value
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    Authority authority;

    @Override
    public String getAuthority() {
        return authority.name();
    }

}
