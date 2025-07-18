package org.unibl.etf.fitsocial.controller;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitsocial.auth.user.UserService;
import org.unibl.etf.fitsocial.feed.comment.Comment;
import org.unibl.etf.fitsocial.feed.comment.CommentDto;
import org.unibl.etf.fitsocial.feed.comment.CommentService;
import org.unibl.etf.fitsocial.feed.like.Like;
import org.unibl.etf.fitsocial.feed.like.LikeDto;
import org.unibl.etf.fitsocial.feed.like.LikeService;
import org.unibl.etf.fitsocial.feed.media.MediaService;
import org.unibl.etf.fitsocial.feed.post.Post;
import org.unibl.etf.fitsocial.feed.post.PostDto;
import org.unibl.etf.fitsocial.feed.post.PostService;
import org.unibl.etf.fitsocial.feed.post.PostDto.List;
import org.unibl.etf.fitsocial.service.FileStorageService;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/public")
public class PublicPostController {

    private final MediaService mediaService;
    private final FileStorageService fileStorageService;
    private final UserService userService;
    protected PostService postService;
    protected LikeService likeService;
    protected CommentService commentService;

    PublicPostController(PostService postService, LikeService likeService, CommentService commentService, MediaService mediaService, FileStorageService fileStorageService, UserService userService) {
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.mediaService = mediaService;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }

    @GetMapping("/post")
    public ResponseDto<PageResponseDto<PostDto.List>, Post> getAll(Pageable pageable) {
        return postService.finaAllByPublic(pageable, true);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<ResponseDto<PostDto, Post>> getPostById(@PathVariable Long id) {
        var response = postService.findById(id);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/post/{postId}/like")
    public ResponseEntity<ResponseDto<PageResponseDto<LikeDto.List>, Like>> getLikesByPostId(@PathVariable Long postId, Pageable pageable) {
        var response = likeService.findAllByPostId(postId, pageable);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<ResponseDto<PageResponseDto<CommentDto.List>, Comment>> getCommentsByPostId(@PathVariable Long postId, Pageable pageable) {
        var response = commentService.findAllByPostId(postId, pageable);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/post/media/{mediaId}/stream")
    public ResponseEntity<Resource> streamPostImage(@PathVariable Long mediaId) {
        var response = mediaService.findByIdForPublicPost(mediaId);
        if (response.isSuccess()) {
            var media = response.getEntity();
            if (media != null) {
                var mimeType = media.getMimeType();
                Resource resource = fileStorageService.loadAsResource(media.getMediaUrl());

                CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(mimeType))
                        .cacheControl(cacheControl)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }
        }

        return ResponseEntity.notFound().build();

    }

    @Transactional(readOnly = true)
    @GetMapping("/user/{id}/avatar")
    public ResponseEntity<Resource> getUserAvatar(@PathVariable Long id) {
        var response = userService.findById(id);
        if (response.isSuccess() && response.getEntity() != null) {
            var user = response.getEntity();
            var contentType = user.getProfileImageContentType();
            Resource resource = fileStorageService.loadAsResource(user.getProfileImageUrl());

            CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .cacheControl(cacheControl)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

}
