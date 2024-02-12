package com.hotel.manager.core.repository;

import com.hotel.manager.core.domain.Guest;
import java.util.Optional;

public interface GuestRepository {

    Guest save(Guest guest);

    Optional<Guest> findById(Long id);
}
