package com.inst.base.service.auth;

import com.inst.base.entity.user.User;
import com.inst.base.request.auth.*;
import com.inst.base.response.auth.BaseAuthResponse;
import com.inst.base.response.auth.StartEmailConfirmationResponse;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    StartEmailConfirmationResponse startEmailConfirmation(StartEmailConfirmationRequest request);

    Boolean submitEmailConfirmation(SubmitEmailConfirmationRequest request);

    User signInByEmailConfirmation(SignInByEmailConfirmationRequest request);

    BaseAuthResponse baseAuth(BaseAuthRequest request);

    BaseAuthResponse emailAuth(EmailAuthRequest request);

    User signIn(SignInRequest request);

    BaseAuthResponse refreshToken(RefreshTokenRequest request);

    Boolean validate(ValidateTokenRequest request);
}
