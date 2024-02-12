package com.hotel.manager.core.wiring;

import com.hotel.manager.core.exception.CoreException;
import java.util.Properties;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

public class FlywayMigration {

    public static synchronized void run() {
        try {
            Properties applicationProperties = ApplicationPropertiesLoader.load();

            String locations = applicationProperties.getProperty("flyway.locations");

            Flyway flyway = Flyway.configure()
                    .dataSource(DataSourceFactory.getInstance())
                    .locations(locations)
                    .load();

            flyway.migrate();
        } catch (FlywayException exception) {
            throw new CoreException(exception.getMessage());
        }
    }
}
