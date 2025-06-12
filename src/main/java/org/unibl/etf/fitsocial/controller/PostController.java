package org.unibl.etf.fitsocial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service. BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.entity.Post;
import org.unibl.etf.fitsocial.dto.PostDto;
import org.unibl.etf.fitsocial.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController extends BaseController<
        Post,
        PostDto,
        PostDto.List,
        PostDto.Update,
        PostDto.Create,
        Long
        > {
    public PostController(BaseSoftDeletableServiceImpl<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create, Long> service) {
        super(service);
    }


}