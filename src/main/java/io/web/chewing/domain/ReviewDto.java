package io.web.chewing.domain;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Review;
import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {


    private Long id;
    private String store_name;
    private double rate;
    private String member_nickname;
    private String content;
    private String creat_time;
    private String modify_time;

    private List<String> fileName;


    public Review toEntity(Store store){

        return Review.builder()
                .store_name(store)
                .rate(rate)
                .content(content).build();
    }

}
