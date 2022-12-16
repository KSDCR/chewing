package io.web.chewing.repository;

import io.web.chewing.Entity.Store_Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreLikeRepository extends JpaRepository<Store_Like,Object> {

   /* @Query(countQuery = "select count(l) from Store_Like l " +
            "where l.store_likeID.name = :storeName and l.store_likeID.nickname = :nickname")*/

    //List<Store_like> findAllBy();
}
