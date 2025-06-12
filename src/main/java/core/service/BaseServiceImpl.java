package org.unibl.etf.fitsocial.service.base;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.fitsocial.dto.base.*;
import org.unibl.etf.fitsocial.mapper.base.IMapper;

import java.io.Serializable;

public abstract class BaseServiceImpl<T, Dto extends IBasicDto, ListDto extends IListDto, UpdateDto extends IUpdateDto, CreateDto extends ICreateDto, ID extends Serializable> implements IBaseService<T, Dto, ListDto, UpdateDto, CreateDto, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final IMapper<T, Dto, ListDto, UpdateDto, CreateDto> mapper;


    public BaseServiceImpl(JpaRepository<T, ID> repository, IMapper<T, Dto, ListDto, UpdateDto, CreateDto> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ResponseDto<PageResponseDto<ListDto>, T> findAll(Pageable pageable) {
        return new ResponseDto<PageResponseDto<ListDto>,T>(new PageResponseDto<>(repository.findAll(pageable).map(mapper::toListDto)));
    }

    @Override
    public ResponseDto<Dto, T> findById(ID id) {
        var optional = repository.findById(id);
        return new ResponseDto<Dto, T>(optional.map(mapper::toDto).orElse(null), optional.orElse(null));
    }

    @Override
    public ResponseDto<Dto, T> save(CreateDto dto) {
        T entity = mapper.fromCreateDto(dto);
        var savedEntity = repository.save(entity);
        return new ResponseDto<Dto, T>(mapper.toDto(savedEntity), savedEntity);
    }

    @Override
    public ResponseDto<Dto, T> update(ID id, UpdateDto dto) {
        var opt = repository.findById(id);
        if (opt.isEmpty()) {
            return new ResponseDto<Dto, T>("Entity not found");
        }
        T entity = opt.get();
        entity = mapper.partialUpdate(dto, entity);
        var updatedEntity = repository.save(entity);
        return new ResponseDto<>(mapper.toDto(updatedEntity), updatedEntity);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);

    }
}