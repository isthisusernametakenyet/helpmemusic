package com.tig167.helpmusic.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(PATTERN);

    public static String format(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return FORMATTER.format(time);
    }

    public static String getMinutesSince(LocalDateTime ldt) {
        return Long.toString(ChronoUnit.MINUTES.between(ldt, LocalDateTime.now()));
    }

}
