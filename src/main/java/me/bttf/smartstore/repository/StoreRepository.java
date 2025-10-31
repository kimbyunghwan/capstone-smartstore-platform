package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
