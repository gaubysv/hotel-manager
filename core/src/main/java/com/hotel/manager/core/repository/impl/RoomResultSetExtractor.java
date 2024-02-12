package com.hotel.manager.core.repository.impl;

import com.hotel.manager.core.domain.Room;
import com.hotel.manager.core.domain.RoomStatus;
import com.hotel.manager.core.exception.CoreException;
import com.hotel.manager.core.repository.ResultSetExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomResultSetExtractor implements ResultSetExtractor<Room> {

    @Override
    public List<Room> extract(ResultSet resultSet) {
        try {
            List<Room> rooms = new ArrayList<>();

            while (resultSet.next()) {
                Long id = resultSet.getLong("room_id");
                String number = resultSet.getString("room_number");
                String status = resultSet.getString("room_status");

                Room room = new Room();
                room.setId(id);
                room.setNumber(number);
                room.setStatus(RoomStatus.valueOf(status));
                rooms.add(room);
            }
            return rooms;
        } catch (SQLException exception) {
            throw new CoreException(exception.getMessage());
        }
    }
}
