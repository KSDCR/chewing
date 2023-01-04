package io.web.chewing.domain.booking;


import io.web.chewing.Entity.BaseEntity;
import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname", referencedColumnName = "nickname")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_name", referencedColumnName = "name")
    private Store store;

    private Long store_id;

    private String real_name;

    @NotNull
    private Long people;

    @NotNull
    private String date;

    @NotNull
    private String time;

    @Column(name = "booking_state")
    @Enumerated(EnumType.STRING)
    private BookingState.bookingState bookingState;

    public void changeReal_name(String real_name){
        this.real_name = real_name;
    }

    public void changeState(BookingState.bookingState bookingState){
        this.bookingState = bookingState;
    }
}
