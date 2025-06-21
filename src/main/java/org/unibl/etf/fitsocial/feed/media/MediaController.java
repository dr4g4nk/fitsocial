package org.unibl.etf.fitsocial.feed.media;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;

@RestController
@RequestMapping("/api/media")
public class MediaController extends BaseController<
    Media,
    MediaDto,
    MediaDto.List,
    MediaDto.Update,
    MediaDto.Create,
    Long
> {
    public MediaController(BaseSoftDeletableServiceImpl<Media, MediaDto, MediaDto.List, MediaDto.Update, MediaDto.Create, Long> service) {
        super(service);
    }

}