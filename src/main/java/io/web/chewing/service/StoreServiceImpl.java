package io.web.chewing.service;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.mapper.StoreMapper;
import io.web.chewing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    //private final StoreMapper storeMapper;

    @Override
    public StoreDto get(Long id) {
        Optional<Store> result = storeRepository.findById(id);
        Store store = result.orElseThrow();
        return modelMapper.map(store, StoreDto.class);
    }

    @Override
    public Page<StoreDto> list(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Store> stores = storeRepository.findAllStores(pageRequest);

        return stores.map(store -> StoreDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .phone(store.getPhone())
                .detail(store.getDetail())
                .open_time(store.getOpen_time())
                .close_time(store.getClose_time())
                .build());
    }

    @Override
    public Page<StoreDto> listByKeyword(int page, int size, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Store> stores = storeRepository.findAllByKeyword(keyword,pageRequest);

        return stores.map(store -> StoreDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .phone(store.getPhone())
                .detail(store.getDetail())
                .open_time(store.getOpen_time())
                .close_time(store.getClose_time())
                .build());
    }

    @Override
    public Page<StoreDto> listByCategory(int page, int size, String category) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Store> stores = storeRepository.findAllByCategory(category,pageRequest);

        return stores.map(store -> StoreDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .phone(store.getPhone())
                .detail(store.getDetail())
                .open_time(store.getOpen_time())
                .close_time(store.getClose_time())
                .build());
    }

    @Override
    public PageDto page(Page<StoreDto> page, String keyword, String category) {

        return PageDto.builder()
                .total(page.getTotalPages())
                .number(page.getNumber())
                .keyword(keyword)
                .category(category)
                .build();
    }

    @Override
    @Transactional
    public Long register(StoreDto storeDto, MultipartFile multipartFile) {
        log.info("============> " + multipartFile);
        Store store = modelMapper.map(storeDto, Store.class);
        log.info(String.valueOf(storeDto));

        if (multipartFile != null && multipartFile.getSize() > 0) {
            // DB에 파일 정보 저장
            log.info("============> " + multipartFile.getOriginalFilename());
            //store.setFile(file.getOriginalFilename());

            // 파일 저장
//            File folder = new File("C:\\Users\\dlfma\\Desktop\\2022\\chewing\\upload\\store" + storeDto.getId());
//            folder.mkdirs();
//            File dest = new File(folder, file.getOriginalFilename());
//
//            try {
//                file.transferTo(dest);
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
        }

        // DB에 매장 정보 저장
        Long id = storeRepository.save(store).getId();
        return id;
    }

    @Override
    public void update(StoreDto storeDto) {
        Optional<Store> result = storeRepository.findById(storeDto.getId());
        Store store = result.orElseThrow();
        store.change(storeDto.getName(), storeDto.getDetail(), storeDto.getAddress(), storeDto.getPhone(), storeDto.getFile(), storeDto.getOpen_time(), storeDto.getClose_time());
        storeRepository.save(store);
    }

    @Override
    public void remove(Long id) {
        storeRepository.deleteById(id);
    }
}
