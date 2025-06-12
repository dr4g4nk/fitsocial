package org.unibl.etf.fitsocial.mapper.base;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.unibl.etf.fitsocial.dto.base.IBasicDto;
import org.unibl.etf.fitsocial.dto.base.ICreateDto;
import org.unibl.etf.fitsocial.dto.base.IListDto;
import org.unibl.etf.fitsocial.dto.base.IUpdateDto;

public interface IMapper<T, Dto extends IBasicDto, ListDto extends IListDto, UpdateDto extends IUpdateDto, CreateDto extends ICreateDto> {
    @Named("detailed")
    Dto toDto(T entity);

    @Named("basic")
    ListDto toListDto(T entity);

    T fromCreateDto(CreateDto dto);

    T toEntity(Dto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    T partialUpdate(UpdateDto dto, @MappingTarget T entity);
}
