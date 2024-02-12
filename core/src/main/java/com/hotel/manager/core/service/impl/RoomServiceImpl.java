package com.hotel.manager.core.service.impl;

import com.hotel.manager.core.domain.Booking;
import com.hotel.manager.core.domain.Guest;
import com.hotel.manager.core.domain.Room;
import com.hotel.manager.core.exception.NoAvailableRoomsException;
import com.hotel.manager.core.exception.RoomNotFoundException;
import com.hotel.manager.core.repository.RoomRepository;
import com.hotel.manager.core.service.BookingService;
import com.hotel.manager.core.service.GuestService;
import com.hotel.manager.core.service.RoomService;
import com.hotel.manager.core.service.dto.RoomLookupParams;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

// TODO - add tests.
// TODO - add transaction support.
public class RoomServiceImpl implements RoomService {

    private final GuestService guestService;
    private final BookingService bookingService;
    private final RoomRepository roomRepository;

    public RoomServiceImpl(GuestService guestService, BookingService bookingService, RoomRepository roomRepository) {
        this.guestService = guestService;
        this.bookingService = bookingService;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> lookupRooms(RoomLookupParams params) {
        return roomRepository.lookup(params);
    }

    @Override
    public Room checkin(Guest guest) {
        Room room = roomRepository.findFirstAvailable()
                .orElseThrow(() -> new NoAvailableRoomsException("There are no available rooms."));

        room.setOccupied();

        guest = guestService.save(guest);
        room = roomRepository.update(room);

        Booking booking = new Booking();
        booking.setRoomId(room.getId());
        booking.setGuestId(guest.getId());
        booking.setCheckinTime(LocalDateTime.now(ZoneOffset.UTC));
        booking.setActive();

        bookingService.save(booking);

        return room;
    }

    @Override
    public void checkout(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(String.format("Room %s not found.", roomId)));

        Booking booking = bookingService.getActiveBookingByRoomId(roomId);

        room.setAvailable();
        booking.setCompleted();
        booking.setCheckoutTime(LocalDateTime.now(ZoneOffset.UTC));

        roomRepository.update(room);
        bookingService.update(booking);
    }
}
