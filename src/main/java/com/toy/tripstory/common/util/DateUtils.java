package com.toy.tripstory.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String toStringFormat(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static LocalDateTime getSearchStartDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    public static LocalDateTime getSearchEndDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }


    /**
     * LocalDateTime을 yyyy-MM-dd HH:mm:ss 형식의 String으로 변환하는 메소드
     * @param dateTime
     * @return
     */
    public static String convert_yyyy_mm_dd_hh_mm_ss(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }


}
