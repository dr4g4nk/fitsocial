package org.unibl.etf.fitsocial.auth.fcmtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {

    @Query("select f from FcmToken f where f.token = :token")
    Optional<FcmToken> findByToken(@Param("token") String token);

    @Query("""
            select f.token from FcmToken f where f.timestamp in
            (
            select MAX(f2.timestamp) from FcmToken f2 where f2.user.Id in :userId group by f2.user.id
            )
            """)
    List<String> findAllByUserIdInLatest(@Param("userId") List<Long> userId);

    @Transactional
    @Modifying
    void deleteByUserId(@Param("userId") Long userId);

}
