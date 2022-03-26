package com.inst.base.repository.follow;

import com.inst.base.entity.follow.FollowRequest;
import com.inst.base.entity.follow.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, UUID> {
    @Query("SELECT f FROM FollowRequest f WHERE f.followingRequester.id = ?1 AND f.followingReceiver.id = ?2")
    Optional<Follower> findByFollowerIdAndFollowedId(UUID followingRequester, UUID followingReceiver);
}
