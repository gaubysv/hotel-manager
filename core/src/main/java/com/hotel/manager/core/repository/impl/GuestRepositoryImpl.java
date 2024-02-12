package com.hotel.manager.core.repository.impl;

import com.hotel.manager.core.domain.Guest;
import com.hotel.manager.core.repository.GuestRepository;
import com.hotel.manager.core.repository.NamedQueries;
import com.hotel.manager.core.repository.QueryExecutor;
import java.util.Optional;

public class GuestRepositoryImpl implements GuestRepository {

    private final NamedQueries namedQueries;
    private final QueryExecutor queryExecutor;
    private final GuestResultSetExtractor resultSetExtractor;

    public GuestRepositoryImpl(NamedQueries namedQueries, QueryExecutor queryExecutor, GuestResultSetExtractor resultSetExtractor) {
        this.namedQueries = namedQueries;
        this.queryExecutor = queryExecutor;
        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public Guest save(Guest guest) {
        String query = namedQueries.getNamedQuery("guests.save");

        Object[] queryParams = {
                guest.getName(),
                guest.getSurname()
        };

        return queryExecutor.executeWithResults(resultSetExtractor, query, queryParams).stream().findFirst().orElse(null);
    }

    @Override
    public Optional<Guest> findById(Long id) {
        String query = namedQueries.getNamedQuery("guests.get-by-id");

        Object[] queryParams = {id};

        return queryExecutor.executeWithResults(resultSetExtractor, query, queryParams).stream().findFirst();
    }
}
