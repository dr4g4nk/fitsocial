package core.service;

import core.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import core.entity.SoftDeletableEntity;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import java.io.Serializable;

public abstract class BaseSoftDeletableServiceImpl<T extends SoftDeletableEntity<ID>, Dto extends IBasicDto, ListDto extends IListDto, UpdateDto extends IUpdateDto, CreateDto extends ICreateDto, ID extends Serializable> extends BaseServiceImpl<T, Dto, ListDto, UpdateDto, CreateDto, ID> {

    protected BaseSoftDeletableRepository<T, ID> repository;

    public BaseSoftDeletableServiceImpl(BaseSoftDeletableRepository<T, ID> repository, IMapper<T, Dto, ListDto, UpdateDto, CreateDto> mapper) {
        super(repository, mapper);
        this.repository = repository;
    }

    protected Specification<T> specification = (root, query, builder) -> builder.isNull(root.get("deletedAt"));

    protected Specification<T> getSpecification(T  entity) {
        return (root, query, builder) -> null;
    }

    @Override
    public ResponseDto<PageResponseDto<ListDto>, T> findAll(Pageable pageable) {
        return new ResponseDto<PageResponseDto<ListDto>, T>(new PageResponseDto<ListDto>(repository.findAllByDeletedAtIsNull(pageable).map(mapper::toListDto)));
    }

    @Override
    public ResponseDto<Dto, T> findById(ID id) {
        var optionalEntity = repository.findByIdAndDeletedAtIsNull(id);
        return new ResponseDto<Dto, T>(optionalEntity.map(mapper::toDto).orElse(null), optionalEntity.orElse(null));
    }

    @Override
    public ResponseDto<Dto, T> update(ID id, UpdateDto dto) {
        var opt = repository.findByIdAndDeletedAtIsNull(id);
        if (opt.isEmpty()) {
            return new ResponseDto<Dto, T>("Entity not found");
        }
        T entity = opt.get();
        entity = mapper.partialUpdate(dto, entity);

        var updatedEntity = repository.save(entity);

        return new ResponseDto<Dto, T>(mapper.toDto(updatedEntity), updatedEntity);
    }
}