package br.com.meetime.hubspot.utils;

import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateAndHourUtils {

    public static String getDateAndHourNow(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return now.format(formatter);
    }

    public static String getDateAndHourNow() {
        return getDateAndHourNow("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    }
}
