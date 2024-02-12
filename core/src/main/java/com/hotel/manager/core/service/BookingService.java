package com.hotel.manager.core.service;

import com.hotel.manager.core.domain.Booking;
import java.util.List;

public interface BookingService {

    Booking save(Booking booking);

    Booking update(Booking booking);

    Booking getActiveBookingByRoomId(Long roomId);

    List<Booking> getCompletedBookingsByRoomId(Long roomId);
}
