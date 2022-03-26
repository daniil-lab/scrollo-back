package com.inst.base.repository.post;

import com.inst.base.entity.post.PostCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike, UUID> {
    Optional<PostCommentLike> findByPostCommentIdAndLikedUserId(UUID postId, UUID likeId);

    List<PostCommentLike> findByPostCommentId(UUID postId);
}
