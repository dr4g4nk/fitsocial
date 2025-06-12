package org.unibl.etf.fitsocial.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.fitsocial.dto.PostMediaDto;
import org.unibl.etf.fitsocial.entity.PostMedia;
import org.unibl.etf.fitsocial.mapper.base.IMapper;
import org.unibl.etf.fitsocial.repository.base.BaseSoftDeletableRepository;
import org.unibl.etf.fitsocial.service.base.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class PostMediaService extends BaseSoftDeletableServiceImpl<PostMedia, PostMediaDto, PostMediaDto, PostMediaDto, PostMediaDto, Long> {
    public PostMediaService(BaseSoftDeletableRepository<PostMedia, Long> repository, IMapper<PostMedia, PostMediaDto, PostMediaDto, PostMediaDto, PostMediaDto> mapper) {
        super(repository, mapper);
    }
}
