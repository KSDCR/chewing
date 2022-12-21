package io.web.chewing.repository;

import io.web.chewing.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> , JpaSpecificationExecutor {

    Optional<Booking> findByIdAndMember_Nickname(Long id, String nickname);
    /*List<BookingDTO> findBookingListPaging(String store, String storeName);*/
}
