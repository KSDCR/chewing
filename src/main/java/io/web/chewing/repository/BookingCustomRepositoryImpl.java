package io.web.chewing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.web.chewing.Entity.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.web.chewing.Entity.QBooking.booking;

@RequiredArgsConstructor
@Repository
public class BookingCustomRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public List<Booking> findByName(String name) {
        return queryFactory.selectFrom(booking)
                .where(booking.real_name.eq(name))
                .fetch();
    }
}
