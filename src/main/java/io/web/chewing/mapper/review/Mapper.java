package io.web.chewing.mapper.review;


import io.web.chewing.domain.ReviewDto;
import org.springframework.stereotype.Repository;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface Mapper {

    ReviewDto select();

}
