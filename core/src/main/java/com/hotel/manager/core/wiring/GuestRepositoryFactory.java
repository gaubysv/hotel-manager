package com.hotel.manager.core.wiring;

import com.hotel.manager.core.repository.GuestRepository;
import com.hotel.manager.core.repository.impl.GuestRepositoryImpl;

public class GuestRepositoryFactory {

    private static GuestRepository instance;

    public static synchronized GuestRepository getInstance() {
        if (instance == null) {
            instance = new GuestRepositoryImpl(
                    NamedQueriesLoader.load(),
                    QueryExecutorFactory.getInstance(),
                    GuestResultSetExtractorFactory.getInstance()
            );
        }

        return instance;
    }
}
