package com.inst.base.repository.follow;

import com.inst.base.entity.follow.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, UUID> {
    @Query("SELECT f FROM Follower f WHERE f.followOnUser.id = ?1 AND f.followedUser.id = ?2")
    Optional<Follower> findByFollowerIdAndFollowedId(UUID followOnUser, UUID followedUser);

    Page<Follower> findByFollowedUserId(UUID userId, Pageable pageable);

    Page<Follower> findByFollowOnUserId(UUID userId, Pageable pageable);
}
