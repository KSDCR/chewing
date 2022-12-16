package io.web.chewing.mapper;

import io.web.chewing.domain.StoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreMapper {

    StoreDto getStoreReviewInfo(String store_name);

    Long getLikeByStoreAndMember(String storeName, String nickname);
}
