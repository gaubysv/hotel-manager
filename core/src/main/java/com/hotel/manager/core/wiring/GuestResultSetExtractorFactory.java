package com.hotel.manager.core.wiring;

import com.hotel.manager.core.repository.impl.GuestResultSetExtractor;

public class GuestResultSetExtractorFactory {

    private static GuestResultSetExtractor instance;

    public static synchronized GuestResultSetExtractor getInstance() {
        if (instance == null) {
            instance = new GuestResultSetExtractor();
        }

        return instance;
    }
}
