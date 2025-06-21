package org.unibl.etf.fitsocial.feed.activity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;

@RestController
@RequestMapping("/api/activity")
public class ActivityController extends BaseController<
    Activity,
    ActivityDto,
    ActivityDto.List,
    ActivityDto.Update,
    ActivityDto.Create,
    Long
> {
    public ActivityController(BaseSoftDeletableServiceImpl<Activity, ActivityDto, ActivityDto.List, ActivityDto.Update, ActivityDto.Create, Long> service) {
        super(service);
    }
}