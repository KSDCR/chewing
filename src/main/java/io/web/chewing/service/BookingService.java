package io.web.chewing.service;

import io.web.chewing.domain.BookingDTO;

public interface BookingService {

    Long register(BookingDTO bookingDTO);

    BookingDTO readOne(Long id);

    void modify(BookingDTO bookingDTO);

    void remove(Long Id);
}
