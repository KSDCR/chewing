package io.web.chewing.service;

import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.StoreDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {

    StoreDto get(Long id);

    Page<StoreDto> list(int page, int size);
    Page<StoreDto> listByKeyword(int page, int size, String keyword);
    //Page<StoreDto> listByCategory(int page, int size, String category);
    PageDto page(Page<StoreDto> stores, String keyword, String category);
    //Page<StoreDto> page(int page, int size);
    //Page<Store> list(Pageable pageable);

   // PageResponseDto<StoreDto> list(PageRequestDto pageRequestDto);
    //List<StoreDto> list();
    //Page<StoreDto> list(Pageable);
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
