package io.web.chewing.mapper.review;


import io.web.chewing.domain.ReviewDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface ReviewMapper {

    ReviewDto select();

    List<ReviewDto> findReviewByStore(String store,int offset, int records);

    int countReviewByStore(String store);

    List<ReviewDto> findReviewByMember(String member_nickname, int offset, int records);

    int countReviewByMember(String member_nickname);


    int update(ReviewDto review);
}
