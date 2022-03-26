package com.inst.base.repository.post;

import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    Optional<PostLike> findByPostIdAndLikedUserId(UUID postId, UUID userId);
}
