package com.hotel.manager.core.wiring;

import java.util.Properties;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public class DataSourceFactory {

    private static JdbcDataSource instance;

    public static synchronized DataSource getInstance() {
        Properties applicationProperties = ApplicationPropertiesLoader.load();

        String url = applicationProperties.getProperty("datasource.url");
        String user = applicationProperties.getProperty("datasource.user");
        String password = applicationProperties.getProperty("datasource.password");

        if (instance == null) {
            instance = new JdbcDataSource();
            instance.setURL(url);
            instance.setUser(user);
            instance.setPassword(password);
        }

        return instance;
    }
}
