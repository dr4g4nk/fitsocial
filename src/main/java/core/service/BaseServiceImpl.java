package core.service;

import core.dto.*;
import core.repository.BaseRepository;
import core.util.CurrentUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import core.mapper.IMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.Optional;

public abstract class BaseServiceImpl<T, Dto extends IBasicDto, ListDto extends IListDto, UpdateDto extends IUpdateDto, CreateDto extends ICreateDto, ID extends Serializable> implements IBaseService<T, Dto, ListDto, UpdateDto, CreateDto, ID> {

    protected final BaseRepository<T, ID> repository;
    protected final IMapper<T, Dto, ListDto, UpdateDto, CreateDto> mapper;

    @PersistenceContext
    protected EntityManager entityManager;


    public BaseServiceImpl(BaseRepository<T, ID> repository, IMapper<T, Dto, ListDto, UpdateDto, CreateDto> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    protected Specification<T> getSpecification(T  entity) {
        return (root, query, builder) -> null;
    }

    protected Optional<CurrentUserDetails> getUserDetails(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) return Optional.empty();
        return Optional.of ((CurrentUserDetails) auth.getPrincipal());
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
    public ResponseDto<Dto, T> deleteById(ID id) {
        repository.deleteById(id);
        return new ResponseDto<>(true);
    }
}