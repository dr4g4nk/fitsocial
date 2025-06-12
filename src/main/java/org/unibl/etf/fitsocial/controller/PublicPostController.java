package org.unibl.etf.fitsocial.controller;

import core.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitsocial.dto.CommentDto;
import org.unibl.etf.fitsocial.dto.LikeDto;
import org.unibl.etf.fitsocial.dto.PostDto;
import core.dto.ResponseDto;
import org.unibl.etf.fitsocial.entity.Comment;
import org.unibl.etf.fitsocial.entity.Like;
import org.unibl.etf.fitsocial.entity.Post;
import org.unibl.etf.fitsocial.service.CommentService;
import org.unibl.etf.fitsocial.service.LikeService;
import org.unibl.etf.fitsocial.service.PostService;

@RestController
@RequestMapping("/public")
public class PublicPostController {

    protected PostService postService;
    protected LikeService likeService;
    protected CommentService commentService;

    PublicPostController(PostService postService, LikeService likeService, CommentService commentService) {
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @GetMapping("/post")
    public ResponseDto<PageResponseDto<PostDto.List>, Post> getAll(Pageable pageable) {
         return postService.findPublicPostsWithLikesAndComments(pageable, true);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<ResponseDto<PostDto, Post>> getPostById(@PathVariable Long id) {
        var response = postService.findById(id);
        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/post/{postId}/like")
    public ResponseEntity<ResponseDto<PageResponseDto<LikeDto.List>, Like>> getLikesByPostId(@PathVariable Long postId, Pageable pageable) {
        var response = likeService.findAllByPostId(postId, pageable);
        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<ResponseDto<PageResponseDto<CommentDto.List>, Comment>>  getCommentsByPostId(@PathVariable Long postId, Pageable pageable) {
        var response = commentService.findAllByPostId(postId, pageable);
        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
