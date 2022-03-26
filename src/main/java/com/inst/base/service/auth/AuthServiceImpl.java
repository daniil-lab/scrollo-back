package com.inst.base.service.auth;

import com.inst.base.config.JwtProvider;
import com.inst.base.config.RefreshJwtProvider;
import com.inst.base.entity.user.User;
import com.inst.base.repository.user.UserRepository;
import com.inst.base.request.auth.BaseAuthRequest;
import com.inst.base.request.auth.RefreshTokenRequest;
import com.inst.base.request.auth.SignInRequest;
import com.inst.base.response.auth.BaseAuthResponse;
import com.inst.base.util.PasswordValidator;
import com.inst.base.util.ServiceException;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2(topic = "Auth Service")
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RefreshJwtProvider refreshJwtProvider;

    @Override
    public User signIn(SignInRequest request) {
        userRepository.findByLogin(request.getLogin()).ifPresent((val) -> {
            throw new ServiceException("User with given login already exists", HttpStatus.BAD_REQUEST);
        });

        userRepository.findByEmailDataEmail(request.getEmail()).ifPresent((val) -> {
            throw new ServiceException("User with given login already exists", HttpStatus.BAD_REQUEST);
        });

        User user = new User(request.getLogin(), passwordEncoder.encode(PasswordValidator.decodeAndValidatePassword(request.getPassword())));

        user.getEmailData().setEmail(request.getEmail());
        user.getPersonalInformation().setName(request.getName());

        userRepository.save(user);

        log.info("Registered new user with login: " + user.getLogin() + " and ID " + user.getId());

        return user;
    }

    @Override
    public BaseAuthResponse refreshToken(RefreshTokenRequest request) {
        refreshJwtProvider.validateToken(request.getRefreshToken());

        Claims tokenData = refreshJwtProvider.getDataFromToken(request.getRefreshToken());

         User user = userRepository.findById(UUID.fromString(tokenData.get("id", String.class))).orElseThrow(() -> {
            throw new ServiceException("Token invalid", HttpStatus.BAD_REQUEST);
         });

        return new BaseAuthResponse(jwtProvider.generateToken(user.getLogin(), user.getId()), refreshJwtProvider.generateToken(user.getLogin(), user.getId()), user);
    }

    @Override
    public BaseAuthResponse baseAuth(BaseAuthRequest request) {
        User user = userRepository.findByLogin(request.getLogin()).orElseThrow(() -> {
            throw new ServiceException("Check auth data", HttpStatus.BAD_REQUEST);
        });

        if(!passwordEncoder.matches(PasswordValidator.decodePassword(request.getPassword()), user.getPassword()))
            throw new ServiceException("Check auth data", HttpStatus.BAD_REQUEST);

        return new BaseAuthResponse(jwtProvider.generateToken(user.getLogin(), user.getId()), refreshJwtProvider.generateToken(user.getLogin(), user.getId()), user);
    }
}
