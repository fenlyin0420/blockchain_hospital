package com.example.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    /**
     * 获取本地时区的日期
     * @param fullDate 精确到毫秒的 Date 对象
     * @return
     */
    public static Date getLocalDateOnly(Date fullDate) {
        LocalDate localDate = fullDate.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDate();
        return java.util.Date.from(localDate.atStartOfDay()
              .atZone(ZoneId.systemDefault())
              .toInstant());
    }

    public static String dateToString(Date date) {
        // 将Date对象转换为Instant
        Instant instant = date.toInstant();

        // 将Instant转换为LocalDateTime，并指定时区
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 将LocalDateTime按照指定格式转换为字符串
        String dateString = localDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return dateString;
    }
}