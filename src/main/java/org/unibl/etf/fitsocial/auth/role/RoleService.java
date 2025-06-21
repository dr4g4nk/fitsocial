package org.unibl.etf.fitsocial.auth.role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class RoleService extends BaseSoftDeletableServiceImpl<
    Role,
    RoleDto,
    RoleDto.List,
    RoleDto.Update,
    RoleDto.Create,
    Long
> {
    public RoleService(
        BaseSoftDeletableRepository<Role, Long> repository,
        IMapper<Role, RoleDto, RoleDto.List, RoleDto.Update, RoleDto.Create> mapper
    ) {
        super(repository, mapper);
    }
}