package com.hotel.manager.core.wiring;

import com.hotel.manager.core.service.RoomService;
import com.hotel.manager.core.service.impl.RoomServiceImpl;

public class RoomServiceFactory {

    private static RoomService instance;

    public static synchronized RoomService getInstance() {
        if (instance == null) {
            instance = new RoomServiceImpl(
                    GuestServiceFactory.getInstance(),
                    BookingServiceFactory.getInstance(),
                    RoomRepositoryFactory.getInstance()
            );
        }

        return instance;
    }
}
