package io.web.chewing.repository;

import io.web.chewing.Entity.Store;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store,Object> {

    @Query(value = "select distinct s from Store s ",
            countQuery = "select count(s) from Store s")
    Page<Store> findAllStores(Pageable pageRequest);

    @Query(value = "select distinct s from Store s " +
            "where s.name like %:keyword%",
            countQuery = "select count(s) from Store s where s.name like %:keyword%")
    Page<Store> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

   @Query(value = "select distinct s from Store s " +
            "left join Store_categories c " +
            "ON s.id = c.store.id " +
            "where c.category like %:category%")
    Page<Store> findAllByCategory(@Param("category") String category, Pageable pageable);

    @Modifying
    @Query("UPDATE Store s set s.file = :file where s.id = :id")
    void updateFileName(@Param("id") Long id, @Param("file") String file);
}
