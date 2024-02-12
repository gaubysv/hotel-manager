package com.hotel.manager.core.repository;

import com.hotel.manager.core.domain.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    Booking save(Booking booking);

    Booking update(Booking booking);

    Optional<Booking> findActiveByRoomId(Long roomId);

    List<Booking> findCompletedByRoomId(Long roomId);
}
