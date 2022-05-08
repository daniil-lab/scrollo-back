package com.inst.base.service.post;

import com.inst.base.dto.post.PostDTO;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostComment;
import com.inst.base.entity.post.PostType;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.post.*;
import com.inst.base.util.PageResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PostService {
    PageResponse<PostDTO> getFeedPosts(PageRequestParams params);

    PageResponse<PostDTO> getMyTextPosts(PageRequestParams params);

    PageResponse<PostDTO> getMyMediaPosts(PageRequestParams params);

    Post createPost(CreatePostRequest request);

    PageResponse<PostDTO> getPosts(PostType type, UUID userId, PageRequestParams pageRequestParams);

    PostDTO getPostById(UUID id);

    Boolean removePost(UUID postId);

    PostDTO likePost(CreateLikePostRequest request);

    PostDTO dislikePost(CreateDislikePostRequest request);

    PostComment commentPost(CreatePostCommentRequest request);

    PostDTO removeLikePost(UUID likeId);

    PostDTO removeDislikePost(UUID likeId);

    PostComment removeCommentPost(UUID commentId);

    PostComment likePostComment(CreateLikePostCommentRequest commentId);

    PostComment removeLikeComment(UUID likeId);

    Post savePost(SavePostRequest request);

    Boolean removeSavedPost(UUID id);
}
