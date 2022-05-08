package com.inst.base.service.user;

import com.inst.base.config.AuthCredentials;
import com.inst.base.dto.post.PostDTO;
import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.user.User;
import com.inst.base.entity.user.UserGeo;
import com.inst.base.repository.follow.FollowerRepository;
import com.inst.base.repository.user.UserRepository;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.auth.SignInRequest;
import com.inst.base.request.user.*;
import com.inst.base.service.FileStorageService;
import com.inst.base.util.AuthHelper;
import com.inst.base.util.PageResponse;
import com.inst.base.util.PasswordValidator;
import com.inst.base.util.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2(topic = "User Service")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private FollowerRepository followerRepository;

    @Override
    public User updateUser(UpdateUserRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        if(request.getBio() != null)
            user.getPersonalInformation().setBio(request.getBio());

        if(request.getEmail() != null && !request.getEmail().equals(user.getEmailData().getEmail())) {
            userRepository.findByEmailDataEmail(request.getEmail()).ifPresent((val) -> {
                throw new ServiceException("User with given email already exist", HttpStatus.BAD_REQUEST);
            });

            user.getEmailData().setEmail(request.getEmail());
        }

        if(request.getCareer() != null)
            user.setCareer(request.getCareer());

        if(request.getDirection() != null)
            user.setDirection(request.getDirection());

        if(request.getLogin() != null&& !request.getLogin().equals(user.getLogin())) {
            userRepository.findByLogin(request.getLogin()).ifPresent((val) -> {
                throw new ServiceException("User with given login already exist", HttpStatus.BAD_REQUEST);
            });

            user.setLogin(request.getLogin());
        }

        if(request.getGender() != null)
            user.getPersonalInformation().setGender(request.getGender());

        if(request.getName() != null)
            user.getPersonalInformation().setName(request.getName());

        if(request.getPhone() != null && !request.getPhone().equals(user.getPhoneData().getPhone())) {
            userRepository.findByPhoneDataPhone(request.getPhone()).ifPresent((val) -> {
                throw new ServiceException("User with given phone already exist", HttpStatus.BAD_REQUEST);
            });

            user.getPhoneData().setPhone(request.getPhone());
        }

        if(request.getWebsite() != null)
            user.getPersonalInformation().setWebsite(request.getWebsite());

        if(request.getAccountType() != null)
            user.setAccountType(request.getAccountType());

        userRepository.save(user);

        return user;
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> {
            throw new ServiceException("User with given login not found", HttpStatus.NOT_FOUND);
        });
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailDataEmail(email).orElseThrow(() -> {
            throw new ServiceException("User with given email not found", HttpStatus.NOT_FOUND);
        });
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepository.findByPhoneDataPhone(phone).orElseThrow(() -> {
            throw new ServiceException("User with given phone not found", HttpStatus.NOT_FOUND);
        });
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = authHelper.getUserFromAuthCredentials();

        User foundUser = userRepository.findById(id).orElseThrow(() -> {
            throw new ServiceException("User with given ID not found", HttpStatus.NOT_FOUND);
        });

        UserDTO dto = new UserDTO(foundUser);

        dto.setFollowOnHim(followerRepository.findByFollowerIdAndFollowedId(user.getId(), foundUser.getId()).isPresent());
        dto.setFollower(followerRepository.findByFollowerIdAndFollowedId(foundUser.getId(), user.getId()).isPresent());

        return dto;
    }

    @Override
    public User updateBackground(UpdateUserBackgroundRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        user.setBackground(fileStorageService.storeFile(request.getBackground(), user));

        userRepository.save(user);

        return user;
    }

    @Override
    public User updateAvatar(UpdateUserAvatarRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        user.setAvatar(fileStorageService.storeFile(request.getAvatar(), user));

        userRepository.save(user);

        return user;
    }

    @Override
    public User removeAvatar() {
        User user = authHelper.getUserFromAuthCredentials();

        fileStorageService.destroyFile(user.getAvatar());

        user.setAvatar(null);

        userRepository.save(user);

        return user;
    }

    @Override
    public User removeBackground() {
        User user = authHelper.getUserFromAuthCredentials();

        fileStorageService.destroyFile(user.getBackground());

        user.setBackground(null);

        userRepository.save(user);

        return user;
    }

    @Override
    public Boolean changeUserPassword(ChangeUserPasswordRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        if(passwordEncoder.matches(PasswordValidator.decodePassword(request.getOldPassword()), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(PasswordValidator.decodeAndValidatePassword(request.getNewPassword())));

            log.info("User with login " + user.getLogin() + " and ID " + user.getId() + " changed password");

            return true;
        }

        return false;
    }

    @Override
    public PageResponse<UserDTO> findByLoginAndName(String data, PageRequestParams params) {
        User user = authHelper.getUserFromAuthCredentials();

        Page<User> page = userRepository.findByLoginContainingOrPersonalInformationNameContainingAndIdNot(data, data, user.getId(), PageRequest.of(params.getPage(), params.getPageSize()));
        return new PageResponse<>(page.getContent().stream().map(UserDTO::new).collect(Collectors.toList()),
                params.getPage(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public User getUserInfo() {
        AuthCredentials authCredentials = authHelper.getAuthCredentials();

        if(authCredentials == null)
            throw new ServiceException("No auth data. Make login or re-login", HttpStatus.FORBIDDEN);

        return getUserById(authCredentials.getId());
    }

    @Override
    public Boolean checkEmail(String email) {
        Optional<User> user = userRepository.findByEmailDataEmail(email);

        return user.isPresent();
    }

    @Override
    public Boolean checkPhone(String phone) {
        Optional<User> user = userRepository.findByPhoneDataPhone(phone);

        return user.isPresent();
    }

    @Override
    public Boolean updateUserGeo(UpdateUserGeoRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        if(user.getGeo() == null)
            user.setGeo(new UserGeo());

        user.getGeo().setLatitude(request.getLatitude());
        user.getGeo().setLongitude(request.getLongitude());

        userRepository.save(user);

        return true;
    }

    @Override
    public Boolean checkLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);

        return user.isPresent();
    }

    @Override
    public PageResponse<PostDTO> getSavedPosts(PageRequestParams params) {
        List<Post> posts = new ArrayList<>(authHelper.getUserFromAuthCredentials().getSavedPosts());

        int from = params.getPage() * params.getPageSize();
        int to = (params.getPage() + 1) * params.getPageSize();

        if(from > (posts.size() - 1))
            from = posts.size() - 1;

        if(from < 0)
            from = 0;

        if(to > (posts.size() - 1))
            to = posts.size() - 1;

        if(to < 0)
            to = 0;

        return new PageResponse<PostDTO>(posts.subList(from, to).
                stream().map(PostDTO::new).collect(Collectors.toList()), params.getPage(), (int) Math.floor(posts.size() / params.getPageSize()), (long) posts.size());
    }
}
