package org.unibl.etf.fitsocial.feed.media;

import core.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.entity.FileType;
import org.unibl.etf.fitsocial.service.FileStorageService;

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
    private final MediaRepository mediaRepository;
    private final FileStorageService fileStorageService;

    public MediaService(
            BaseSoftDeletableRepository<Media, Long> repository,
            IMapper<Media, MediaDto, MediaDto.List, MediaDto.Update, MediaDto.Create> mapper,
            MediaRepository mediaRepository, FileStorageService fileStorageService) {
        super(repository, mapper);
        this.mediaRepository = mediaRepository;
        this.fileStorageService = fileStorageService;
    }

    public ResponseDto<MediaDto, Media> findByIdForPublicPost(Long id) {
        var optionalEntity = mediaRepository.findByIdAndPostPublic(id);
        return new ResponseDto<MediaDto, Media>(optionalEntity.map(mapper::toDto).orElse(null), optionalEntity.orElse(null));
    }

    @Override
    public ResponseDto<MediaDto, Media> save(MediaDto.Create dto) {
        Media entity = mapper.fromCreateDto(dto);
        var fileType = dto.mimeType().startsWith("video/") ? FileType.VIDEO : FileType.IMAGE;
        var path = fileStorageService.store(dto.file(), fileType);
        entity.setMediaUrl(path);

        var savedEntity = repository.save(entity);
        entityManager.refresh(savedEntity);
        return new ResponseDto<MediaDto, Media>(mapper.toDto(savedEntity), savedEntity);
    }
/*
    @Override
    public ResponseDto<MediaDto, Media> update(Long id, MediaDto.Update dto) {
        var fileType = dto.mimeType().startsWith("video/") ? FileType.VIDEO : FileType.IMAGE;
        var path = fileStorageService.store(dto.file(), fileType);
        dto = new MediaDto.Update(id, dto.postId(),dto.order(),dto.mimeType(),null,path);
        return super.update(id, dto);
    }*/
}