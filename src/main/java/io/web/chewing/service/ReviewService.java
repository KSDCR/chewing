package io.web.chewing.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Review;
import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.PageInfo;
import io.web.chewing.domain.ReviewDto;
import io.web.chewing.mapper.review.ReviewMapper;
import io.web.chewing.model.PrincipalUser;
import io.web.chewing.repository.ReviewRepository;
import io.web.chewing.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

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

    private static Member getBuild(PrincipalUser principalUser) {
        return Member.builder()
                .id(0L)
                .nickname(principalUser.providerUser().getNickName())
                .password(principalUser.providerUser().getPassword())
                .delete_yn('0')
                .email(principalUser.providerUser().getEmail())
                .provider(principalUser.providerUser().getProvider())
                .build();
    }



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


    public String modify(ReviewDto reviewDto, MultipartFile[] addFiles, List<String> removeFiles) {
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


        return reviewRepository.save(review).getMember_id().getNickname();

    }


    @Transactional
    public String remove(Long id) {
        ReviewDto reviewDto = reviewMapper.select(id);
        List<String> fileNames = reviewDto.getFileName();

        log.info("---------------------"+String.valueOf(id));

        if (fileNames != null) {
            for (String fileName : fileNames) {
                deleteFile(id, fileName);

                reviewMapper.deleteFileByReviewIdAndFileName(id, fileName);
            }


        }


        reviewRepository.deleteById(id);
        return reviewDto.getMember_nickname();
    }

    private void deleteFile(Long id, String fileName) {
        String key = "chewing/review/" + id + "/" + fileName;

        s3Client.deleteObject(bucketName, key);
        log.info("==========="+ key);

    }


    public ReviewDto get(Long id) {
        ReviewDto reviewDto= reviewMapper.findReviewById(id);


        return reviewDto;
    }
    public List<ReviewDto> listReviewByStore(String store_name, int page, PageInfo pageInfo) {

        log.info("bbbbbbbbbbbPage"+page);

        int records = 10;
        int offset = (page - 1) * records;


        int countAll = reviewMapper.countReviewByStore(store_name);
        int lastPage = (countAll - 1) / records + 1;

        log.info("==========="+countAll);
        log.info("===========lastPage"+lastPage);

        int leftPageNumber = (page - 1) / 10 * 10 + 1;

        log.info("ccccccccccccccLeftPageNum"+leftPageNumber);

        int rightPageNumber = leftPageNumber + 9;

        log.info("==========="+rightPageNumber);


        int currentPageNumber = page;

        log.info("ccccccccccccccCurrentPageNum"+currentPageNumber);

        rightPageNumber = Math.min(rightPageNumber, lastPage);

        log.info("ccccccccccccccRightPageNum"+rightPageNumber);
        boolean hasNextPageNumber = page <= ((lastPage-1)/10*10);

        log.info("cccccccccccccchasNextPageNum"+hasNextPageNumber);

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

        log.info("==========="+rightPageNumber);
        boolean hasNextPageNumber = page <= ((lastPage-1)/10*10);

        pageInfo.setHasNextPageNumber(hasNextPageNumber);
        pageInfo.setCurrentPageNumber(currentPageNumber);
        pageInfo.setLeftPageNumber(leftPageNumber);
        pageInfo.setRightPageNumber(rightPageNumber);
        pageInfo.setLastPageNumber(lastPage);

        return reviewMapper.findReviewByMember(member_nickname, offset, records);
    }

        public File multipartToFile(MultipartFile mfile) throws IllegalStateException, IOException{
            File file = new File(mfile.getOriginalFilename());
            mfile.transferTo(file);
            return file;
        }


//    public void removeFileByName(Long id, String fileName) {
//
//        String key = "chewing/review/" + id + "/" + fileName;
//
//        s3Client.deleteObject(bucketName, key);
//
//        reviewMapper.deleteFileByReviewIdAndFileName(id, fileName);
//    }
//

}
