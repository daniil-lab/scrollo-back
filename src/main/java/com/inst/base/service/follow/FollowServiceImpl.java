package com.inst.base.service.follow;

import com.inst.base.dto.follow.FollowerDTO;
import com.inst.base.entity.follow.FollowRequest;
import com.inst.base.entity.follow.Follower;
import com.inst.base.entity.user.AccountType;
import com.inst.base.entity.user.User;
import com.inst.base.repository.user.UserRepository;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.follow.FollowOnUserRequest;
import com.inst.base.repository.follow.FollowRequestRepository;
import com.inst.base.repository.follow.FollowerRepository;
import com.inst.base.request.follow.ModerateFollowAction;
import com.inst.base.request.follow.ModerateFollowRequest;
import com.inst.base.util.AuthHelper;
import com.inst.base.util.PageResponse;
import com.inst.base.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthHelper authHelper;

    @Override
    public Boolean removeFollower(UUID followId) {
        User user = authHelper.getUserFromAuthCredentials();

        Follower follower = followerRepository.findById(followId).orElseThrow(() -> {
            throw new ServiceException("Follow not found", HttpStatus.NOT_FOUND);
        });

        if(!follower.getFollowOnUser().getId().equals(user.getId()))
            throw new ServiceException("It`s not your follower", HttpStatus.BAD_REQUEST);

        follower.setFollowedUser(null);
        follower.setFollowOnUser(null);

        followerRepository.delete(follower);

        return true;
    }

    @Override
    public Boolean declineFollow(UUID followId) {
        User user = authHelper.getUserFromAuthCredentials();

        Follower follower = followerRepository.findById(followId).orElseThrow(() -> {
            throw new ServiceException("Follow not found", HttpStatus.NOT_FOUND);
        });

        if(!follower.getFollowedUser().getId().equals(user.getId()))
            throw new ServiceException("It`s not your follow", HttpStatus.BAD_REQUEST);

        follower.setFollowedUser(null);
        follower.setFollowOnUser(null);

        followerRepository.delete(follower);

        return true;
    }

    @Override
    public Boolean followedOnUser(UUID id) {
        return followerRepository.findByFollowerIdAndFollowedId(id, authHelper.getUserFromAuthCredentials().getId()).isPresent();
    }

    @Override
    public Boolean declineFollowRequest(UUID id) {
        User user = authHelper.getUserFromAuthCredentials();

        FollowRequest followRequest = followRequestRepository.findById(id).orElseThrow(() -> {
            throw new ServiceException("Follow request not found", HttpStatus.NOT_FOUND);
        });

        if(!followRequest.getFollowingRequester().getId().equals(user.getId()))
            throw new ServiceException("It`s not your follow request", HttpStatus.BAD_REQUEST);

        followRequestRepository.delete(followRequest);

        return true;
    }

    @Override
    public Boolean moderateFollowRequest(ModerateFollowRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        FollowRequest followRequest = followRequestRepository.findById(request.getFollowId()).orElseThrow(() -> {
            throw new ServiceException("Follow request not found", HttpStatus.NOT_FOUND);
        });

        if(!followRequest.getFollowingReceiver().getId().equals(user.getId()))
            throw new ServiceException("It`s not your follow request", HttpStatus.BAD_REQUEST);

        if(request.getAction() == ModerateFollowAction.ACCEPT) {
            Follower follower = new Follower();

            follower.setFollowOnUser(user);
            follower.setFollowedUser(followRequest.getFollowingRequester());

            followerRepository.save(follower);
        }

        followRequestRepository.delete(followRequest);

        return true;
    }

    @Override
    public Boolean followOnUser(FollowOnUserRequest request) {
        User followRequester = authHelper.getUserFromAuthCredentials();

        User followOnUser = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            throw new ServiceException("User with passed ID not found", HttpStatus.NOT_FOUND);
        });

        if(followOnUser.getId().equals(followRequester.getId()))
            throw new ServiceException("You sended your ID", HttpStatus.BAD_REQUEST);

        followerRepository.findByFollowerIdAndFollowedId(followOnUser.getId(), followRequester.getId()).ifPresent((val) -> {
            throw new ServiceException("You already followed on this user", HttpStatus.BAD_REQUEST);
        });

        followRequestRepository.findByFollowerIdAndFollowedId(followRequester.getId(), followOnUser.getId()).ifPresent((val) -> {
            throw new ServiceException("You already sent follow request for this user", HttpStatus.BAD_REQUEST);
        });

        if(followOnUser.getAccountType() == AccountType.OPEN) {
            Follower follower = new Follower();

            follower.setFollowOnUser(followOnUser);
            follower.setFollowedUser(followRequester);

            followerRepository.save(follower);

            return true;
        } else if (followOnUser.getAccountType() == AccountType.CLOSED) {
            FollowRequest followRequest = new FollowRequest();

            followRequest.setFollowingRequester(followRequester);
            followRequest.setFollowingReceiver(followOnUser);

            followRequestRepository.save(followRequest);

            return true;
        }

        return false;
    }

    @Override
    public PageResponse<FollowerDTO> getFollowers(UUID userId, PageRequestParams params) {
        Page<Follower> pageResult = followerRepository.findByFollowOnUserId(userId, PageRequest.of(params.getPage(), params.getPageSize()));

        return new PageResponse<>(pageResult.getContent().stream().map(FollowerDTO::new).collect(Collectors.toList()),
                params.getPage(), pageResult.getTotalPages(), pageResult.getTotalElements());
    }

    @Override
    public PageResponse<FollowerDTO> getFollowing(UUID userId, PageRequestParams params) {
        Page<Follower> pageResult = followerRepository.findByFollowedUserId(userId, PageRequest.of(params.getPage(), params.getPageSize()));

        return new PageResponse<>(pageResult.getContent().stream().map(FollowerDTO::new).collect(Collectors.toList()),
                params.getPage(), pageResult.getTotalPages(), pageResult.getTotalElements());
    }

    @Override
    public Set<FollowRequest> getFollowRequests() {
        return authHelper.getUserFromAuthCredentials().getFollowingRequests();
    }
}
