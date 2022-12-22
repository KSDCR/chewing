package io.web.chewing.service;

import io.web.chewing.Entity.Booking;
import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.BookingDTO;
import io.web.chewing.domain.PageDto;
import io.web.chewing.model.PrincipalUser;
import io.web.chewing.repository.BookingRepository;
import io.web.chewing.repository.MemberRepository;
import io.web.chewing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final ModelMapper modelMapper;

    private final BookingRepository bookingRepository;

    private final StoreRepository storeRepository;

    private final MemberRepository memberRepository;


    public Long register(BookingDTO bookingDTO, PrincipalUser principalUser, String store_name) {

        Store store = storeRepository.findByName(store_name).orElseThrow(RuntimeException::new);

        Member member = memberRepository.findByNickname(principalUser.providerUser().getNickName()).orElseThrow();

        Booking booking = bookingDTO.toEntity(store, member);

        return bookingRepository.save(booking).getId();
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


    public void remove(Long id, PrincipalUser principalUser) {
        Optional<Booking> optional = bookingRepository.findByIdAndMember_Nickname(id, principalUser.providerUser().getNickName());
        Booking booking = optional.orElseThrow();

        if (Objects.equals(booking.getMember().getNickname(), principalUser.providerUser().getNickName())) {
            bookingRepository.deleteById(id);
        }
    }

    public Page<BookingDTO> listPage(int page, int size, String member_nickname) {
        PageRequest pageRequest = PageRequest.of(page, size);


        Page<Booking> bookings = bookingRepository.findAllByMember_Nickname(pageRequest, member_nickname);

        return bookings.map(booking -> BookingDTO.builder()
                .id(booking.getId())
                .store_name(booking.getStore().getName())
                .store_id(booking.getStore_id())
                .member_nickname(booking.getMember().getNickname())
                .real_name(booking.getReal_name())
                .people(booking.getPeople())
                .date(booking.getDate())
                .time(booking.getTime())
                .build());
    }

    public PageDto page(Page<BookingDTO> bookings, int page, String keyword) {

        return PageDto.builder()
                .total(bookings.getTotalPages())
                .number(bookings.getNumber())
                .keyword(keyword)
                .build();
    }
}
