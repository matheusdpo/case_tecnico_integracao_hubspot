package br.com.meetime.hubspot.v1.utils;

import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateAndHourUtils {

    public static String getDateAndHourNow(String format) {
        return ZonedDateTime
                .now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(format));
    }

    public static String getDateAndHourNow() {
        return getDateAndHourNow("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    }
}
