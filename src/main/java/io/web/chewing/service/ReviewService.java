package io.web.chewing.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Review;
import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.*;
import io.web.chewing.mapper.review.ReviewMapper;
import io.web.chewing.repository.ReviewRepository;
import io.web.chewing.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private  ReviewRepository reviewRepository;
    @Autowired
    private  StoreRepository storeRepository;
    @Autowired
    private  ReviewMapper reviewMapper;

    @Autowired
    private AmazonS3Client s3Client;


    @Value("${aws.s3.bucket}")
    private  String bucketName;


    public List<ReviewDto> myReviewList(String member_nickname) {

        return reviewRepository.ReviewByMember(member_nickname);
    }


    public PageResponseDto<ReviewDto> listbefore(Long store/*String member,*/ ,PageRequestDto pageRequestDto) {

        Optional<Store> store1 = storeRepository.findById(store);

        Store store2 = Store.builder()
                .id(store1.get().getId()).build();


        Pageable pageable = pageRequestDto.getPageable("store");

        Page<Review> result = reviewRepository.findReviewByStore(store2, pageable);

//        Page<Review> result = reviewRepository.findReviewByStore(store);

        List<ReviewDto> dtoList = result.getContent().stream()
                .map(review -> modelMapper.map(review,ReviewDto.class)).collect(Collectors.toList());


        return PageResponseDto.<ReviewDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }


    public List<ReviewDto> list(String store_name, int page, PageInfo pageInfo) {

        int records = 10;
        int offset = (page - 1) * records;


        int countAll = reviewRepository.countAll(store_name);
        int lastPage = (countAll - 1) / records + 1;


        int leftPageNumber = (page - 1) / 10 * 10 + 1;
        int rightPageNumber = leftPageNumber + 9;
        int currentPageNumber = page;
        rightPageNumber = Math.min(rightPageNumber, lastPage);
        boolean hasNextPageNumber = page <= ((lastPage-1)/10*10);

        pageInfo.setHasNextPageNumber(hasNextPageNumber);
        pageInfo.setCurrentPageNumber(currentPageNumber);
        pageInfo.setLeftPageNumber(leftPageNumber);
        pageInfo.setRightPageNumber(rightPageNumber);
        pageInfo.setLastPageNumber(lastPage);
////        Pageable pageable = pageRequestDto.getPageable("id");
////
////        Page<Review> result = reviewRepository.findReviewByStore(store,/* member,*/ pageable);
////        Page<Review> result = reviewRepository.findReviewByStore(store,/* member,*/ pageable);
////
////        List<ReviewDto> dtoList = result.getContent().stream()
////                .map(review -> modelMapper.map(review,ReviewDto.class)).collect(Collectors.toList());
////        return PageResponseDto.<ReviewDto>withAll()
////                .pageRequestDto(pageRequestDto)
////                .dtoList(dtoList)
////                .total((int)result.getTotalElements())
////                .build();
//
//        return reviewRepository.listByStore(store);
//    }

        return reviewRepository.listByStore(store_name);
    }

    public String register(ReviewDto reviewDto,
                           @AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                           String store_name,
                           MultipartFile[] files) throws NotFoundException {

        Optional<Store> getStore = storeRepository.findByName(store_name);
        Store findStore = getStore.orElseThrow();

        log.info(findStore.getName());

        Review review = reviewDto.toEntity(findStore);
        Member loadMember = getBuild(authMemberDTO);
        review.assignUser(loadMember);

        log.info("===========================ls");
        String storeSave = reviewRepository.save(review).getStore_name().getName();

        log.info(String.valueOf(review.getId()));

        for (MultipartFile file : files) {
            log.info("abcd"+file.getOriginalFilename());

            if (file != null && file.getSize() > 0) {
                reviewMapper.insertFile(review.getId(), file.getOriginalFilename());


                uploadFile(review.getId(), file);
            }
        }

        return storeSave;
    }


    private static Member getBuild(AuthMemberDTO authMemberDTO) {
        return Member.builder()
                .id(authMemberDTO.getId())
                .nickname(authMemberDTO.getNickname())
                .password(authMemberDTO.getPassword())
                .delete_yn('0')
                .email(authMemberDTO.getEmail())
                .provider(authMemberDTO.getProvider())
                .build();
    }

    public Member memberBuild(AuthMemberDTO authMemberDTO) {
        return modelMapper.map(authMemberDTO, Member.class);
    }


//            ObjectMetadata objMeta = new ObjectMetadata();
//            objMeta.setContentLength(file.getInputStream().available());
//
//            s3Client.putObject(bucketName, key, file.getInputStream(), objMeta);

            /*return s3Client.getUrl(bucketName, key).toString();*/




    private void uploadFile(Long id, MultipartFile file) {
        try {
            // S3에 파일 저장
            // 키 생성
            String key = "chewing/review/" + id + "/" + file.getOriginalFilename();

            // putObjectRequest
            /*File file1 = multipartToFile(file);*/
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file1);

            InputStream inputStream = file.getInputStream();
            // object(파일) 올리기
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            s3Client.putObject(bucketName, key, inputStream,metadata);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void modify(ReviewDto reviewDto, MultipartFile[] addFiles, List<String> removeFiles) {
        log.info(String.valueOf(reviewDto.getId()));
        Optional<Review> result = reviewRepository.findById(reviewDto.getId());
        log.info("Optional<Review>:"+String.valueOf(result));
        Review review = result.orElseThrow();

        log.info("modify"+review);

        review.change(reviewDto.getContent());

        if (removeFiles != null)
            for (String fileName : removeFiles) {


                reviewMapper.deleteFileByReviewIdAndFileName(reviewDto.getId(), fileName);

                deleteFile(reviewDto.getId(), fileName);

            };

        for (MultipartFile file : addFiles) {

            reviewMapper.deleteFileByReviewIdAndFileName(reviewDto.getId(), file.getOriginalFilename());


            if (file != null && file.getSize() > 0) {

                reviewMapper.insertFile(reviewDto.getId(), file.getOriginalFilename());

                uploadFile(reviewDto.getId(), file);


            }
        }


        reviewRepository.save(review);

    }



    public void modifybefore(ReviewDto reviewDto) {
        log.info(String.valueOf(reviewDto.getId()));

//        reviewRepository.save(review);

        reviewMapper.update(reviewDto);
    }

    public void remove(Long id) {
        ReviewDto review = reviewMapper.select(id);
        List<String> fileNames = review.getFileName();

        if (fileNames != null) {
            for (String fileName : fileNames) {
                deleteFile(id, fileName);
            }
        }

//        reviewMapper.deleteLikeByReviewId(id);
//
//        reviewMapper.deleteFileByReviewId(id);
//
//        reviewMapper.deleteByReviewId(id);
//


        reviewRepository.deleteById(id);

    }

    private void deleteFile(Long id, String fileName) {
        String key = "chewing/review/" + id + "/" + fileName;

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName,key);
/*        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        .deleteObject(deleteObjectRequest);*/
        s3Client.deleteObject(deleteObjectRequest);
    }
//
//    public Map<String, Object> updateLike(String boardId, String memberId) {
//
//        Map<String, Object> map = new HashMap<>();
//
//        int cnt = boardMapper.getLikeByBoardIdAndMemberId(boardId, memberId);
//
//        if(cnt == 1) {
//            boardMapper.deleteLike(boardId, memberId);
//            map.put("current", "not liked");
//        }else {
//            boardMapper.insertLike(boardId, memberId);
//            map.put("current", "liked");
//        }
//
//        int countAll = boardMapper.countLikeByBoardId(boardId);
//        map.put("count", countAll);
//
//        return map;
//    }
    public PageResponseDto<ReviewDto> myList(Long member, PageRequestDto pageRequestDto) {

        Pageable pageable = pageRequestDto.getPageable("id");

        Page<Review> result = reviewRepository.findReviewByMember(member, pageable);

        List<ReviewDto> dtoList = result.getContent().stream()
                .map(review -> modelMapper.map(review, ReviewDto.class)).collect(Collectors.toList());


        return PageResponseDto.<ReviewDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }


    public List<ReviewDto> reviewList(String store_name) {
        return reviewRepository.reviewList(store_name);
    }

    public ReviewDto get(Long id) {
        Optional<Review> result = reviewRepository.findById(id);

        Review review = result.orElseThrow();

        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        return reviewDto;
    }


    public List<ReviewDto> listReviewByStore(String store_name, int page, PageInfo pageInfo) {
        int records = 10;
        int offset = (page - 1) * records;


        int countAll = reviewMapper.countReviewByStore(store_name);
        int lastPage = (countAll - 1) / records + 1;

        log.info("==========="+countAll);

        int leftPageNumber = (page - 1) / 10 * 10 + 1;
        int rightPageNumber = leftPageNumber + 9;
        int currentPageNumber = page;
        rightPageNumber = Math.min(rightPageNumber, lastPage);
        boolean hasNextPageNumber = page <= ((lastPage-1)/10*10);

        pageInfo.setHasNextPageNumber(hasNextPageNumber);
        pageInfo.setCurrentPageNumber(currentPageNumber);
        pageInfo.setLeftPageNumber(leftPageNumber);
        pageInfo.setRightPageNumber(rightPageNumber);
        pageInfo.setLastPageNumber(lastPage);

        log.info("FFF"+reviewMapper.findReviewByStore(store_name,offset,records).toString());
        return reviewMapper.findReviewByStore(store_name, offset, records);
    }

    public List<ReviewDto> listReviewByMember(String member_nickname, int page, PageInfo pageInfo) {
        int records = 10;
        int offset = (page - 1) * records;

        int countAll = reviewMapper.countReviewByMember(member_nickname);
        int lastPage = (countAll - 1) / records + 1;

        log.info("==========="+countAll);

        int leftPageNumber = (page - 1) / 10 * 10 + 1;
        int rightPageNumber = leftPageNumber + 9;
        int currentPageNumber = page;
        rightPageNumber = Math.min(rightPageNumber, lastPage);
        boolean hasNextPageNumber = page <= ((lastPage-1)/10*10);

        pageInfo.setHasNextPageNumber(hasNextPageNumber);
        pageInfo.setCurrentPageNumber(currentPageNumber);
        pageInfo.setLeftPageNumber(leftPageNumber);
        pageInfo.setRightPageNumber(rightPageNumber);
        pageInfo.setLastPageNumber(lastPage);

        return reviewMapper.findReviewByMember(member_nickname, offset, records);
    }


//    public List<ReviewDto> getListOfStore(Long store, ReviewDto reviewDto, AuthMemberDTO authMemberDTO) throws NotFoundException {
//
//        Review review = reviewDto.toEntity();
//
//        Member loadMember = Member.builder()
//            .id(authMemberDTO.getId())
//            .nickname(authMemberDTO.getNickname())
//            .password(authMemberDTO.getPassword())
//            .delete_yn('0')
//            .email(authMemberDTO.getEmail())
//            .provider(authMemberDTO.getProvider())
//            .build();
//        review.assignUser(loadMember);
//
//        return reviewRepository.listOfBoard(store, review.getMember_id());
//
//
//    }
//
//        log.info("dd" + String.valueOf(reviewDto));
//    Review review = reviewDto.toEntity();
//    Member loadMember = Member.builder()
//            .id(authMemberDTO.getId())
//            .nickname(authMemberDTO.getNickname())
//            .password(authMemberDTO.getPassword())
//            .delete_yn('0')
//            .email(authMemberDTO.getEmail())
//            .provider(authMemberDTO.getProvider())
//            .build();
//        review.assignUser(loadMember);
//        log.info("===========================ls");
//    Long id = reviewRepository.save(review).getId();
//
//        return id;
        public File multipartToFile(MultipartFile mfile) throws IllegalStateException, IOException{
            File file = new File(mfile.getOriginalFilename());
            mfile.transferTo(file);
            return file;
        }
}
