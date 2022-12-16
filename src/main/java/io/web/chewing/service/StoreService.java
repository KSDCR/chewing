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
import java.util.Map;
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
        log.info("storeReview ====================> {}", storeReview);
        if (storeReview != null) {
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
        return modelMapper.map(store, StoreDto.class);
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

        // DB에 매장 정보 저장
        Long id = storeRepository.save(store).getId();

        if (multipartFile != null && multipartFile.getSize() > 0) {
            // DB에 파일 정보 저장
            log.info("============> " + multipartFile.getOriginalFilename());
            storeRepository.updateFileName(id, multipartFile.getOriginalFilename());

            // S3에 실제 파일 저장
            uploadFile(id, multipartFile);
        }

        return id;
    }

    /* 파일 업로드 - S3 */
    private void uploadFile(Long id, MultipartFile multipartFile) {
        try {
            // 키 생성
            String key = "chewing/store/" + id + "/" + multipartFile.getOriginalFilename();
            log.info("key ============> " + key);
            InputStream inputStream = multipartFile.getInputStream();
            // 파일 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            s3Client.putObject(bucketName, key, inputStream, metadata);
            log.info("============= 파일 업로드 ============");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /* 파일 삭제 - S3 */
    private void deleteFile(Long id, String fileName) {
        // 키 생성
        String key = "chewing/store/" + id + "/" + fileName;
        log.info("key ============> " + key);
        // 파일 삭제
        s3Client.deleteObject(bucketName, key);
        log.info("============= 파일 삭제 ============");
    }

    public void update(StoreDto storeDto, MultipartFile addImage, String removeImage) {
        Optional<Store> result = storeRepository.findById(storeDto.getId());
        Store store = result.orElseThrow();
        log.info("removeImage ===========> {} / addImage ============> {}", removeImage, addImage);

        storeDto.setFile(removeImage);
        log.info("storeDto ========> {}", storeDto);
        // 파일 변경
        if (addImage != null && addImage.getSize() > 0) {
            log.info("----------- 파일 변경 ---------");
            // 변경 파일 정보 저장
            storeDto.setFile(addImage.getOriginalFilename());

            if (removeImage != null) { // 파일이 있는 경우
                log.info("----------- 기존 파일 삭제 ---------");
                // S3에서 기존 파일 삭제
                deleteFile(storeDto.getId(), removeImage);
            }
            // S3에 실제 파일 저장
            uploadFile(storeDto.getId(), addImage);
        }
        
        // DB의 매장 정보 수정
        store.change(storeDto.getName(), storeDto.getDetail(), storeDto.getAddress(), storeDto.getPhone(), storeDto.getFile(), storeDto.getOpen_time(), storeDto.getClose_time());
        storeRepository.save(store);
    }

    public void remove(Long id) {
        Optional<Store> result = storeRepository.findById(id);
        Store store = result.orElseThrow();
        if (store.getFile() != null) { // 파일이 있는 경우
            // S3에서 기존 파일 삭제
            deleteFile(store.getId(), store.getFile());
        }
        // DB에서 매장 삭제
        storeRepository.deleteById(id);
    }

    public Store getByName(String name) {
        return storeRepository.findByName(name);
    }

}
