package io.web.chewing.repository;

import io.web.chewing.Entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Object> {

    //boolean existsByName(String name);
}
