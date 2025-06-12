package org.unibl.etf.fitsocial.repository;

import org.springframework.data.jpa.repository.Query;
import org.unibl.etf.fitsocial.entity.User;
import org.unibl.etf.fitsocial.entity.UserRole;
import core.repository.BaseSoftDeletableRepository;

import java.time.Instant;
import java.util.List;

public interface UserRoleRepository extends BaseSoftDeletableRepository<UserRole,Long> {
    List<UserRole> findByUserIdAndDeletedAtIsNull(Long userId);

    Long user(User user);
}