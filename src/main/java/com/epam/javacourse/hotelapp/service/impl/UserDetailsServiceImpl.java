package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service which is responsible for fetching the details of the user from a database
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param email User's email
     * @return a UserDetails object populated with the user data fetched from the db.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            logger.error("No user found with email: {}", email);
            throw new UsernameNotFoundException("No user found with email: " + email);
        }

        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), roles);
    }
}
