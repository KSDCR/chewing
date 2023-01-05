package io.web.chewing.domain.booking;

import com.querydsl.core.annotations.QueryProjection;
import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class BookingDTO {
    private Long id;

    private String store_name;

    private Long store_id;

    private String member_nickname;

    private String real_name;

    private Long people;

    private String date;

    private String time;


    private BookingState.bookingState state;

    public BookingDTO(Long id, String store_name, Long store_id, String member_nickname, String real_name, Long people, String date, String time, BookingState.bookingState state) {
        this.id = id;
        this.store_name = store_name;
        this.store_id = store_id;
        this.member_nickname = member_nickname;
        this.real_name = real_name;
        this.people = people;
        this.date = date;
        this.time = time;
        this.state = state;
    }

    public Booking toEntity(Store store, Member member) {
        return Booking.builder()
                .real_name(real_name)
                .people(people)
                .date(date)
                .time(time)
                .bookingState(BookingState.bookingState.needConfirm)
                .store(store)
                .store_id(store.getId())
                .member(member)
                .build();
    }
}
