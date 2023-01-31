package io.web.chewing.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static io.web.chewing.Entity.QMember.member;
import static io.web.chewing.Entity.QStore.store;
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
                        booking.member.nickname.as("member_nickname"),
                        booking.store.name.as("store_name"),
                        booking.date,
                        booking.time,
                        booking.bookingState))
                .from(booking)
                .where(
                        booking.member.nickname.eq(searchName)
                )
                .orderBy(booking.id.desc())
                .limit(PageSize)
                .offset((long) pageNo * PageSize)
                .fetch();
    }

    /**
     * 커버링 인덱스 by Querydsl
     * Dto Class에 String 필드로 member_nickname & store_name 저장
     * 해당 query는 join을 직접적으로 선언하지 않아도
     * 명시적으로 join이 발생해서 문제가 없음
     * 이게 CoveringIndex 활용하면 이런지 확실하지 않아서
     * innerJoin명시해줌
     */
    public List<BookingPaginationDto> paginationCoveringIndexByEntityToDto(String name, int pageNo, int pageSize) {
        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory.select(booking.id)
                .from(booking)
                .where(booking.member.nickname.eq(name))
                .orderBy(booking.id.desc())
                .limit(pageSize)
                .offset((long) pageNo * pageSize)
                .fetch();
        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        // 2)
        return queryFactory.select(Projections.fields(BookingPaginationDto.class,
                        booking.id.as("id"),
                        booking.real_name,
                        booking.people,
                        booking.date,
                        booking.time,
                        booking.member.nickname.as("member_nickname"),
                        booking.store.name.as("store_name"),
                        booking.bookingState))
                .from(booking)
                .innerJoin(booking.store, store)
                .innerJoin(booking.member, member)
                .where(booking.id.in(ids))
                .orderBy(booking.id.desc())
                .fetch();
    }

    public List<BookingPaginationDto> paginationCoveringIndexByEntityToDtoLeftJoin(String name, int pageNo, int pageSize) {
        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory.select(booking.id)
                .from(booking)
                .where(booking.member.nickname.eq(name))
                .orderBy(booking.id.desc())
                .limit(pageSize)
                .offset((long) pageNo * pageSize)
                .fetch();
        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        // 2)
        return queryFactory.select(Projections.fields(BookingPaginationDto.class,
                        booking.id.as("id"),
                        booking.real_name,
                        booking.people,
                        booking.date,
                        booking.time,
                        booking.member.nickname.as("member_nickname"),
                        booking.store.name.as("store_name"),
                        booking.bookingState))
                .from(booking)
                .leftJoin(booking.store, store)
                .leftJoin(booking.member, member)
                .where(booking.id.in(ids))
                .orderBy(booking.id.desc())
                .fetch();
    }

    /**
     * 커버링 인덱스 by Querydsl
     * Dto class에 Entity 자체를 받아서 저장후 member_nickname과 store_name을 가져오는 구조
     * 현재 페이지 상으론 효율성이 낮아보임
     */
    public List<BookingPaginationDto> paginationCoveringIndex(String name, int pageNo, int pageSize) {
        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory.select(booking.id)
                .from(booking)
                .where(booking.member.nickname.eq(name))
                .orderBy(booking.id.desc())
                .limit(pageSize)
                .offset((long) pageNo * pageSize)
                .fetch();
        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        // 2)
        return queryFactory.select(Projections.fields(BookingPaginationDto.class,
                        booking.id.as("id"),
                        booking.real_name,
                        booking.people,
                        booking.member,
                        booking.store,
                        booking.date,
                        booking.time,
                        booking.bookingState))
                .from(booking)
                .where(booking.id.in(ids))
                .orderBy(booking.id.desc())
                .fetch();
    }

    /**
     * 페이징 by NoOffset
     * 첫 페이지 이후 조회 할때 필요한 동적 쿼리를 위해서
     * BooleanBuilder사용
     * 하지만 쿼리가 보기 불편함
     */
    public List<BookingPaginationDto> paginationNoOffsetBuilder(Long bookingId, String name, int pageSize) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (bookingId != null) {
            booleanBuilder.and(booking.id.lt(bookingId));
        }

        return queryFactory.select(Projections.fields(BookingPaginationDto.class,
                        booking.id,
                        booking.real_name,
                        booking.people,
                        booking.date,
                        booking.time,
                        booking.member.nickname,
                        booking.store.name,
                        booking.bookingState))
                .from(booking)
                .where(booleanBuilder.and(booking.member.nickname.eq(name)))
                .orderBy(booking.id.desc())
                .limit(pageSize)
                .fetch();
    }

    /**
     * 페이징 by NoOffset
     * 첫 페이지 이후 조회 할때 필요한 동적 쿼리를 위해서
     * BooleanBuilder사용
     * 이전 메서드의 불편한 점을 개선해서
     * ltBookingId() 외부 메서드화 해서 사용함
     */
    public List<BookingPaginationDto> paginationNoOffset(Long bookingId, String name, int pageSize) {

        return queryFactory
                .select(Projections.fields(BookingPaginationDto.class,
                        booking.id,
                        booking.real_name,
                        booking.people,
                        booking.date,
                        booking.time,
                        booking.member.nickname,
                        booking.store.name,
                        booking.bookingState))
                .from(booking)
                .where(
                        ltBookingId(bookingId),
                        booking.member.nickname.eq(name)
                )
                .orderBy(booking.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression ltBookingId(Long bookingId) {
        if (bookingId == null) {
            return null;
        }

        return booking.id.lt(bookingId);
    }

}
