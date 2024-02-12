package com.hotel.manager.core.service.impl;

import com.hotel.manager.core.domain.Booking;
import com.hotel.manager.core.exception.ActiveBookingNotFoundException;
import com.hotel.manager.core.repository.BookingRepository;
import com.hotel.manager.core.service.BookingService;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingRepository.update(booking);
    }

    @Override
    public Booking getActiveBookingByRoomId(Long roomId) {
        return bookingRepository.findActiveByRoomId(roomId)
                .orElseThrow(() -> new ActiveBookingNotFoundException(String.format("Room %s is not booked.", roomId)));
    }

    @Override
    public List<Booking> getCompletedBookingsByRoomId(Long roomId) {
        return bookingRepository.findCompletedByRoomId(roomId);
    }
}
