package org.unibl.etf.fitsocial.conversation.attachment;

import core.dto.ResponseDto;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.entity.FileType;
import org.unibl.etf.fitsocial.service.FileStorageService;

@Service
@Transactional
public class AttachmentService extends BaseSoftDeletableServiceImpl<
        Attachment,
        AttachmentDto,
        AttachmentDto.List,
        AttachmentDto.Update,
        AttachmentDto.Create,
        Long
        > {

    private FileStorageService fileStorageService;

    public AttachmentService(
            BaseSoftDeletableRepository<Attachment, Long> repository,
            IMapper<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create> mapper, FileStorageService fileStorageService
    ) {
        super(repository, mapper);
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResponseDto<AttachmentDto, Attachment> save(AttachmentDto.Create dto) {
        var fileType = FileType.DOCUMENT;

        if (dto.contentType().startsWith("video")) fileType = FileType.VIDEO;
        else if (dto.contentType().startsWith("image")) fileType = FileType.IMAGE;
        var attachment = mapper.fromCreateDto(dto);

        var path = fileStorageService.store(dto.file(), fileType);
        if(fileType.equals(FileType.VIDEO)){
            var thumbnailPath = fileStorageService.storeThumbnail(path);
            attachment.setThumbnailUrl(thumbnailPath);
        }
        attachment.setFileUrl(path);

        var savedEntity = repository.save(attachment);
        entityManager.refresh(savedEntity);
        return new ResponseDto<>(mapper.toDto(savedEntity), savedEntity);
    }
}