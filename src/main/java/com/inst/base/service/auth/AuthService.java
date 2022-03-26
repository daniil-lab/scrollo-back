package com.inst.base.service.auth;

import com.inst.base.entity.user.User;
import com.inst.base.request.auth.BaseAuthRequest;
import com.inst.base.request.auth.RefreshTokenRequest;
import com.inst.base.request.auth.SignInRequest;
import com.inst.base.response.auth.BaseAuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    BaseAuthResponse baseAuth(BaseAuthRequest request);

    User signIn(SignInRequest request);

    BaseAuthResponse refreshToken(RefreshTokenRequest request);
}
