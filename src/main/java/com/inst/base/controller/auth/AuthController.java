package com.inst.base.controller.auth;

import com.inst.base.dto.user.UserDTO;
import com.inst.base.request.auth.BaseAuthRequest;
import com.inst.base.request.auth.RefreshTokenRequest;
import com.inst.base.request.auth.SignInRequest;
import com.inst.base.response.auth.BaseAuthResponse;
import com.inst.base.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseAuthResponse> authUser(
            @Valid
            @RequestBody
                    BaseAuthRequest request) {
        return new ResponseEntity<>(authService.baseAuth(request), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseAuthResponse> refreshToken(
            @Valid
            @RequestBody
                    RefreshTokenRequest request) {
        return new ResponseEntity<>(authService.refreshToken(request), HttpStatus.OK);
    }


    @PostMapping("/signin")
    public ResponseEntity<UserDTO> createUser(
            @Valid
            @RequestBody
                    SignInRequest request) {
        return new ResponseEntity<>(new UserDTO(authService.signIn(request)), HttpStatus.CREATED);
    }

}
