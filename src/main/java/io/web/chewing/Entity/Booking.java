package io.web.chewing.Entity;


import com.sun.istack.NotNull;
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
    @JoinColumn(name = "member_nickname")
    private Member member;

    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @NonNull
    private Long people;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private LocalDateTime time;

}
