package com.hotel.manager.core.wiring;

import com.hotel.manager.core.repository.impl.BookingResultSetExtractor;

public class BookingResultSetExtractorFactory {

    private static BookingResultSetExtractor instance;

    public static synchronized BookingResultSetExtractor getInstance() {
        if (instance == null) {
            instance = new BookingResultSetExtractor();
        }

        return instance;
    }
}
