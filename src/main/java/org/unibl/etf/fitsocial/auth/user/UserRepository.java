package org.unibl.etf.fitsocial.auth.user;

import core.repository.BaseSoftDeletableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserRepository extends BaseSoftDeletableRepository<User,Long> {
    List<User> findAllByIdInAndDeletedAtIsNull(List<Long> ids);
    @Query("select u from User u where (u.username = :username or u.email = :username) and u.deletedAt is null")
    User findByUsernameAndDeletedAtIsNull(@Param("username") String username);

    boolean existsByUsernameAndDeletedAtIsNull(String username);
    boolean existsByEmailAndDeletedAtIsNull(String email);

    @Query("""
            select u from User u where u.id != :currentId and lower(concat(u.firstName, ' ', u.lastName)) like lower(concat('%', :value, '%'))
            """)
    Page<User> findAllByTextFilterWithoutCurrent(@Param("value") String value, @Param("currentId") Long currentId, Pageable pageable);
    
}