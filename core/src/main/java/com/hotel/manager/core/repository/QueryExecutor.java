package com.hotel.manager.core.repository;

import java.util.List;

public interface QueryExecutor {

    <T> List<T> executeWithResults(ResultSetExtractor<T> extractor, String query, Object... params);
}
