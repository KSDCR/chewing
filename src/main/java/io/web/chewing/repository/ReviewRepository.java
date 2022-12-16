package io.web.chewing.repository;
import io.web.chewing.Entity.Review;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.ReviewDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Object> {



    @Query("select r FROM Review r WHERE r.store_name=:store_name")
    Page<Review> findReviewByStore(@Param("1") Store store_name, Pageable pageable);

    @Query("select r FROM Review r WHERE r.member_id=:member_id ORDER BY r.id")
    Page<Review> findReviewByMember(Long member_id, Pageable pageable);




    @Query("select r FROM Review r WHERE r.store_name=:store_name")
    List<ReviewDto> reviewList(String store_name);

    @Query("select r FROM Review r WHERE r.member_id=:member_id ORDER BY r.id")
    List<ReviewDto> ReviewByMember(String member_id);


    @Query("select count(r) from Review r WHERE r.store_name=:store_name")
    int countAll(String store_name);
}
