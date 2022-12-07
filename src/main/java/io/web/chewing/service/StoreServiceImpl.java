package io.web.chewing.service;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.File;
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
