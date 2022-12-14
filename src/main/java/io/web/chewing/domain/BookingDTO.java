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

    private String name;

    private Long people;

    private LocalDateTime date;

    private LocalDateTime time;


    private LocalDateTime created_at;

    private LocalDateTime modified_at;

}
