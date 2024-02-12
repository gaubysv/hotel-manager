package com.hotel.manager.core.repository;

import com.hotel.manager.core.domain.Room;
import com.hotel.manager.core.service.dto.RoomLookupParams;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room update(Room room);

    Optional<Room> findById(Long id);

    List<Room> findAll();

    List<Room> lookup(RoomLookupParams params);

    Optional<Room> findFirstAvailable();
}
