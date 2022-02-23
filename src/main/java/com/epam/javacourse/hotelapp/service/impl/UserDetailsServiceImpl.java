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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

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

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findUserByEmail(email);
//        if (user == null) {
//            logger.error("No user found with email: {}", email);
//            throw new UsernameNotFoundException("No user found with email: " + email);
//        }
//        List<SimpleGrantedAuthority> grantedAuthorities = user
//                .getAuthorities()
//                .map(authority -> new SimpleGrantedAuthority(authority))
//                .collect(Collectors.toList()); // (1)
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
//    }



//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findUserByEmail(email);
//        if (user == null) {
//            logger.error("No user found with email: {}", email);
//            throw new UsernameNotFoundException("No user found with email: " + email);
//        }
//        boolean enabled = true;
//        boolean accountNonExpired = true;
//        boolean credentialsNonExpired = true;
//        boolean accountNonLocked = true;
//
//        Set<GrantedAuthority> roles = new HashSet<>();
//        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(), user.getPassword().toLowerCase(), enabled, accountNonExpired,
//                credentialsNonExpired, accountNonLocked, roles);
//    }
}
