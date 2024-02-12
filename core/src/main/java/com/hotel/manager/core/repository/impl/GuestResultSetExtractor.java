package com.hotel.manager.core.repository.impl;

import com.hotel.manager.core.domain.Guest;
import com.hotel.manager.core.exception.CoreException;
import com.hotel.manager.core.repository.ResultSetExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuestResultSetExtractor implements ResultSetExtractor<Guest> {

    @Override
    public List<Guest> extract(ResultSet resultSet) {
        try {
            List<Guest> guests = new ArrayList<>();

            while (resultSet.next()) {
                Long id = resultSet.getLong("guest_id");
                String name = resultSet.getString("guest_name");
                String surname = resultSet.getString("guest_surname");

                Guest guest = new Guest();
                guest.setId(id);
                guest.setName(name);
                guest.setSurname(surname);
                guests.add(guest);
            }
            return guests;
        } catch (SQLException exception) {
            throw new CoreException(exception.getMessage());
        }
    }
}
