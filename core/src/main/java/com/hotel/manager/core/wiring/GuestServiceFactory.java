package com.hotel.manager.core.wiring;

import com.hotel.manager.core.service.GuestService;
import com.hotel.manager.core.service.impl.GuestServiceImpl;

public class GuestServiceFactory {

    private static GuestService instance;

    public static synchronized GuestService getInstance() {
        if (instance == null) {
            instance = new GuestServiceImpl(
                    GuestRepositoryFactory.getInstance()
            );
        }

        return instance;
    }
}
