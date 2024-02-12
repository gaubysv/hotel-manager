package com.hotel.manager.core.repository.impl;

import com.hotel.manager.core.exception.CoreException;
import com.hotel.manager.core.repository.QueryExecutor;
import com.hotel.manager.core.repository.ResultSetExtractor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.sql.DataSource;

public class QueryExecutorImpl implements QueryExecutor {

    private final DataSource datasource;

    public QueryExecutorImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public <T> List<T> executeWithResults(ResultSetExtractor<T> extractor, String query, Object... params) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            return extractor.extract(resultSet);
        } catch (Exception exception) {
            throw new CoreException(exception.getMessage());
        }
    }
}
