//package io.web.chewing.domain;
//
//import io.web.chewing.Entity.Review;
//import io.web.chewing.Entity.Store;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class ReviewListAllDto {
//
//
//    private Long id;
//    private String store_name;
//    private double rate;
//    private String member_nickname;
//    private String content;
//    private String create_time;
//    private String modify_time;
//
//    private List<ReviewImageDto> reviewImages;
//
//    public Review toEntity(String store){
//
//        return Review.builder()
//                .store(Store.builder()
//                        .id(1L)
//                        .name(store).build())
//                .rate(rate)
//                .content(content).build();
//    }
//
//}
