package io.web.chewing.Entity;


import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
public class Booking extends BaseEntity {
/*
* NonNull : 메소드 인자의 유효성 검사 / Null이 있을 때, NullPointerException 발생
* NotNull : 변수에 Null 유무 검사
*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_name", referencedColumnName = "name")
    private Store store;

    private String real_name;

    @NonNull
    private Long people;

    @NotNull
    private String date;

    @NotNull
    private String time;

    @ColumnDefault("false")
    private boolean confirm;

}
