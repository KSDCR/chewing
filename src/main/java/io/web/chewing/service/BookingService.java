package io.web.chewing.service;

import io.web.chewing.Entity.Booking;
import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.BookingDTO;
import io.web.chewing.domain.PageInfo;
import io.web.chewing.mapper.booking.BookingMapper;
import io.web.chewing.repository.BookingRepository;
import io.web.chewing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final ModelMapper modelMapper;

    private final BookingRepository bookingRepository;

    private final StoreRepository storeRepository;

    private final BookingMapper bookingMapper;

    public Long register(BookingDTO bookingDTO, AuthMemberDTO authMemberDTO, String store_name) {

        Optional<Store> optionalStore = storeRepository.findByName(store_name);
        Store store = optionalStore.orElseThrow(() -> new RuntimeException());

        Booking booking = modelMapper.map(bookingDTO, Booking.class);

        // Booking Entity에서 bno가 아닌 id로 선언 책과 달리 getId로 변경
        Long bno = bookingRepository.save(booking).getId();

        return bno;
    }

    /**
     * 예약시 보이는 간단한 정보 (매장사진, 매장명) 보여주기
     */
/*    @Override
    public List<StoreDTO> getAllStoreInfo(String store, String store_name) {
        return storeRepository.findBookingListPaging(store, store_name);
    }*/
    public BookingDTO readOne(Long id) {

        Optional<Booking> result = bookingRepository.findById(id);

        Booking booking = result.orElseThrow();

        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);

        return bookingDTO;
    }


    public void remove(Long id) {
        //bookingRepository.deleteAllById(id);
    }

    public List<BookingDTO> listBookingByMember(String member_nickname, int page, PageInfo pageInfo) {
        int records = 10;
        int offset = (page - 1) * records;

        int countAll = bookingMapper.countReviewByMember(member_nickname);
        int lastPage = (countAll - 1) / records + 1;

        log.info("===========" + countAll);

        int leftPageNumber = (page - 1) / 10 * 10 + 1;
        int rightPageNumber = leftPageNumber + 9;
        int currentPageNumber = page;
        rightPageNumber = Math.min(rightPageNumber, lastPage);
        boolean hasNextPageNumber = page <= ((lastPage - 1) / 10 * 10);

        pageInfo.setHasNextPageNumber(hasNextPageNumber);
        pageInfo.setCurrentPageNumber(currentPageNumber);
        pageInfo.setLeftPageNumber(leftPageNumber);
        pageInfo.setRightPageNumber(rightPageNumber);
        pageInfo.setLastPageNumber(lastPage);


        return bookingMapper.findBookingByMember(member_nickname, offset, records);
    }
}
