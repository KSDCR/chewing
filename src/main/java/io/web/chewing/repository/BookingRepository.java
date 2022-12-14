package io.web.chewing.repository;

import io.web.chewing.Entity.Booking;
import io.web.chewing.domain.BookingDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<BookingDTO> findBookingListPaging(String store, String storeName);
}
