package com.hotel.manager.core.wiring;

import com.hotel.manager.core.repository.RoomRepository;
import com.hotel.manager.core.repository.impl.RoomRepositoryImpl;

public class RoomRepositoryFactory {

    private static RoomRepository instance;

    public static synchronized RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepositoryImpl(
                    NamedQueriesLoader.load(),
                    QueryExecutorFactory.getInstance(),
                    RoomResultSetExtractorFactory.getInstance()
            );
        }

        return instance;
    }
}
