package io.web.chewing.repository;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.StoreDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store,Object> {

//    @Query("select s from Store s where s.id = :id")
//    StoreDto get(Long id);
//
//    boolean existsByName(String name);
}