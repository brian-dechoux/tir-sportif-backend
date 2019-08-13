package com.tirsportif.backend.service;

import com.tirsportif.backend.model.User;
import com.tirsportif.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("BEGIN UserService.loadUserByUsername(Username: {})", username);
        log.info("Searching user by Username: {}", username);
        // TODO Proper exception
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: "+username));
        log.debug("END UserService.loadUserByUsername");
        return user;
    }

}
