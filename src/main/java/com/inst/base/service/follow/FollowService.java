package com.inst.base.service.follow;

import com.inst.base.dto.follow.FollowerDTO;
import com.inst.base.entity.follow.FollowRequest;
import com.inst.base.entity.follow.Follower;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.follow.FollowOnUserRequest;
import com.inst.base.request.follow.ModerateFollowRequest;
import com.inst.base.util.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public interface FollowService {
    Boolean followOnUser(FollowOnUserRequest request);

    PageResponse<FollowerDTO> getFollowers(UUID userId, PageRequestParams pageRequestParams);

    PageResponse<FollowerDTO> getFollowing(UUID userId, PageRequestParams pageRequestParams);

    Set<FollowRequest> getFollowRequests();

    Set<FollowRequest> getSentFollowRequests();

    Boolean declineFollow(UUID followId);

    Boolean removeFollower(UUID followId);

    Boolean followedOnUser(UUID id);

    Boolean moderateFollowRequest(ModerateFollowRequest request);

    Boolean declineFollowRequest(UUID id);
}
