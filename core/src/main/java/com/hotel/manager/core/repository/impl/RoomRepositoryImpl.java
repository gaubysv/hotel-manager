package com.hotel.manager.core.repository.impl;

import com.hotel.manager.core.domain.Room;
import com.hotel.manager.core.repository.NamedQueries;
import com.hotel.manager.core.repository.QueryExecutor;
import com.hotel.manager.core.repository.RoomRepository;
import com.hotel.manager.core.service.dto.RoomLookupParams;
import java.util.List;
import java.util.Optional;

public class RoomRepositoryImpl implements RoomRepository {

    private final NamedQueries namedQueries;
    private final QueryExecutor queryExecutor;
    private final RoomResultSetExtractor resultSetExtractor;

    public RoomRepositoryImpl(NamedQueries namedQueries, QueryExecutor queryExecutor, RoomResultSetExtractor resultSetExtractor) {
        this.namedQueries = namedQueries;
        this.queryExecutor = queryExecutor;
        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public Room update(Room room) {
        String query = namedQueries.getNamedQuery("rooms.update");

        Object[] queryParams = {
                room.getNumber(),
                room.getStatus().name(),
                room.getId()
        };

        return queryExecutor.executeWithResults(resultSetExtractor, query, queryParams).stream().findFirst().orElse(null);
    }

    @Override
    public Optional<Room> findById(Long id) {
        String query = namedQueries.getNamedQuery("rooms.get-by-id");

        Object[] queryParams = {id};

        return queryExecutor.executeWithResults(resultSetExtractor, query, queryParams).stream().findFirst();
    }

    @Override
    public List<Room> findAll() {
        String query = namedQueries.getNamedQuery("rooms.get-all");
        return queryExecutor.executeWithResults(resultSetExtractor, query);
    }

    @Override
    public List<Room> lookup(RoomLookupParams params) {
        String query = namedQueries.getNamedQuery("rooms.lookup");

        // Have to pass same param twice as it is used two times in the query.
        Object[] queryParams = {
                params.getNumber(),
                params.getNumber(),
                params.getStatus(),
                params.getStatus()
        };

        return queryExecutor.executeWithResults(resultSetExtractor, query, queryParams);
    }

    @Override
    public Optional<Room> findFirstAvailable() {
        String query = namedQueries.getNamedQuery("rooms.get-available");
        return queryExecutor.executeWithResults(resultSetExtractor, query).stream().findFirst();
    }
}
