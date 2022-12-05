package io.web.chewing.service;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;

    @Override
    public StoreDto get(Long id) {
        Optional<Store> result = storeRepository.findById(id);
        Store store = result.orElseThrow();
        return modelMapper.map(store, StoreDto.class);
    }

    @Override
    public List<StoreDto> list() {
        List<Store> result = storeRepository.findAll();
        List<StoreDto> dtoList = result.stream()
                .map(store -> modelMapper.map(store, StoreDto.class)).collect(Collectors.toList());

        return dtoList;
    }

//    @Override
//    public PageResponseDto<StoreDto> list(PageRequestDto pageRequestDto) {
//
//        String[] types = pageRequestDto.getTypes();
//        String keyword = pageRequestDto.getKeyword();
//        Pageable pageable = pageRequestDto.getPageable("id");
//
//        Page<Store> result = storeRepository.searchAll(types, keyword, pageable);
//
//        List<StoreDto> dtoList = result.getContent().stream()
//                .map(store -> modelMapper.map(store, StoreDto.class)).collect(Collectors.toList());
//
//
//        return PageResponseDto.<StoreDto>withAll()
//                .pageRequestDto(pageRequestDto)
//                .dtoList(dtoList)
//                .total((int)result.getTotalElements())
//                .build();
//
//    }

    @Override
    public Long register(StoreDto storeDto) {
        Store store = modelMapper.map(storeDto, Store.class);
        log.info(String.valueOf(storeDto));
        return storeRepository.save(store).getId();
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
