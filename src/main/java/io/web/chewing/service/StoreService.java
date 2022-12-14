package io.web.chewing.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.mapper.StoreMapper;
import io.web.chewing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StoreService {
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;

    private final AmazonS3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final StoreMapper storeMapper;

    public StoreDto get(Long id) {
        Optional<Store> result = storeRepository.findById(id);
        Store store = result.orElseThrow();
        StoreDto storeReview = storeMapper.getStoreReviewInfo(id);

        return StoreDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .phone(store.getPhone())
                .detail(store.getDetail())
                .open_time(store.getOpen_time())
                .close_time(store.getClose_time())
                .file(store.getFile())
                .rate(storeReview.getRate())
                .reviewCnt(storeReview.getReviewCnt())
                .build();
    }

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
                .file(store.getFile())
                .build());
    }

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
                .file(store.getFile())
                .build());
    }

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
                .file(store.getFile())
                .build());
    }

    public PageDto page(Page<StoreDto> page, String keyword, String category) {

        return PageDto.builder()
                .total(page.getTotalPages())
                .number(page.getNumber())
                .keyword(keyword)
                .category(category)
                .build();
    }

    public Long register(StoreDto storeDto, MultipartFile multipartFile) {
        Store store = modelMapper.map(storeDto, Store.class);
        log.info("============> " + String.valueOf(storeDto));

        if (multipartFile != null && multipartFile.getSize() > 0) {
            // DB에 파일 정보 저장
            log.info("============> " + multipartFile.getOriginalFilename());
            store.setFile(multipartFile.getOriginalFilename());

            // S3에 실제 파일 저장
            uploadFile(storeDto.getName(), multipartFile);
        }

        // DB에 매장 정보 저장
        Long id = storeRepository.save(store).getId();
        return id;
    }

    /* 파일 업로드 - S3 */
    private void uploadFile(String name, MultipartFile multipartFile) {
        try {
            // 키 생성
            String key = "chewing/store/" + name + "/" + multipartFile.getOriginalFilename();
            log.info("key ============> " + key);
            InputStream inputStream = multipartFile.getInputStream();

            // 파일 올리기
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            s3Client.putObject(bucketName, key, inputStream, metadata);
            log.info("============= 파일 올림 ============");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update(StoreDto storeDto) {
        Optional<Store> result = storeRepository.findById(storeDto.getId());
        Store store = result.orElseThrow();
        store.change(storeDto.getName(), storeDto.getDetail(), storeDto.getAddress(), storeDto.getPhone(), storeDto.getFile(), storeDto.getOpen_time(), storeDto.getClose_time());
        storeRepository.save(store);
    }

    public void remove(Long id) {
        storeRepository.deleteById(id);
    }
}
