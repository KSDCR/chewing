package io.web.chewing.repository;

import io.web.chewing.Entity.Categories;
import io.web.chewing.Entity.Store;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface StoreRepository extends JpaRepository<Store,Object> {

    @Query(value = "select distinct s from Store s ",
            countQuery = "select count(s) from Store s")
    Page<Store> findAllStores(Pageable pageRequest);

    @Query(value = "select distinct s from Store s " +
            "where s.name like %:keyword%",
            countQuery = "select count(s) from Store s where s.name like %:keyword%")
    Page<Store> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"categoriesSet"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Store> findByName(String name);



//    @Query(value = "select distinct s from Store s " +
//            "left join fetch s.category c " +
//            "where c.name like %:category%",
//            countQuery = "select count(s) from Seller s " +
//                    "where s.category.name like %:category%")
   // Page<Store> findAllByCategory(@Param("category") String category, Pageable pageable);


    //Page<Store> findByNameContaining(String keyword, Pageable pageable);

    //boolean existsByName(String name);

//    @Query("")
//    void insertFile(Long id, String fileName);


}
