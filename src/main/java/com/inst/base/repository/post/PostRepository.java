package com.inst.base.repository.post;

import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query(value = "SELECT * FROM post WHERE creator_id = ?1 OR creator_id = (SELECT follow_on_user_id FROM follow WHERE followed_id = ?1)", nativeQuery = true)
    Page<Post> getFeed(UUID userId, Pageable pageable);

    Page<Post> findByTypeAndCreatorId(PostType type, UUID userId, Pageable pageable);
}
