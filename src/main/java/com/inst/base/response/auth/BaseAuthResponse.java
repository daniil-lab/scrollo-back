package com.inst.base.response.auth;

import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseAuthResponse {
    private String token;

    private String refreshToken;

    private UserDTO user;

    public BaseAuthResponse(String token, String refreshToken, User user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = new UserDTO(user);
    }
}
