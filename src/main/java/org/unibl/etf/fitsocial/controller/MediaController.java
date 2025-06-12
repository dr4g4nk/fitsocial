package org.unibl.etf.fitsocial.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitsocial.controller.base.BaseController;
import org.unibl.etf.fitsocial.dto.PostMediaDto;
import org.unibl.etf.fitsocial.service.PostMediaService;

@RestController
@RequestMapping("/api/postMedias")
public class PostMediaController extends BaseController<PostMediaDto,PostMediaDto,PostMediaDto,PostMediaDto, Long> {
    public PostMediaController(PostMediaService service) {
        super(service);
    }
}