package com.web.megamart.security;

import com.web.megamart.doa.UserRepository;
import com.web.megamart.entity.UserDetail;
import com.web.megamart.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserDetail user = userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByEmailOrMobileNumber(username, username)
                        .orElseThrow(() ->
                                new UserException("User not found with: " + username, HttpStatus.NOT_FOUND)));

        // Grant "USER" role to every user
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}
