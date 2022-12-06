package io.web.chewing.domain;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
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
    private Long member_id;
    private String content;
    private String create_time;



}
