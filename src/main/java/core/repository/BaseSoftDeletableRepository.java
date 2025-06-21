package core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseSoftDeletableRepository<T, ID> extends BaseRepository<T, ID> {
    Optional<T> findByIdAndDeletedAtIsNull(ID id);
    Page<T> findAllByDeletedAtIsNull(Pageable pageable);
    boolean existsByIdAndDeletedAtIsNull(ID id);
}
