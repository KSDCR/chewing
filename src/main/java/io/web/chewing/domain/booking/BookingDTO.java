package io.web.chewing.domain.booking;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
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


    private BookingState state;


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
