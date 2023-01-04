package io.web.chewing.repository;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.booking.BookingState;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookingPaginationDto implements Comparable<BookingPaginationDto>{
        private Long id;
        private String real_name;
        private Long people;
        private Member member;
        private Store store;
        private String date;
        private String time;
        private String member_nickname;
        private String store_name;
        private BookingState.bookingState bookingState;

        public BookingPaginationDto(Long id) {
            this.id = id;
        }

        @Override
        public int compareTo(BookingPaginationDto bookingPaginationDto) {
            return bookingPaginationDto.id.compareTo(this.id);
        }


}
