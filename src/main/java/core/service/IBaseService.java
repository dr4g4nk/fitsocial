package org.unibl.etf.fitsocial.service.base;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.fitsocial.dto.base.PageResponseDto;
import org.unibl.etf.fitsocial.dto.base.ResponseDto;

public interface IBaseService<T , Dto, ListDto, UpdateDto, CreateDto, ID> {
    ResponseDto<PageResponseDto<ListDto>, T> findAll(Pageable pageable);
    ResponseDto<Dto, T> findById(ID id);
    ResponseDto<Dto, T> save(CreateDto dto);
    ResponseDto<Dto, T> update(ID id, UpdateDto dto);
    void deleteById(ID id);
}