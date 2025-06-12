package core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseSoftDeletableRepository<T, ID> extends BaseRepository<T, ID> {
    Optional<T> findByIdAndDeletedAtIsNull(ID id);

    boolean existsByIdAndDeletedAtIsNull(ID id);
}
