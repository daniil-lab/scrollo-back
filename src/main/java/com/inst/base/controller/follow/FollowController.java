package com.inst.base.controller.follow;

import com.inst.base.dto.follow.FollowerDTO;
import com.inst.base.dto.user.UserDTO;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.follow.FollowOnUserRequest;
import com.inst.base.service.follow.FollowService;
import com.inst.base.util.ApiResponse;
import com.inst.base.util.AuthHelper;
import com.inst.base.util.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/follow")
@Tag(name = "Follow API")
@SecurityRequirement(name = "Bearer")
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private AuthHelper authHelper;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/followers/me")
    public ResponseEntity<PageResponse<FollowerDTO>> getFollowers(
            @Valid
                    PageRequestParams pageRequestParams
    ) {
        return new ResponseEntity<>(followService.getFollowers(authHelper.getUserFromAuthCredentials().getId(), pageRequestParams), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/following/{followId}")
    public ResponseEntity<ApiResponse> removeFollower(
            @PathVariable
                    UUID followId
    ) {
        return new ResponseEntity<>(new ApiResponse(followService.removeFollower(followId), "Follower removed"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/followed-on-him/{userId}")
    public ResponseEntity<ApiResponse> checkFollowing(
            @Valid
            @PathVariable
                    UUID userId
    ) {

        return new ResponseEntity<>(new ApiResponse(followService.followedOnUser(userId), "User checked"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{followId}")
    public ResponseEntity<ApiResponse> declineFollow(
            @PathVariable
                    UUID followId
    ) {
        return new ResponseEntity<>(new ApiResponse(followService.declineFollow(followId), "Unfollow successful"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/followers/{userId}/")
    public ResponseEntity<PageResponse<FollowerDTO>> getFollowersWithId(
            @PathVariable
                    UUID userId,
            @Valid
                    PageRequestParams pageRequestParams
    ) {
        return new ResponseEntity<>(followService.getFollowers(userId, pageRequestParams), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/following/{userId}/")
    public ResponseEntity<PageResponse<FollowerDTO>> getFollowingsWithId(
            @PathVariable
                    UUID userId,
            @Valid
                    PageRequestParams pageRequestParams
    ) {
        return new ResponseEntity<>(followService.getFollowing(userId, pageRequestParams), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/following/me")
    public ResponseEntity<PageResponse<FollowerDTO>> getFollowings(
            @Valid
                    PageRequestParams pageRequestParams
    ) {
        return new ResponseEntity<>(followService.getFollowing(authHelper.getUserFromAuthCredentials().getId(), pageRequestParams), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse> followOnUser(
            @RequestBody
            @Valid
                FollowOnUserRequest request
    ) {
        boolean result = followService.followOnUser(request);
        return new ResponseEntity<>(new ApiResponse(result, "You " + (result ? "followed" : "does`t follow") + " on user"), HttpStatus.OK);
    }
}
