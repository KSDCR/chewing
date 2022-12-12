package io.web.chewing.service;

import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.StoreDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {

    StoreDto get(Long id);

    Page<StoreDto> list(int page, int size);
    Page<StoreDto> listByKeyword(int page, int size, String keyword);
    Page<StoreDto> listByCategory(int page, int size, String category);
    PageDto page(Page<StoreDto> stores, String keyword, String category);

    Long register(StoreDto storeDto, MultipartFile multipartFile);

    void update(StoreDto storeDto);

    void remove(Long id);
}
