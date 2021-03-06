package com.inst.base.service.post;

import com.inst.base.dto.post.PostDTO;
import com.inst.base.entity.place.Place;
import com.inst.base.entity.post.*;
import com.inst.base.entity.user.AccountType;
import com.inst.base.entity.user.User;
import com.inst.base.repository.place.PlaceRepository;
import com.inst.base.repository.post.*;
import com.inst.base.repository.user.UserRepository;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.post.*;
import com.inst.base.service.FileStorageService;
import com.inst.base.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PostFileRepository postFileRepository;

    @Autowired
    private PostCommentLikeRepository postCommentLikeRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostDislikeRepository postDislikeRepository;

    @Override
    public PageResponse<PostDTO> getMyTextPosts(PageRequestParams params) {
        User user = authHelper.getUserFromAuthCredentials();

        Page<Post> page = postRepository.findByTypeAndCreatorId(PostType.TEXT, user.getId(), PageRequest.of(params.getPage(), params.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")));

        return new PageResponse<>(page.getContent().stream().map(PostDTO::new).collect(Collectors.toList()), params.getPage(),
                page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public PageResponse<PostDTO> getMyMediaPosts(PageRequestParams params) {
        User user = authHelper.getUserFromAuthCredentials();

        Page<Post> page = postRepository.findByTypeAndCreatorId(PostType.STANDART, user.getId(), PageRequest.of(params.getPage(), params.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")));

        return new PageResponse<>(page.getContent().stream().map(PostDTO::new).collect(Collectors.toList()), params.getPage(),
                page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public PostDTO dislikePost(CreateDislikePostRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        if(!AccessChecker.followedOrEquals(user, post.getCreator()))
            throw new ServiceException("No access", HttpStatus.FORBIDDEN);

        postDislikeRepository.findByPostIdAndDislikedUserId(post.getId(), user.getId()).ifPresent((val) -> {
            throw new ServiceException("Dislike already exists", HttpStatus.BAD_REQUEST);
        });

        PostDislike dislike = new PostDislike();

        dislike.setDislikedUser(user);
        dislike.setPost(post);

        postDislikeRepository.save(dislike);

        PostDTO dto = new PostDTO(post);

        dto.setCommented(postCommentRepository.countByPostIdAndSentUserId(post.getId(), user.getId()) > 0);
        dto.setDisliked(true);
        dto.setLiked(postLikeRepository.findByPostIdAndLikedUserId(post.getId(), user.getId()).isPresent());

        return dto;
    }

    @Override
    public PostDTO removeDislikePost(UUID postId) {
        User user = authHelper.getUserFromAuthCredentials();

        PostDislike postDislike = postDislikeRepository.findByPostIdAndDislikedUserId(postId, user.getId()).orElseThrow(() -> {
            throw new ServiceException("Dislike not found", HttpStatus.NOT_FOUND);
        });

        Post post = postRepository.findById(postDislike.getPost().getId()).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        postDislike.setPost(null);
        postDislike.setDislikedUser(null);

        postDislikeRepository.delete(postDislike);

        PostDTO dto = new PostDTO(post);

        dto.setCommented(postCommentRepository.countByPostIdAndSentUserId(post.getId(), user.getId()) > 0);
        dto.setDisliked(false);
        dto.setLiked(postLikeRepository.findByPostIdAndLikedUserId(post.getId(), user.getId()).isPresent());

        return dto;
    }

    @Override
    public PageResponse<PostDTO> getFeedPosts(PageRequestParams params) {
        User user = authHelper.getUserFromAuthCredentials();

        Page<Post> page = postRepository.getFeed(user.getId(), PageRequest.of(params.getPage(), params.getPageSize()));

        return new PageResponse<PostDTO>(page.getContent().stream().map((p) -> {
            PostDTO dto = new PostDTO(p);

            dto.setCommented(postCommentRepository.countByPostIdAndSentUserId(p.getId(), user.getId()) > 0);
            dto.setDisliked(postDislikeRepository.findByPostIdAndDislikedUserId(p.getId(), user.getId()).isPresent());
            dto.setLiked(postLikeRepository.findByPostIdAndLikedUserId(p.getId(), user.getId()).isPresent());

            return dto;
        }).collect(Collectors.toList()),
        params.getPage(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Post createPost(CreatePostRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        Post post = new Post();

        post.setCreator(user);
        post.setContent(request.getContent());

        if(request.getPlace() != null) {
            Optional<Place> foundPlace = placeRepository.findByName(request.getPlace());

            if(foundPlace.isPresent())
                post.setPlace(foundPlace.get());
            else {
                Place place = new Place();

                place.setName(request.getPlace());

                placeRepository.save(place);

                post.setPlace(place);
            }
        }

        post.setType(PostType.valueOf(request.getType().replace("\"", "")));

        if(post.getType() != PostType.TEXT) {
            if(request.getFiles() == null)
                throw new ServiceException("?????????????????? ?????????? ?????? ????????????????", HttpStatus.BAD_REQUEST);

            request.getFiles().forEach((file) -> {
                if(file.getOriginalFilename().contains(".."))
                    throw new ServiceException("Invalid filename", HttpStatus.BAD_REQUEST);

                if(!file.getContentType().contains("video") && !file.getContentType().contains("image"))
                    throw new ServiceException("Invalid file type", HttpStatus.BAD_REQUEST);
            });

            for (MultipartFile file : request.getFiles()) {
                String filePath = fileStorageService.storeFile(file, user);

                PostFile postFile = new PostFile();
                postFile.setFilePath(filePath);
                postFile.setPost(post);
                postFile.setType(file.getContentType().contains("video") ? PostFileType.VIDEO : PostFileType.IMAGE);

                postFileRepository.save(postFile);

                post.getFiles().add(postFile);
            }
        }

        postRepository.save(post);

        return post;
    }

    @Override
    public PageResponse<PostDTO> getPosts(PostType type, UUID userId, PageRequestParams pageRequestParams) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
        });

        User requester = null;

        try {
            requester = authHelper.getUserFromAuthCredentials();
        } catch (Exception ignored) {

        }

        if(user.getAccountType() == AccountType.CLOSED)
            if(!AccessChecker.followedOrEquals(user, requester))
                throw new ServiceException("No access", HttpStatus.FORBIDDEN);

        Page<Post> postPage = postRepository.findByTypeAndCreatorId(type, userId, PageRequest.of(pageRequestParams.getPage(), pageRequestParams.getPageSize()));

        User finalRequester = requester;

        return new PageResponse<PostDTO>(postPage.getContent().stream().map((p) -> {
            PostDTO dto = new PostDTO(p);

            if(finalRequester != null) {
                dto.setLiked(postLikeRepository.findByPostIdAndLikedUserId(p.getId(), finalRequester.getId()).isPresent());
                dto.setCommented(postCommentRepository.countByPostIdAndSentUserId(p.getId(), finalRequester.getId()) > 0);
                dto.setDisliked(postDislikeRepository.findByPostIdAndDislikedUserId(p.getId(), finalRequester.getId()).isPresent());
            }

            return dto;
        }).collect(Collectors.toList()), pageRequestParams.getPage(), postPage.getTotalPages(), postPage.getTotalElements());
    }

    @Override
    public PostDTO getPostById(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        User requester = null;

        try {
            requester = authHelper.getUserFromAuthCredentials();
        } catch (Exception ignored) {

        }

        if(post.getCreator().getAccountType() == AccountType.CLOSED)
            if(!AccessChecker.followedOrEquals(post.getCreator(), requester))
                throw new ServiceException("No access", HttpStatus.FORBIDDEN);

        PostDTO dto = new PostDTO(post);

        if(requester != null) {
            dto.setLiked(postLikeRepository.findByPostIdAndLikedUserId(post.getId(), requester.getId()).isPresent());
            dto.setCommented(postCommentRepository.countByPostIdAndSentUserId(post.getId(), requester.getId()) > 0);
            dto.setDisliked(postDislikeRepository.findByPostIdAndDislikedUserId(post.getId(), requester.getId()).isPresent());
        }

        return dto;
    }

    @Override
    public Boolean removePost(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        for (PostFile pf : post.getFiles())
            fileStorageService.destroyFile(pf.getFilePath());

        postRepository.delete(post);

        return true;
    }

    @Override
    public PostDTO likePost(CreateLikePostRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        if(!AccessChecker.followedOrEquals(user, post.getCreator()))
            throw new ServiceException("No access", HttpStatus.FORBIDDEN);

        postLikeRepository.findByPostIdAndLikedUserId(post.getId(), user.getId()).ifPresent((val) -> {
            throw new ServiceException("Like already exists", HttpStatus.BAD_REQUEST);
        });

        PostLike like = new PostLike();

        like.setLikedUser(user);
        like.setPost(post);

        postLikeRepository.save(like);

        PostDTO dto = new PostDTO(post);

        dto.setLiked(true);
        dto.setCommented(postCommentRepository.countByPostIdAndSentUserId(post.getId(), user.getId()) > 0);
        dto.setDisliked(postDislikeRepository.findByPostIdAndDislikedUserId(post.getId(), user.getId()).isPresent());

        return dto;
    }

    @Override
    public PostComment commentPost(CreatePostCommentRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        if(!AccessChecker.followedOrEquals(user, post.getCreator()))
            throw new ServiceException("No access", HttpStatus.FORBIDDEN);

        PostComment comment = new PostComment();

        comment.setSentUser(user);
        comment.setPost(post);
        comment.setContent(request.getComment());

        postCommentRepository.save(comment);

        return comment;
    }

    @Override
    @Transactional
    public PostDTO removeLikePost(UUID postId) {
        User user = authHelper.getUserFromAuthCredentials();

        PostLike postLike = postLikeRepository.findByPostIdAndLikedUserId(postId, user.getId()).orElseThrow(() -> {
            throw new ServiceException("Like not found", HttpStatus.NOT_FOUND);
        });

        Post post = postRepository.findById(postLike.getPost().getId()).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        postLike.setPost(null);
        postLike.setLikedUser(null);

        postLikeRepository.delete(postLike);

        PostDTO dto = new PostDTO(post);

        dto.setLiked(false);
        dto.setCommented(postCommentRepository.countByPostIdAndSentUserId(post.getId(), user.getId()) > 0);
        dto.setDisliked(postDislikeRepository.findByPostIdAndDislikedUserId(post.getId(), user.getId()).isPresent());

        return dto;
    }

    @Override
    @Transactional
    public PostComment removeCommentPost(UUID commentId) {
        User user = authHelper.getUserFromAuthCredentials();

        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(() -> {
            throw new ServiceException("Comment not found", HttpStatus.NOT_FOUND);
        });

        if(!postComment.getSentUser().getId().equals(user.getId()))
            throw new ServiceException("It`s not you comment", HttpStatus.BAD_REQUEST);

        postCommentLikeRepository.findByPostCommentId(postComment.getId()).forEach((val) -> {
            val.setPostComment(null);
            val.setLikedUser(null);
            val.setPost(null);

            postCommentLikeRepository.delete(val);
        });

        postCommentRepository.delete(postComment);

        return postComment;
    }

    @Override
    public PostComment likePostComment(CreateLikePostCommentRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        PostComment postComment = postCommentRepository.findById(request.getPostCommentId()).orElseThrow(() -> {
            throw new ServiceException("Comment not found", HttpStatus.NOT_FOUND);
        });

        postCommentLikeRepository.findByPostCommentIdAndLikedUserId(postComment.getId(), user.getId()).ifPresent((val) -> {
            throw new ServiceException("You like on this comment already exist", HttpStatus.BAD_REQUEST);
        });

        PostCommentLike like = new PostCommentLike();

        like.setPostComment(postComment);
        like.setPost(postComment.getPost());
        like.setLikedUser(user);

        postCommentLikeRepository.save(like);

        postComment.getLikes().add(like);

        return postComment;
    }

    @Override
    @Transactional
    public PostComment removeLikeComment(UUID likeId) {
        User user = authHelper.getUserFromAuthCredentials();

        PostCommentLike like = postCommentLikeRepository.findById(likeId).orElseThrow(() -> {
            throw new ServiceException("Like not found", HttpStatus.NOT_FOUND);
        });

        PostComment postComment = postCommentRepository.findById(like.getPostComment().getId()).orElseThrow(() -> {
            throw new ServiceException("Comment not found", HttpStatus.NOT_FOUND);
        });

        if(!like.getLikedUser().getId().equals(user.getId()))
            throw new ServiceException("It`s not you like", HttpStatus.BAD_REQUEST);


        like.setLikedUser(null);
        like.setPost(null);
        like.setPostComment(null);

        postCommentLikeRepository.delete(like);

        return postComment;
    }

    @Override
    public Post savePost(SavePostRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> {
            throw new ServiceException("Post not found", HttpStatus.NOT_FOUND);
        });

        if(!AccessChecker.followedOrEquals(user, post.getCreator()))
            throw new ServiceException("No access", HttpStatus.BAD_REQUEST);

        user.getSavedPosts().add(post);

        userRepository.save(user);

        return post;
    }

    @Override
    public Boolean removeSavedPost(UUID id) {
        User user = authHelper.getUserFromAuthCredentials();

        user.getSavedPosts().removeIf((val) -> val.getId().equals(id));

        userRepository.save(user);

        return true;
    }
}
