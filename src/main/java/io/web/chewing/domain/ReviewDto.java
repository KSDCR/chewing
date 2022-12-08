package io.web.chewing.domain;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Review;
import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {


    private Long id;
    private Long store_id;
    private String Store;
    private double rate;
    private String member_nickname;
    private String content;
    private String create_time;
    private String modify_time;

    public Review toEntity(Store store){

        return Review.builder()
                .store(store)
                .rate(rate)
                .content(content).build();
    }

}
