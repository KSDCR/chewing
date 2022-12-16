package io.web.chewing.mapper;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.StoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreMapper {

    StoreDto getStoreReviewInfo(Long id);

}
