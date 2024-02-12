package com.hotel.manager.core.service.impl;

import com.hotel.manager.core.domain.Guest;
import com.hotel.manager.core.exception.GuestNotFoundException;
import com.hotel.manager.core.repository.GuestRepository;
import com.hotel.manager.core.service.GuestService;

public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest save(Guest guest) {
        return guestRepository.save(guest);
    }

    @Override
    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(String.format("Guest %s not found.", id)));
    }
}
