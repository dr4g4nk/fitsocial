package org.unibl.etf.fitsocial.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitsocial.controller.base.BaseController;
import org.unibl.etf.fitsocial.dto.PostLikeDto;
import org.unibl.etf.fitsocial.service.PostLikeService;

@RestController
@RequestMapping("/api/postLikes")
public class PostLikeController extends BaseController<PostLikeDto.GetAll,PostLikeDto,PostLikeDto,PostLikeDto.Create, Long> {
    protected PostLikeService service;
    public PostLikeController(PostLikeService service) {
        super(service);

        this.service = service;
    }

    @GetMapping("/post/{postId}")
    public PagedModel<EntityModel<PostLikeDto.GetAll>> getAllByPostId(@PathVariable Long postId, Pageable pageable, PagedResourcesAssembler<PostLikeDto.GetAll> assembler) {
        return  assembler.toModel(service.findAllByPostId(postId, pageable));
    }
}
