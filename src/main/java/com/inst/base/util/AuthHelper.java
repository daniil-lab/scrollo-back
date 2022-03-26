package com.inst.base.util;

import com.inst.base.config.AuthCredentials;
import com.inst.base.entity.user.User;
import com.inst.base.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {
    @Autowired
    private UserRepository userRepository;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public AuthCredentials getAuthCredentials() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof AuthCredentials) {
            return (AuthCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return null;
    }

    public User getUserFromAuthCredentials() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof AuthCredentials) {
            return userRepository.findById(((AuthCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElseThrow(() -> {
                throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
            });
        }

        throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
    }
}
