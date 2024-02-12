package com.hotel.manager.core.wiring;

import com.hotel.manager.core.exception.CoreException;
import com.hotel.manager.core.repository.NamedQueries;
import java.io.InputStream;
import java.util.Properties;

public class NamedQueriesLoader {

    private static final String[] LOCATIONS = {
            "db/queries/room-queries.xml",
            "db/queries/guest-queries.xml",
            "db/queries/booking-queries.xml",
    };

    private static NamedQueries namedQueries;

    public static synchronized NamedQueries load() {
        if (namedQueries == null) {
            Properties queries = new Properties();
            for (String location : LOCATIONS) {
                loadNamedQueriesFromLocation(location, queries);
            }

            namedQueries = new NamedQueries(queries);
        }

        return namedQueries;
    }

    private static void loadNamedQueriesFromLocation(String location, Properties queries) {
        try (InputStream resource = NamedQueriesLoader.class.getClassLoader().getResourceAsStream(location)) {
            queries.loadFromXML(resource);
        } catch (Exception e) {
            throw new CoreException(e.getMessage());
        }
    }
}
