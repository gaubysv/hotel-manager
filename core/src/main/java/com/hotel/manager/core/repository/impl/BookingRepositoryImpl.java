package com.hotel.manager.core.repository.impl;

import com.hotel.manager.core.domain.Booking;
import com.hotel.manager.core.repository.BookingRepository;
import com.hotel.manager.core.repository.NamedQueries;
import com.hotel.manager.core.repository.QueryExecutor;
import java.util.List;
import java.util.Optional;

import static com.hotel.manager.core.utils.DateUtils.format;

public class BookingRepositoryImpl implements BookingRepository {

    private final NamedQueries namedQueries;
    private final QueryExecutor queryExecutor;
    private final BookingResultSetExtractor bookingResultSetExtractor;

    public BookingRepositoryImpl(NamedQueries namedQueries, QueryExecutor queryExecutor, BookingResultSetExtractor bookingResultSetExtractor) {
        this.namedQueries = namedQueries;
        this.queryExecutor = queryExecutor;
        this.bookingResultSetExtractor = bookingResultSetExtractor;
    }

    @Override
    public Booking save(Booking booking) {
        String query = namedQueries.getNamedQuery("bookings.save");

        Object[] queryParams = {
                booking.getRoomId(),
                booking.getGuestId(),
                booking.getStatus().name(),
                format(booking.getCheckinTime()),
                format(booking.getCheckoutTime())
        };

        return queryExecutor.executeWithResults(bookingResultSetExtractor, query, queryParams).stream().findFirst().orElse(null);
    }

    @Override
    public Booking update(Booking booking) {
        String query = namedQueries.getNamedQuery("bookings.update");

        Object[] queryParams = {
                booking.getRoomId(),
                booking.getGuestId(),
                booking.getStatus().name(),
                format(booking.getCheckinTime()),
                format(booking.getCheckoutTime()),
                booking.getId()
        };

        return queryExecutor.executeWithResults(bookingResultSetExtractor, query, queryParams).stream().findFirst().orElse(null);
    }

    @Override
    public Optional<Booking> findActiveByRoomId(Long roomId) {
        String query = namedQueries.getNamedQuery("bookings.get-active-by-room-id");

        Object[] queryParams = {roomId};

        return queryExecutor.executeWithResults(bookingResultSetExtractor, query, queryParams).stream().findFirst();
    }

    @Override
    public List<Booking> findCompletedByRoomId(Long roomId) {
        String query = namedQueries.getNamedQuery("bookings.get-completed-by-room-id");

        Object[] queryParams = {roomId};

        return queryExecutor.executeWithResults(bookingResultSetExtractor, query, queryParams);
    }
}
