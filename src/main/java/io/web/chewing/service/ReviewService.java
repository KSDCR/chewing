package io.web.chewing.service;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Review;
import io.web.chewing.domain.MemberDto;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.PageResponseDto;
import io.web.chewing.domain.ReviewDto;
import io.web.chewing.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewService {

    private final ModelMapper modelMapper;

    private final ReviewRepository reviewRepository;

//    private ReviewDto convertToReviewDto(Review review) {
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE);
//        ReviewDto reviewDto = modelMapper
//                .map(review, ReviewDto.class);
//        return reviewDto;
//    }
//

//    private final MemberRepository memberRepository;

    public List<ReviewDto> myReviewList(Long member_id) {

        return reviewRepository.ReviewByMember(member_id);
    }

//    public List<ReviewDto> list(Long store) {
//
//        List<ReviewDto> result = reviewRepository.findByStore(store);
//
//        List<ReviewDto> dtos = result.stream()
//                .map(review -> modelMapper.map(review, ReviewDto.class))
//                .collect(Collectors.toList());

//        return reviewRepository.findByStore(store);

//        return reviewRepository
//                .findByStore(store)
//                .stream()
//                .map(review -> modelMapper.)
//                .collect(Collectors.toList());

//    return dtos;



//    }

    public PageResponseDto<ReviewDto> list(Long store,Long member_id, PageRequestDto pageRequestDto) {

        Pageable pageable = pageRequestDto.getPageable("store");

        Page<Review> result = reviewRepository.findReviewByStore(store, pageable);


        List<ReviewDto> dtoList = result.getContent().stream()
                .map(review -> modelMapper.map(review,ReviewDto.class)).collect(Collectors.toList());


        return PageResponseDto.<ReviewDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }

//    public PageResponseDto<ReviewDto> list(Long store/*String member,*/ /*PageRequestDto pageRequestDto*/) {
//
////        Pageable pageable = pageRequestDto.getPageable("store");
//
////        Page<Review> result = reviewRepository.findReviewByStore(store,/* member,*/ pageable);
//
//        Page<Review> result = reviewRepository.findReviewByStore(store);
//
//        List<ReviewDto> dtoList = result.getContent().stream()
//                .map(review -> modelMapper.map(review,ReviewDto.class)).collect(Collectors.toList());
//
//
//        return PageResponseDto.<ReviewDto>withAll()
////                .pageRequestDto(pageRequestDto)
//                .dtoList(dtoList)
//                .total((int)result.getTotalElements())
//                .build();
//
//    }

//    public List<ReviewDto> list(Long store, int page, PageInfo pageInfo) {
//
//        int records = 10;
//        int offset = (page - 1) * records;
//
//
//        int countAll = reviewRepository.countAll(store);
//        int lastPage = (countAll - 1) / records + 1;
//
//
//        int leftPageNumber = (page - 1) / 10 * 10 + 1;
//        int rightPageNumber = leftPageNumber + 9;
//        int currentPageNumber = page;
//        rightPageNumber = Math.min(rightPageNumber, lastPage);
//        boolean hasNextPageNumber = page <= ((lastPage-1)/10*10);
//
//        pageInfo.setHasNextPageNumber(hasNextPageNumber);
//        pageInfo.setCurrentPageNumber(currentPageNumber);
//        pageInfo.setLeftPageNumber(leftPageNumber);
//        pageInfo.setRightPageNumber(rightPageNumber);
//        pageInfo.setLastPageNumber(lastPage);
////        Pageable pageable = pageRequestDto.getPageable("id");
////
////        Page<Review> result = reviewRepository.findReviewByStore(store,/* member,*/ pageable);
////
////        List<ReviewDto> dtoList = result.getContent().stream()
////                .map(review -> modelMapper.map(review,ReviewDto.class)).collect(Collectors.toList());
//
//
////        return PageResponseDto.<ReviewDto>withAll()
////                .pageRequestDto(pageRequestDto)
////                .dtoList(dtoList)
////                .total((int)result.getTotalElements())
////                .build();
//
//        return reviewRepository.listByStore(store);
//    }

    public Long register(ReviewDto reviewDto) {

        Review review = modelMapper.map(reviewDto, Review.class);

        log.info("===========================ls");
        Long id = reviewRepository.save(review).getId();

        return id;
    }

//    public Long register(ReviewDto reviewDto /*,MemberDto memberDto/*, Long id, MultipartFile[] files*/) {
//
//
////        Optional<Member> member = memberRepository.findById(id);
////        log.info("이게 뭐냐구우"+String.valueOf(member));
////
////        Review review = Review.builder()
////                .rate(reviewDto.getRate())
////                .content(reviewDto.getContent())
////                .member_nickname(reviewDto.getMember())
////
////                .build();
////
////
////        review.getStore();
//
//        Review review = modelMapper.map(reviewDto, Review.class);
//
//
//
//        Long id =reviewRepository.save(review).getId();
//
//        log.info("===========================ls");
//
//        return id;
//    }

//    @Value("${aws.s3.bucket}")
//    private String bucketName;

//    public List<ReviewDto> findByStore(StoreDto store) {
//
//        return reviewRepository.findByStore(store);
//    }

//    public ReviewDto get(Long member_id) {
//
//        Optional<Review> result = reviewRepository.findByMember(member_id);
//
//        Review review = result.orElseThrow();
//
//        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
//
//        return reviewDto;
//    }

//    public ReviewDto findById(long id) {
//
//        Optional<Review> result = reviewRepository.findById(id);
//
//        Review review = result.orElseThrow();
//
//        ReviewDto reviewDTO = modelMapper.map(review, ReviewDto.class);
//
//        return reviewDTO;
//    }
//
//    public int register(ReviewDto review, MultipartFile[] files) {
//        int cnt = reviewRepository.save(review);
//
//        for (MultipartFile file : files) {
//
//            if (file != null && file.getSize() > 0) {
//                reviewRepository.saveFile(review.getId(), file.getOriginalFilename());
//
//                uploadFile(review.getId(), file);
//            }
//        }
//
//        return cnt;
//    }

//    private void uploadFile(int id, MultipartFile file) {
//        try {
//            // S3에 파일 저장
//            // 키 생성
//            String key = "chewing/review/" + id + "/" + file.getOriginalFilename();
//
//            // putObjectRequest
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(key)
//                    .acl(ObjectCannedACL.PUBLIC_READ)
//                    .build();
//
//            // requestBody
//            RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
//
//            // object(파일) 올리기
//            s3Client.putObject(putObjectRequest, requestBody);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

//    public int update(Review review, MultipartFile[] files, List<String> removeFiles) {
//
//        return reviewRepository.update(review);
//    }
//
//    public int deleteById(long id) {
//
//        return reviewRepository.deleteById(id);
//    }



    public void modify(ReviewDto reviewDto) {

        Optional<Review> result = reviewRepository.findById(reviewDto.getId());

        Review review = result.orElseThrow();

        review.change(reviewDto.getContent());

        reviewRepository.save(review);

    }

    public void remove(Long id) {

        reviewRepository.deleteById(id);

    }

    public PageResponseDto<ReviewDto> myList(Long member, PageRequestDto pageRequestDto) {

        Pageable pageable = pageRequestDto.getPageable("id");

        Page<Review> result = reviewRepository.findReviewByMember(member, pageable);

        List<ReviewDto> dtoList = result.getContent().stream()
                .map(review -> modelMapper.map(review,ReviewDto.class)).collect(Collectors.toList());


        return PageResponseDto.<ReviewDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

//    public Optional<Review> reviewList(Long store) {
//        return reviewRepository.findByStore(store);
//    }
//


    public List<ReviewDto> reviewList(Long store) {
        return reviewRepository.reviewList(store);
    }

    public ReviewDto get(Long id) {
        Optional<Review> result = reviewRepository.findById(id);

        Review review = result.orElseThrow();

        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        return reviewDto;
    }

    public PageResponseDto<ReviewDto> getList(Long store, PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() <=0? 0: pageRequestDto.getPage() -1,
                pageRequestDto.getSize(),
                Sort.by("id").ascending());

        Page<Review> result = reviewRepository.listOfStore(store, pageable);

        List<ReviewDto> dtoList =
                result.getContent().stream().map(reply -> modelMapper.map(reply, ReviewDto.class))
                        .collect(Collectors.toList());

        return PageResponseDto.<ReviewDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }


}
