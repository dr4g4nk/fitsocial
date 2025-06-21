package org.unibl.etf.fitsocial.feed.post;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;

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