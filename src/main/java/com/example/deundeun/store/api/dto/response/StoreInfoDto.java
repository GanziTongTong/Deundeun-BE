package com.example.deundeun.store.api.dto.response;

import com.example.deundeun.review.api.dto.response.ReviewInfoDto;
import com.example.deundeun.store.domin.Category;
import com.example.deundeun.store.domin.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Schema(description = "가맹점 상세 정보 DTO")
public record StoreInfoDto(
        @Schema(description = "가맹점 ID", example = "1")
        Long storeId,

        @Schema(description = "가맹점 이름", example = "동동빵집")
        String name,

        @Schema(description = "카테고리 목록", example = "[\"CHILD_MEAL_CARD\", \"GOOD_NEIGHBOR_STORE\"]")
        List<Category> categories,

        @Schema(description = "도로명 주소", example = "경기도 수원시 권선구 덕영대로1217번길 25-4")
        String address,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "영업시간", example = "09:00-21:00")
        String openingHours,

        @Schema(description = "위도", example = "37.55315")
        Double latitude,

        @Schema(description = "경도", example = "127.0240298256")
        Double longitude,

        @Schema(description = "리뷰")
        List<ReviewInfoDto> reviews
) {
    public static StoreInfoDto of(Store store, List<ReviewInfoDto> reviews) {
        return new StoreInfoDto(
                store.getId(),
                store.getFacltNm(),
                store.getCategoryList(),
                store.getRoadnmAddr(),
                store.getPhoneNumber(),
                // [변경] 운영시간 포맷팅 로직 적용
                formatOpeningHours(store.getOpeningHours()),
                parseDoubleOrNull(store.getLat()),
                parseDoubleOrNull(store.getLogt()),
                reviews
        );
    }

    /**
     * 월요일 기준으로 운영시간 포맷팅
     * (Monday가 Closed면 Tuesday 시간을 가져옴)
     */
    private static String formatOpeningHours(String rawHours) {
        if (rawHours == null || rawHours.isBlank()) {
            return null;
        }

        // 1. 월요일 시간 추출
        String timePart = extractTimePart(rawHours, "Monday");

        // 2. [수정] 월요일이 'Closed' 이거나 없으면 -> 화요일 시간 추출
        if (timePart == null || "Closed".equalsIgnoreCase(timePart)) {
            timePart = extractTimePart(rawHours, "Tuesday");
        }

        // 3. 화요일 데이터도 없으면 null 반환 (혹은 원본 반환 등 정책 결정)
        if (timePart == null) {
            return null;
        }

        // 4. 예외 텍스트 처리
        if ("Closed".equalsIgnoreCase(timePart)) {
            return "휴무"; // 화요일도 휴무인 경우
        }

        if ("Open 24 hours".equalsIgnoreCase(timePart)) {
            return "24시간 영업";
        }

        // 5. AM/PM 시간 포맷 변환
        return convertTo24HourFormat(timePart);
    }

    private static String convertTo24HourFormat(String timePart) {
        // 구글 데이터의 특수 공백 문자나 대시를 일반 문자로 치환
        String cleaned = timePart.replace("\u202F", " ") // 좁은 공백 제거
                .replace("\u2009", " ") // 얇은 공백 제거
                .replace("–", "-")      // 엔 대시 -> 하이픈
                .replace("—", "-");     // 엠 대시 -> 하이픈

        // 정규식으로 시작 시간과 종료 시간 추출 (예: 9:00 AM - 6:00 PM)
        // 그룹 1: 시작시간, 그룹 2: 시작AM/PM, 그룹 3: 종료시간, 그룹 4: 종료AM/PM
        Pattern pattern = Pattern.compile("(\\d{1,2}:\\d{2})\\s*([AP]M)\\s*-\\s*(\\d{1,2}:\\d{2})\\s*([AP]M)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(cleaned);

        if (matcher.find()) {
            String start = convertSingleTime(matcher.group(1), matcher.group(2));
            String end = convertSingleTime(matcher.group(3), matcher.group(4));
            return start + "-" + end;
        }

        // 패턴 매칭 실패 시 원본 반환 (혹은 null)
        return timePart;
    }

    // "9:00", "AM" -> "09:00" / "6:00", "PM" -> "18:00" 변환
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

    /**
     * 특정 요일의 시간 문자열만 추출하는 헬퍼 메서드
     */
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

    private static Double parseDoubleOrNull(String s) {
        if (s == null) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}