package com.inst.base.repository.post;

import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findByTypeAndCreatorId(PostType type, UUID userId, Pageable pageable);
}
