package org.unibl.etf.fitsocial.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.dto.ActivityDto;
import org.unibl.etf.fitsocial.entity.Activity;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class ActivityService extends BaseSoftDeletableServiceImpl<
    Activity,
    ActivityDto,
    ActivityDto.List,
    ActivityDto.Update,
    ActivityDto.Create,
    Long
> {
    public ActivityService(
        BaseSoftDeletableRepository<Activity, Long> repository,
        IMapper<Activity, ActivityDto, ActivityDto.List, ActivityDto.Update, ActivityDto.Create> mapper
    ) {
        super(repository, mapper);
    }
}