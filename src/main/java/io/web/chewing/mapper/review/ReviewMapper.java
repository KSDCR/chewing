package io.web.chewing.mapper.review;


import io.web.chewing.domain.ReviewDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface ReviewMapper {

    ReviewDto select(Long id, String member_nickname);
    default ReviewDto select(Long id){
        return select(id, null);
    };

    List<ReviewDto> findReviewByStore(String store_name,int offset, int records);

    int countReviewByStore(String store_name);

    List<ReviewDto> findReviewByMember(String member_nickname, int offset, int records);

    int countReviewByMember(String member_nickname);


    int update(ReviewDto review);

    void insertFile(Long id, String fileName);

    void deleteFileByReviewIdAndFileName(Long id, String fileName);

    int deleteFileByBoardId(Long id);

    int delete(Long id);

    ReviewDto findReviewById(Long id);


}
