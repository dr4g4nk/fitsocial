package org.unibl.etf.fitsocial.feed.media;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class MediaService extends BaseSoftDeletableServiceImpl<
    Media,
    MediaDto,
    MediaDto.List,
    MediaDto.Update,
    MediaDto.Create,
    Long
> {
    public MediaService(
        BaseSoftDeletableRepository<Media, Long> repository,
        IMapper<Media, MediaDto, MediaDto.List, MediaDto.Update, MediaDto.Create> mapper
    ) {
        super(repository, mapper);
    }
}