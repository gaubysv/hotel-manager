package com.hotel.manager.core.wiring;

import com.hotel.manager.core.repository.QueryExecutor;
import com.hotel.manager.core.repository.impl.QueryExecutorImpl;

public class QueryExecutorFactory {

    private static QueryExecutor instance;

    public static synchronized QueryExecutor getInstance() {
        if (instance == null) {
            instance = new QueryExecutorImpl(DataSourceFactory.getInstance());
        }

        return instance;
    }
}
