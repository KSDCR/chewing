package io.web.chewing.domain;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Review;
import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {


    private Long id;
    private Long store;
    private double rate;
    private Member member_nickname;
    private String content;
    private String create_time;
    private String modify_time;

    public Review toEntity(){
        return Review.builder()
                .rate(rate)
                .content(content).build();
    }

}
