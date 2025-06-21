package org.unibl.etf.fitsocial.auth.user;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserRepository extends BaseSoftDeletableRepository<User,Long> {
    List<User> findAllByIdInAndDeletedAtIsNull(List<Long> ids);
    User findByUsernameAndDeletedAtIsNull(@Param("username") String username);

    boolean existsByUsernameAndDeletedAtIsNull(String username);
    boolean existsByEmailAndDeletedAtIsNull(String email);
}