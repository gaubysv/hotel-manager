package com.hotel.manager.core.wiring;

import com.hotel.manager.core.exception.CoreException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesLoader {

    private static final String[] LOCATIONS = {
            "application.properties",
    };

    private static Properties applicationProperties;

    public static synchronized Properties load() {
        if (applicationProperties == null) {
            applicationProperties = new Properties();
            for (String location : LOCATIONS) {
                loadPropertiesFromLocation(location);
            }
        }

        return applicationProperties;
    }

    private static void loadPropertiesFromLocation(String location) {
        try (InputStream resource = ApplicationPropertiesLoader.class.getClassLoader().getResourceAsStream(location)) {
            applicationProperties.load(resource);
        } catch (Exception e) {
            throw new CoreException(e.getMessage());
        }
    }
}
