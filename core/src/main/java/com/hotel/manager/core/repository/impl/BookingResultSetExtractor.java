package com.hotel.manager.core.repository.impl;

import com.hotel.manager.core.domain.Booking;
import com.hotel.manager.core.domain.BookingStatus;
import com.hotel.manager.core.exception.CoreException;
import com.hotel.manager.core.repository.ResultSetExtractor;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.hotel.manager.core.utils.DateUtils.parse;

public class BookingResultSetExtractor implements ResultSetExtractor<Booking> {

    @Override
    public List<Booking> extract(ResultSet resultSet) {
        try {
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()) {
                Long id = resultSet.getLong("booking_id");
                Long roomId = resultSet.getLong("booking_room_id");
                Long guestId = resultSet.getLong("booking_guest_id");
                String status = resultSet.getString("booking_status");
                String checkInTime = resultSet.getString("booking_check_in_time");
                String checkOutTime = resultSet.getString("booking_check_out_time");

                Booking booking = new Booking();
                booking.setId(id);
                booking.setRoomId(roomId);
                booking.setGuestId(guestId);
                booking.setStatus(BookingStatus.valueOf(status));
                booking.setCheckinTime(parse(checkInTime));
                booking.setCheckoutTime(parse(checkOutTime));
                bookings.add(booking);
            }
            return bookings;
        } catch (Exception exception) {
            throw new CoreException(exception.getMessage());
        }
    }
}
