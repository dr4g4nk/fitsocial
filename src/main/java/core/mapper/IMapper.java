package core.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import core.dto.IBasicDto;
import core.dto.ICreateDto;
import core.dto.IListDto;
import core.dto.IUpdateDto;

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
