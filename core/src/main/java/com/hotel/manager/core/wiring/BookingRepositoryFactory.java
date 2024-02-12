package com.hotel.manager.core.wiring;

import com.hotel.manager.core.repository.BookingRepository;
import com.hotel.manager.core.repository.impl.BookingRepositoryImpl;

public class BookingRepositoryFactory {

    private static BookingRepository instance;

    public static synchronized BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepositoryImpl(
                    NamedQueriesLoader.load(),
                    QueryExecutorFactory.getInstance(),
                    BookingResultSetExtractorFactory.getInstance()
            );
        }

        return instance;
    }
}
