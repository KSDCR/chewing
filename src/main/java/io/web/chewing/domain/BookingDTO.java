package io.web.chewing.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long id;

    private String store_name;

    private String member_nickname;

    private String real_name;

    private Long people;

    private String date;

    private String time;


}
