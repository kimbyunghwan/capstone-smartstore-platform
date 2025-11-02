package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByOwner_Id(Long memberId);

    @Query("SELECT s.id FROM Store s WHERE s.owner.id = :memberId")
    Optional<Long> findIdByOwner_Id(@Param("memberId") Long memberId);
}
