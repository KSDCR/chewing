package io.web.chewing.mapper.review;


import io.web.chewing.domain.ReviewDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface ReviewMapper {

    default ReviewDto select(Long id){
        return select(id);
    };

    List<ReviewDto> findReviewByStore(String store_name,int offset, int records);

    int countReviewByStore(String store_name);

    List<ReviewDto> findReviewByMember(String member_nickname, int offset, int records);

    int countReviewByMember(String member_nickname);


    int update(ReviewDto review);

    void insertFile(Long id, String fileName);

    void deleteFileByReviewIdAndFileName(Long id, String fileName);
}
