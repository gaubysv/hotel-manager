package com.hotel.manager.core.service;

import com.hotel.manager.core.domain.Guest;

public interface GuestService {

    Guest save(Guest guest);

    Guest getGuestById(Long id);
}
