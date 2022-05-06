package com.inst.base.repository.post;

import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostComment;
import com.inst.base.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, UUID> {
    Integer countByPostIdAndSentUserId(UUID postId, UUID userId);
}
