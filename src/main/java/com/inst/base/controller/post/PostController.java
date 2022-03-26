package com.inst.base.controller.post;

import com.inst.base.dto.post.PostCommentDTO;
import com.inst.base.dto.post.PostDTO;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.post.*;
import com.inst.base.service.post.PostService;
import com.inst.base.util.ApiResponse;
import com.inst.base.util.AuthHelper;
import com.inst.base.util.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post API")
@SecurityRequirement(name = "Bearer")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private AuthHelper authHelper;

    @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostDTO> createPost(
            @Valid
            @ModelAttribute
                CreatePostRequest request
    ) {
        return new ResponseEntity<>(new PostDTO(postService.createPost(request)), HttpStatus.CREATED);
    }

    @PostMapping(value = "/like")
    public ResponseEntity<PostDTO> likePost(
            @Valid
            @RequestBody
                    CreateLikePostRequest request
    ) {
        return new ResponseEntity<>(new PostDTO(postService.likePost(request)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/like/{likeId}")
    public ResponseEntity<PostDTO> removeLikePost(
            @PathVariable
                    UUID likeId
    ) {
        return new ResponseEntity<>(new PostDTO(postService.removeLikePost(likeId)), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<PostDTO> savePost(
            @Valid
            @RequestBody
                    SavePostRequest request
    ) {
        return new ResponseEntity<>(new PostDTO(postService.savePost(request)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/save/{postId}")
    public ResponseEntity<ApiResponse> removeSavedPost(
            @PathVariable
                    UUID postId
    ) {
        boolean result = postService.removeSavedPost(postId);

        return new ResponseEntity<>(new ApiResponse(result, "Post " + (result ? "removed" : "does`t remove")), HttpStatus.OK);
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<PostCommentDTO> commentPost(
            @Valid
            @RequestBody
                    CreatePostCommentRequest request
    ) {
        return new ResponseEntity<>(new PostCommentDTO(postService.commentPost(request)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/comment/{commentId}")
    public ResponseEntity<PostCommentDTO> removeCommentPost(
            @PathVariable
                    UUID commentId
    ) {
        return new ResponseEntity<>(new PostCommentDTO(postService.removeCommentPost(commentId)), HttpStatus.OK);
    }

    @PostMapping(value = "/comment/like")
    public ResponseEntity<PostCommentDTO> createCommentLike(
            @Valid
            @RequestBody
                    CreateLikePostCommentRequest request
    ) {
        return new ResponseEntity<>(new PostCommentDTO(postService.likePostComment(request)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/comment/like/{postCommentId}")
    public ResponseEntity<PostCommentDTO> removePostCommentLike(
            @PathVariable
                    UUID postCommentId
    ) {
        return new ResponseEntity<>(new PostCommentDTO(postService.removeLikeComment(postCommentId)), HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<PageResponse<PostDTO>> getMyPosts(
            @Valid
                PageRequestParams pageRequestParams
    ) {
        return new ResponseEntity<>(postService.getPosts(authHelper.getUserFromAuthCredentials().getId(), pageRequestParams), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<PageResponse<PostDTO>> getPosts(
            @PathVariable
                    UUID userId,
            @Valid
                    PageRequestParams pageRequestParams
    ) {
        return new ResponseEntity<>(postService.getPosts(userId, pageRequestParams), HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostDTO> getPostById(
            @PathVariable
                    UUID postId
    ) {
        return new ResponseEntity<>(new PostDTO(postService.getPostById(postId)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<ApiResponse> removePost(
            @PathVariable
                    UUID postId
    ) {
        boolean result = postService.removePost(postId);

        return new ResponseEntity<>(new ApiResponse(result, "Post " + (result ? "removed" : "does`t remove")), HttpStatus.OK);
    }
}
