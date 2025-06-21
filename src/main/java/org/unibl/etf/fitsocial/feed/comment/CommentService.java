package org.unibl.etf.fitsocial.feed.comment;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.service.BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.feed.post.PostRepository;

@Service
@Transactional
public class CommentService extends BaseSoftDeletableServiceImpl<
    Comment,
    CommentDto,
    CommentDto.List,
    CommentDto.Update,
    CommentDto.Create,
    Long
> {
    protected CommentRepository repository;
    protected PostRepository postRepository;

    public CommentService(CommentRepository repository, PostRepository postRepository, IMapper<Comment, CommentDto, CommentDto.List, CommentDto.Update, CommentDto.Create> mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.postRepository = postRepository;
    }

    public ResponseDto<PageResponseDto<CommentDto.List>, Comment> findAllByPostId(Long postId, Pageable pageable) {
        boolean onlyPublic = true;
        var userDetails = getUserDetails();
        if (userDetails.isPresent()) onlyPublic = false;

        if (postRepository.existsByIdAndDeletedAtIsNullAndIsPublicTrueOrIsPublicEquals(postId, onlyPublic))
            return new ResponseDto<>(new PageResponseDto<>(repository.findAllByPostIdAndDeletedAtIsNull(postId, pageable).map(mapper::toListDto)));

        return new ResponseDto<>("Not found");

    }
}