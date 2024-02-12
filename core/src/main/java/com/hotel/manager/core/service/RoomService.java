package com.hotel.manager.core.service;

import com.hotel.manager.core.domain.Guest;
import com.hotel.manager.core.domain.Room;
import com.hotel.manager.core.service.dto.RoomLookupParams;
import java.util.List;

public interface RoomService {

    List<Room> getAllRooms();

    List<Room> lookupRooms(RoomLookupParams params);

    Room checkin(Guest guest);

    void checkout(Long roomId);
}
