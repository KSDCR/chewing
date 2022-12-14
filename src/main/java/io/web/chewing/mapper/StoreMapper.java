package io.web.chewing.mapper;

import io.web.chewing.domain.StoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StoreMapper {
    StoreDto getStoreById(Long id, String nickname);
    StoreDto getStoreReviewInfo(String store_name);

    int getLikeByStoreAndMember(String storeName, String nickname);
    int insertLike(String storeName, String nickname);
    int deleteLike(String storeName, String nickname);
    int countLikeByStore(String storeName);
    int deleteLikeByStore(String storeName);

    List<String> getStoreNameByMember(String nickname);

    List<String> getTop10Store();

    List<StoreDto> getRandomStore();
}
