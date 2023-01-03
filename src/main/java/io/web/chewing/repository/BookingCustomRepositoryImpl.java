package io.web.chewing.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static io.web.chewing.domain.booking.QBooking.booking;


@RequiredArgsConstructor
@Repository
@Slf4j
public class BookingCustomRepositoryImpl {
    private final JPAQueryFactory queryFactory;


    /**
     * Legacy 페이징 by Querydsl
     */
    public List<BookingPaginationDto> paginationLegacy(String searchName, int pageNo, int PageSize) {
        return queryFactory.select(Projections.fields(BookingPaginationDto.class,
                        booking.id.as("id"),
                        booking.real_name,
                        booking.people,
                        booking.store,
                        booking.date,
                        booking.time,
                        booking.bookingState))
                .from(booking)
                .where(
                        booking.real_name.like(searchName + "%")
                )
                .orderBy(booking.id.desc())
                .limit(PageSize)
                .offset((long) pageNo * PageSize)
                .fetch();
    }

    /**
     * 커버링 인덱스 by Querydsl
     */
    public List<BookingPaginationDto> paginationCoveringIndex(String name, int pageNo, int pageSize) {
        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory.select(booking.id)
                .from(booking)
                .where(booking.real_name.like(name + "%"))
                .orderBy(booking.id.desc())
                .offset(pageSize)
                .offset((long) pageNo * pageSize)
                .fetch();
        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            log.info("검색 안됨");
            return new ArrayList<>();
        }

        log.info("검색됨");
        // 2)
        return queryFactory.select(Projections.fields(BookingPaginationDto.class,
                        booking.id.as("id"),
                        booking.real_name,
                        booking.people,
                        booking.store,
                        booking.date,
                        booking.time,
                        booking.bookingState))
                .from(booking)
                .where(booking.id.in(ids))
                .orderBy(booking.id.desc())
                .fetch();
    }

}
