package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);

}
