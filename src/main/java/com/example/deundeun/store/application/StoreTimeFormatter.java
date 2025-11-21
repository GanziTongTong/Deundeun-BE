package com.example.deundeun.store.application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreTimeFormatter {

    // 인스턴스 생성 방지
    private StoreTimeFormatter() {}

    /**
     * 월요일 기준으로 운영시간 포맷팅
     */
    public static String formatOpeningHours(String rawHours) {
        if (rawHours == null || rawHours.isBlank()) {
            return null;
        }

        String timePart = extractTimePart(rawHours, "Monday");

        if (timePart == null || "Closed".equalsIgnoreCase(timePart)) {
            timePart = extractTimePart(rawHours, "Tuesday");
        }

        if (timePart == null) {
            return null;
        }

        if ("Closed".equalsIgnoreCase(timePart)) {
            return "휴무";
        }

        if ("Open 24 hours".equalsIgnoreCase(timePart)) {
            return "24시간 영업";
        }

        return convertTo24HourFormat(timePart);
    }

    private static String convertTo24HourFormat(String timePart) {
        String cleaned = timePart.replace("\u202F", " ")
                .replace("\u2009", " ")
                .replace("–", "-")
                .replace("—", "-");

        Pattern pattern = Pattern.compile("(\\d{1,2}:\\d{2})\\s*([AP]M)\\s*-\\s*(\\d{1,2}:\\d{2})\\s*([AP]M)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(cleaned);

        if (matcher.find()) {
            String start = convertSingleTime(matcher.group(1), matcher.group(2));
            String end = convertSingleTime(matcher.group(3), matcher.group(4));
            return start + "-" + end;
        }

        return timePart;
    }

    private static String convertSingleTime(String time, String ampm) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        String minute = parts[1];

        if (ampm.equalsIgnoreCase("PM") && hour != 12) {
            hour += 12;
        } else if (ampm.equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }

        return String.format("%02d:%s", hour, minute);
    }

    private static String extractTimePart(String rawHours, String day) {
        String target = day + ":";
        int startIndex = rawHours.indexOf(target);

        if (startIndex == -1) {
            return null;
        }

        int endIndex = rawHours.indexOf('\n', startIndex);
        String dayLine = (endIndex == -1)
                ? rawHours.substring(startIndex)
                : rawHours.substring(startIndex, endIndex);

        return dayLine.replace(target, "").trim();
    }
}