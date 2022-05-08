package com.inst.base.controller.user;

import com.inst.base.dto.user.UserDTO;
import com.inst.base.dto.user.UserDirectionDTO;
import com.inst.base.entity.user.UserDirection;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.auth.SignInRequest;
import com.inst.base.request.user.*;
import com.inst.base.service.user.UserService;
import com.inst.base.util.ApiResponse;
import com.inst.base.util.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User API")
@SecurityRequirement(name = "Bearer")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserInfo() {
        return new ResponseEntity<>(new UserDTO(userService.getUserInfo()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(
            @PathVariable
                    UUID userId
    ) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/finding/{loginOrName}")
    public ResponseEntity<PageResponse<UserDTO>> findUsers(
            @PathVariable
                    String loginOrName,
            PageRequestParams params
    ) {
        return new ResponseEntity<>(userService.findByLoginAndName(loginOrName, params), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse> editPassword(
            @Valid
            @RequestBody
                    ChangeUserPasswordRequest request
    ) {
        boolean result = userService.changeUserPassword(request);

        return new ResponseEntity<>(new ApiResponse(result, "Password " + (result ? "changed" : "does`t change")), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping(value = "/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDTO> updateAvatar(
            @Valid
            @ModelAttribute
                    UpdateUserAvatarRequest request
    ) {
        return new ResponseEntity<>(new UserDTO(userService.updateAvatar(request)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping(value = "/background", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDTO> updateBackground(
            @Valid
            @ModelAttribute
                    UpdateUserBackgroundRequest request
    ) {
        return new ResponseEntity<>(new UserDTO(userService.updateBackground(request)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping(value = "/avatar")
    public ResponseEntity<UserDTO> removeAvatar() {
        return new ResponseEntity<>(new UserDTO(userService.removeAvatar()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping("/")
    public ResponseEntity<UserDTO> editUser(
            @Valid
            @RequestBody
                    UpdateUserRequest request
    ) {

        return new ResponseEntity<>(new UserDTO(userService.updateUser(request)), HttpStatus.OK);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse> checkEmail(
            @Valid
            @PathVariable
                    String email
    ) {

        return new ResponseEntity<>(new ApiResponse(userService.checkEmail(email), "User checked"), HttpStatus.OK);
    }

    @PatchMapping("/geo")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponse> updateGeo(
            @Valid
            @RequestBody
                    UpdateUserGeoRequest request
    ) {

        return new ResponseEntity<>(new ApiResponse(userService.updateUserGeo(request), "User geo updated"), HttpStatus.OK);
    }

    @GetMapping("/check-phone/{phone}")
    public ResponseEntity<ApiResponse> checkPhone(
            @Valid
            @PathVariable
                    String phone
    ) {

        return new ResponseEntity<>(new ApiResponse(userService.checkPhone(phone), "User phone"), HttpStatus.OK);
    }

    @GetMapping("/check-login/{login}")
    public ResponseEntity<ApiResponse> checkLogin(
            @Valid
            @PathVariable
                    String login
    ) {

        return new ResponseEntity<>(new ApiResponse(userService.checkLogin(login), "User checked"), HttpStatus.OK);
    }

    @GetMapping("/directions")
    public ResponseEntity<List<UserDirectionDTO>> getDirections() {
        return new ResponseEntity<>(Arrays.stream(UserDirection.values()).map(UserDirectionDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }
}
