package org.unibl.etf.fitsocial.auth.userrole;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibl.etf.fitsocial.auth.role.Role;

import java.util.List;

public interface UserRoleRepository extends BaseSoftDeletableRepository<UserRole,Long> {
    @Query("select u from UserRole u where u.user.id = :userId and u.deletedAt is null")
    List<UserRole> findByUserIdAndDeletedAtIsNull(@Param("userId") Long userId);


    @Query("select r from UserRole u join Role r on r.id = u.role.id  where u.user.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}