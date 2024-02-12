package com.hotel.manager.core.wiring;

import com.hotel.manager.core.service.BookingService;
import com.hotel.manager.core.service.impl.BookingServiceImpl;

public class BookingServiceFactory {

    private static BookingService instance;

    public static synchronized BookingService getInstance() {
        if (instance == null) {
            instance = new BookingServiceImpl(
                    BookingRepositoryFactory.getInstance()
            );
        }

        return instance;
    }
}
