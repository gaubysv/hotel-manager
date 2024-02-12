package com.hotel.manager.core.wiring;

import com.hotel.manager.core.repository.impl.RoomResultSetExtractor;

public class RoomResultSetExtractorFactory {

    private static RoomResultSetExtractor instance;

    public static synchronized RoomResultSetExtractor getInstance() {
        if (instance == null) {
            instance = new RoomResultSetExtractor();
        }

        return instance;
    }
}
