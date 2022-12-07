package io.web.chewing.service;

import io.web.chewing.Entity.Categories;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.PageResponseDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StoreService {

    StoreDto get(Long id);

    //PageResponseDto<StoreDto> list(PageRequestDto pageRequestDto);
    List<StoreDto> list();
    Long register(StoreDto storeDto, MultipartFile multipartFile);

    void update(StoreDto storeDto);

    void remove(Long id);




//    @Autowired
//    private StoreRepository repo;
//
//    public Optional<Store> get(Long id) {
//        return repo.findById(id);
//    }
//
////    public StoreDto get(Long id) {
////        return repo.findById(id);
////    }
//
//    public List<Store> getList() {
//        return repo.findAll();
//    }
//
//    public void register(StoreDto storeDto) {
//
//        Store store = Store.builder()
//                .name(storeDto.getName())
//                .address(storeDto.getAddress())
//                .phone(storeDto.getPhone())
//                .detail(storeDto.getDetail())
//                .openTime(LocalDateTime.now())
//                .closeTime(LocalDateTime.now())
//                .file("NotFound")
//                .build();
//
//        store.addCategories(Categories.Chinese);
//
//        repo.save(store);
//    }
//
//    public void update(StoreDto storeDto) {
//
//
//    }
}
