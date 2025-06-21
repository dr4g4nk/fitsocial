package org.unibl.etf.fitsocial.auth.userrole;

import core.repository.BaseSoftDeletableRepository;
import java.util.List;

public interface UserRoleRepository extends BaseSoftDeletableRepository<UserRole,Long> {
    List<UserRole> findByUserIdAndDeletedAtIsNull(Long userId);
}