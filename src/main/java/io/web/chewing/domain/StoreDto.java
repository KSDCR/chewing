package io.web.chewing.domain;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class StoreDto {
    //private Long id;
    private String name;
    private String address;
    private String phone;
    private String detail;
    private String open_time;
    private String close_time;
   // private LocalDateTime inserted;

}
