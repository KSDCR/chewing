package io.web.chewing.service;

import io.web.chewing.Entity.Booking;
import io.web.chewing.domain.BookingDTO;
import io.web.chewing.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Log4j
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final ModelMapper modelMapper;

    private final BookingRepository bookingRepository;

    @Override
    public Long register(BookingDTO bookingDTO) {

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


    @Override
    public BookingDTO readOne(Long id) {

        Optional<Booking> result = bookingRepository.findById(id);

        Booking booking = result.orElseThrow();

        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);

        return bookingDTO;
    }

    @Override
    public void modify(BookingDTO bookingDTO) {
        Optional<Booking> result = bookingRepository.findById(bookingDTO.getId());

        Booking booking = result.orElseThrow();

        booking.change(booking.getName());

        bookingRepository.save(booking);
    }

    @Override
    public void remove(Long id) {
        //bookingRepository.deleteAllById(id);
    }

}
