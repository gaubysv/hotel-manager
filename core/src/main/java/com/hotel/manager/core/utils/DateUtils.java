package com.hotel.manager.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        return localDateTime.format(formatter);
    }

    public static LocalDateTime parse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(FORMAT));
    }

    private DateUtils() {
        // NO-OP
    }
}
