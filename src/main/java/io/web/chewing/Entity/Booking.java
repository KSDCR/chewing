package io.web.chewing.Entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private Long people;

    private LocalDateTime date;

    private LocalDateTime time;

}
