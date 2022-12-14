package io.web.chewing.repository;

import io.web.chewing.Entity.Booking;
import io.web.chewing.Entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndMember_Nickname(Long id, String nickname);
    /*List<BookingDTO> findBookingListPaging(String store, String storeName);*/

    Page<Booking> findAllByMember_Nickname(Pageable pageable, String nickname);
}
