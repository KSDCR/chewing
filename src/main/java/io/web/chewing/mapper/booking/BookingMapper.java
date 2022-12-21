package io.web.chewing.mapper.booking;


import io.web.chewing.domain.BookingDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface BookingMapper {

    default ReviewDto select(Long id){
        return select(id);
    };

    List<ReviewDto> findReviewByStore(String store_name,int offset, int records);

    int countReviewByStore(String store_name);

    List<BookingDTO> findBookingByMember(String member_nickname);

    int countBookingByMember(String member_nickname);


    int update(ReviewDto review);

    void insertFile(Long id, String fileName);

    void deleteFileByReviewIdAndFileName(Long id, String fileName);
}
