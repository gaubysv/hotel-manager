package com.hotel.manager.core.wiring;

public class CoreModule {

    public static synchronized void initialize() {
        FlywayMigration.run();
    }
}
