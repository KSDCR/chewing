package io.web.chewing.booking;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.booking.Booking;
import io.web.chewing.domain.booking.BookingState;
import io.web.chewing.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@RequiredArgsConstructor
@Slf4j
public class BookingPaginationRepositoryTest {

    @Autowired
    private BookingCustomRepositoryImpl bookingCustomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    private final String prefixReal_name = "test";

    @Before
    public void setUp() {
        Member member = memberRepository.save(new Member(prefixReal_name + "email", prefixReal_name + "password",
                prefixReal_name + "nick", prefixReal_name + "provider"));

        Store store = storeRepository.save(Store.builder()
                .name(prefixReal_name + "store")
                .detail(prefixReal_name + "detail")
                .open_time(prefixReal_name + "open_time")
                .close_time(prefixReal_name + "close_time")
                .address(prefixReal_name + "address")
                .build());

        for (int i = 1; i <= 30; i++) {
            bookingRepository.save(Booking.builder()
                    .real_name(prefixReal_name + i)
                    .member(member)
                    .store(store)
                    /*.store(new Store((long) i, prefixReal_name + "store" + i))*/
                    .bookingState(BookingState.bookingState.needConfirm)
                    .date("testDate")
                    .time("testTime")
                    .people((long) i)
                    .build());
        }
    }

    // TODO: 2023-01-03 테스트 코드 작성 이슈 
    /**
     *   해당 테스트 클래스 작성하면서 junit5와 junit4의 의존성을 섞어서 import 한 문제 발생함 
     *   이 부분 역시 4와 5의 버전별 차이점과 장단점 사용법 정리 포스팅 
     *   
     *   또한 legacy 테스트 메서드를 작성하면서 내가 원하는 결과 값은 10개 인대 0개만 나오는 이슈가 발생함
     *  알고보니 내가 paginationLegacy() 파라미터에 2번째 매개변수인 pageNo를 10000을 주고 있어서 발생함
     *  나는 30개의 쿼리 데이터만을 입력하니까 총 페이지 수가 3인대 10000번째 페이지를 보고 있으니 당연히 검색 값이 0임
     *  velog에 포스팅하고 지울것
     * */
    @Test
    public void legacy() throws Exception {
        log.info("30개인가요?"+String.valueOf(bookingRepository.count()));

        //given
        String searchName = "test";


        //when
        List<BookingPaginationDto> bookings = bookingCustomRepository.paginationLegacy(searchName, 1, 10);
        System.out.println("몇개지?"+bookings.size());

        //then
        assertThat(bookings).hasSize(10);
    }

    @After
    public void after() {
        bookingRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }


}
