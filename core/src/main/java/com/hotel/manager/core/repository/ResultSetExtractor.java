package com.hotel.manager.core.repository;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSetExtractor<T> {

    List<T> extract(ResultSet resultSet);
}
