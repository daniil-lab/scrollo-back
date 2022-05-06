package com.inst.base.service.user;

import com.inst.base.dto.post.PostDTO;
import com.inst.base.entity.user.User;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.auth.SignInRequest;
import com.inst.base.request.user.*;
import com.inst.base.util.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    User updateUser(UpdateUserRequest request);

    User getUserByLogin(String login);

    User getUserByEmail(String email);

    User getUserByPhone(String phone);

    User getUserInfo();

    User getUserById(UUID id);

    User updateAvatar(UpdateUserAvatarRequest request);

    User updateBackground(UpdateUserBackgroundRequest request);

    User removeAvatar();

    User removeBackground();

    Boolean changeUserPassword(ChangeUserPasswordRequest request);

    PageResponse<PostDTO> getSavedPosts(PageRequestParams params);

    Boolean checkEmail(String email);

    Boolean checkLogin(String login);

    Boolean checkPhone(String phone);

    List<User> findByLoginAndName(String data);

    Boolean updateUserGeo(UpdateUserGeoRequest request);
}
