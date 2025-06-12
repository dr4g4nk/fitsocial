package org.unibl.etf.fitsocial.controller;


import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import org.unibl.etf.fitsocial.dto.CommentDto;
import org.unibl.etf.fitsocial.entity.Comment;
import org.unibl.etf.fitsocial.service.CommentService;

@RestController
@RequestMapping("/api/comment")
public class CommentController extends BaseController<Comment, CommentDto, CommentDto.List, CommentDto.Update, CommentDto.Create, Long> {
    protected CommentService service;

    public CommentController(CommentService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseDto<PageResponseDto<CommentDto.List>, Comment>> getAllByPostId(@PathVariable Long postId, Pageable pageable) {
        return ResponseEntity.ok(service.findAllByPostId(postId, pageable));
    }
}