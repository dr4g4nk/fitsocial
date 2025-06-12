package org.unibl.etf.fitsocial.repository;

import org.springframework.data.repository.query.Param;
import org.unibl.etf.fitsocial.entity.User;
import core.repository.BaseSoftDeletableRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseSoftDeletableRepository<User, Long> {
    List<User> findAllByIdInAndDeletedAtIsNull(List<Long> ids);
    User findByUsernameAndDeletedAtIsNull(@Param("username") String username);

    boolean existsByUsernameAndDeletedAtIsNull(String username);
    boolean existsByEmailAndDeletedAtIsNull(String email);
}