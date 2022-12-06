package io.web.chewing.repository;
import io.web.chewing.Entity.Review;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Object> {

//    @Query("select r FROM Review r ORDER BY r.id")
//    List<ReviewDto> findByStore();


    int save(ReviewDto review);


/*boolean existsByReviewForm(Review review);


   @Modifying(clearAutomatically = true)
    @Query("update Review r set r.likes = r.likes + :likeCount where r.id = :#{#review.id}")
    void increaseLikes(Review review, int likeCount);*/


//    int deleteById(long id);
//
//    int update(Review review);

//    Page<Review> searchAll(String[] types, String keyword, Pageable pageable);

//    Page<Review> selectReviewByStoreId(Pageable pageable);

    @Query("select r FROM Review r WHERE r.store.id=:store")
    Page<Review> findReviewByStore(Long store, Pageable pageable);
//
//    @Query("select r FROM Review r WHERE r.store.id=:store")
//    Page<Review> findReviewByStore(Long store);

    @Query("select r FROM Review r WHERE r.member_id=:member_id ORDER BY r.id")
    Page<Review> findReviewByMember(Long member_id, Pageable pageable);


//    @Query("select r FROM Review r WHERE r.store=:store")
//    List<ReviewDto> reviewList(Store store);

    @Query("select r FROM Review r WHERE r.store.id=:store")
    List<ReviewDto> findByStore(Long store);

//    @Query("select r FROM Review r WHERE r.store.id=:store")
//    List<ReviewDto> reviewList(Long store);

    @Query("select r FROM Review r WHERE r.store.id=:store")
    Page<Review> listOfStore(Long store, Pageable pageable);

    @Query("select r FROM Review r WHERE r.store.id=:store")
    List<ReviewDto> reviewList(Long store);

    @Query("select r FROM Review r WHERE r.member_id=:member_id ORDER BY r.id")
    List<ReviewDto> ReviewByMember(Long member_id);


//    @Query("select r FROM Review r WHERE r.store.id=:store")
//    int countAll(Long store);
//
//
//    @Query("select r FROM Review r WHERE r.store.id=:store")
//    List<ReviewDto> listByStore(Long store);

//    List<ReplyDTO> selectReplyByBoardId(int boardId, String username);

//
//    List<ReviewDto> findByStore(StoreDto store);
}
