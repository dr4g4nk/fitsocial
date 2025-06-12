package org.unibl.etf.fitsocial.controller;


import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitsocial.controller.base.BaseController;
import org.unibl.etf.fitsocial.dto.PostCommentDto;
import org.unibl.etf.fitsocial.dto.PostLikeDto;
import org.unibl.etf.fitsocial.service.PostCommentService;

@RestController
@RequestMapping("/api/postComments")
public class PostCommentController extends BaseController<PostCommentDto,PostCommentDto,PostCommentDto.Update,PostCommentDto.Create, Long> {
    protected PostCommentService service;
    public PostCommentController(PostCommentService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/post/{postId}")
    public PagedModel<EntityModel<PostCommentDto>> getAllByPostId(@PathVariable Long postId, Pageable pageable, PagedResourcesAssembler<PostCommentDto> assembler) {
        return  assembler.toModel(service.findAllByPostId(postId, pageable));
    }
}