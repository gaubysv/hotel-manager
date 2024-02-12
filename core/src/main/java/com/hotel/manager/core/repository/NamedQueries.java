package com.hotel.manager.core.repository;

import com.hotel.manager.core.exception.NamedQueryNotFoundException;
import java.util.Optional;
import java.util.Properties;

public class NamedQueries {

    private final Properties queries;

    public NamedQueries(Properties queries) {
        this.queries = queries;
    }

    public String getNamedQuery(String name) {
        return Optional.ofNullable(queries.getProperty(name))
                .orElseThrow(() -> new NamedQueryNotFoundException(String.format("Named query '%s' not found.", name)));
    }
}
