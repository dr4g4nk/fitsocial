package org.unibl.etf.fitsocial.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.Optional;

@NoRepositoryBean
public interface BaseSoftDeletableRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    Page<T> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<T> findByIdAndDeletedAtIsNull(ID id);

    boolean existsByIdAndDeletedAtIsNull(ID id);
}
