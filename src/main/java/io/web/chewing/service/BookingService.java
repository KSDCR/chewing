package io.web.chewing.service;

import io.web.chewing.Entity.Booking;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.BookingDTO;
import io.web.chewing.mapper.booking.BookingMapper;
import io.web.chewing.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public Long register(BookingDTO bookingDTO, AuthMemberDTO authMemberDTO, String store_name) {

        Optional<Store> optionalStore = storeRepository.findByName(store_name);
        Store store = optionalStore.orElseThrow(RuntimeException::new);
        Optional<Member> optionalMember = memberRepository.findByNickname(authMemberDTO.getNickname());
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

       if(Objects.equals(booking.getMember().getNickname(), authMemberDTO.getNickname())){
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
}
