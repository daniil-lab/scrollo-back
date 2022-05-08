package com.inst.base.repository.post;

import com.inst.base.entity.post.PostDislike;
import com.inst.base.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostDislikeRepository extends JpaRepository<PostDislike, UUID> {
    Optional<PostDislike> findByPostIdAndDislikedUserId(UUID postId, UUID userId);
}
