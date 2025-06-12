package org.unibl.etf.fitsocial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service. BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.entity.Media;
import org.unibl.etf.fitsocial.dto.MediaDto;

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