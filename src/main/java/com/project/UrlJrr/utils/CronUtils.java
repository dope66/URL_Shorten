package com.project.UrlJrr.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CronUtils {
    public static String convertToReadableFormat(String cronExpression) {
        String[] parts = cronExpression.split(" ");
        String hour = parts[2];
        String minute = parts[1];
        String period = "am";
        if (Integer.parseInt(hour) >= 12) {
            period = "pm";
            if (!hour.equals("12")) {
                hour = String.valueOf(Integer.parseInt(hour) - 12);
            }
        }

        String readableTime = period + " " + hour + "시 " + minute + "분";
        return readableTime;
    }

    public static String convertToCronFormat(String readableTime) {
        String[] parts = readableTime.split(" ");
        String period = parts[0];
        String hour = parts[1].replace("시", "");
        String minute = parts[2].replace("분", "");
        if (period.equals("pm")) {
            int convertedHour = Integer.parseInt(hour) + 12;
            hour = String.valueOf(convertedHour);
        }

        String cronExpression = "0 " + minute + " " + hour + " * * ?";
        return cronExpression;
    }
}