package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.Authentication;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "user")
public class User implements Authentication {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    @NotNull
    String username;

    @NonNull
    @NotNull
    String password;

    @NonNull
    @NotNull
    @Column(name = "creationDate", columnDefinition = "TIME WITH TIME ZONE")
    OffsetDateTime creationDate;

    @ManyToMany
    @NonNull
    Set<Role> authorities;

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

}
