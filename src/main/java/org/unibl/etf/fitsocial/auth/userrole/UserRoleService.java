package org.unibl.etf.fitsocial.auth.userrole;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;

@Service
@Transactional
public class UserRoleService extends BaseSoftDeletableServiceImpl<
    UserRole,
    UserRoleDto,
    UserRoleDto.List,
    UserRoleDto.Update,
    UserRoleDto.Create,
    Long
> {
    public UserRoleService(
        BaseSoftDeletableRepository<UserRole, Long> repository,
        IMapper<UserRole, UserRoleDto, UserRoleDto.List, UserRoleDto.Update, UserRoleDto.Create> mapper
    ) {
        super(repository, mapper);
    }
}