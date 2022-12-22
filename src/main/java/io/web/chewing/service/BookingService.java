package io.web.chewing.service;

import io.web.chewing.Entity.Booking;
import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.BookingDTO;
import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.PageInfo;
import io.web.chewing.mapper.booking.BookingMapper;
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
import java.util.List;
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

    private final BookingMapper bookingMapper;

    public Long register(BookingDTO bookingDTO, PrincipalUser principalUser, String store_name) {

        Optional<Store> optionalStore = storeRepository.findByName(store_name);
        Store store = optionalStore.orElseThrow(RuntimeException::new);
        log.info("서비스단에 스토어 이름 있나?"+store.getName());
        Optional<Member> optionalMember = memberRepository.findByNickname(principalUser.providerUser().getNickName());
        Member member = optionalMember.orElseThrow();
        log.info("잘 가져왔나?" + member.getNickname());

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


    public void remove(Long id, AuthMemberDTO authMemberDTO) {
        Optional<Booking> optional = bookingRepository.findByIdAndMember_Nickname(id, authMemberDTO.getNickname());
        Booking booking = optional.orElseThrow();

        if (Objects.equals(booking.getMember().getNickname(), authMemberDTO.getNickname())) {
            bookingRepository.deleteById(id);
        }
    }

    public List<BookingDTO> listBookingByMember(String member_nickname, int page, PageInfo pageInfo) {
        int records = 10;
        int offset = (page - 1) * records;


        int countAll = bookingMapper.countBookingByMember(member_nickname);
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

        return bookingMapper.findBookingByMember(member_nickname);
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
