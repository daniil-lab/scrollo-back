package com.inst.base.service.post;

import com.inst.base.dto.post.PostDTO;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostComment;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.post.*;
import com.inst.base.util.PageResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PostService {
    PageResponse<PostDTO> getFeedPosts(PageRequestParams params);

    Post createPost(CreatePostRequest request);

    PageResponse<PostDTO> getPosts(UUID userId, PageRequestParams pageRequestParams);

    Post getPostById(UUID id);

    Boolean removePost(UUID postId);

    Post likePost(CreateLikePostRequest request);

    PostComment commentPost(CreatePostCommentRequest request);

    Post removeLikePost(UUID likeId);

    PostComment removeCommentPost(UUID commentId);

    PostComment likePostComment(CreateLikePostCommentRequest commentId);

    PostComment removeLikeComment(UUID likeId);

    Post savePost(SavePostRequest request);

    Boolean removeSavedPost(UUID id);
}
