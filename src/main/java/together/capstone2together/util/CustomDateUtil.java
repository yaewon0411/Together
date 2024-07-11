package together.capstone2together.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class CustomDateUtil {

    public static String toStringFormat(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static String makeToday() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    public static String makeDday(String day){
        long diffDays = getDiffDaysforMakeDday(makeToday(), day);

        if (diffDays == 0) {
            return "D-day"; // 같은 날인 경우
        } else if (diffDays > 0) {
            return "D-" + diffDays; // 마감일이 미래인 경우
        } else {
            return "마감 일자가 지났습니다.";
        }
    }

    //방에 지원한 시간
    public static String makeAppliedDay(LocalDateTime localDateTime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        long diffHours = getDiffDays(localDateTime, LocalDateTime.now());

        if (diffHours >= 24) {
            return diffHours/24 + "일 전";
        } else {
            if(diffHours == 0) return "방금 전";
            return diffHours+"시간 전";
        }
    }

    public static long getDiffDaysforMakeDday(String startDay, String endDay) {

        String[] dateParts = endDay.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        String[] currentParts = startDay.split("-");
        int currentYear = Integer.parseInt(currentParts[0]);
        int currentMonth = Integer.parseInt(currentParts[1]);
        int currentDay = Integer.parseInt(currentParts[2]);

        //LocalDate deadlineDate = LocalDate.of(year, month, day);

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        //마감일자 세팅
        cal1.set(Calendar.YEAR, year);
        cal1.set(Calendar.MONTH, month);
        cal1.set(Calendar.DATE,day);
        //현재 날짜 세팅
        cal2.set(Calendar.YEAR, currentYear);
        cal2.set(Calendar.MONTH, currentMonth);
        cal2.set(Calendar.DATE, currentDay);

        // 현재 날짜와 마감일자 간의 차이를 계산
        long diffMillis = cal1.getTimeInMillis() - cal2.getTimeInMillis();
        return diffMillis / (24 * 60 * 60 * 1000); // 밀리초를 일로 변환
    }

    public static long getDiffDays(LocalDateTime startDay, LocalDateTime endDay) {

        return ChronoUnit.HOURS.between(startDay, endDay);
    }

    public static String makeCreatedDay(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createdDay= localDateTime.format(formatter);


        long diffHours = getDiffDays(localDateTime, LocalDateTime.now());

        LocalDate currentDate = LocalDate.now();

        String[] dateParts = createdDay.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        LocalDate createdDate = LocalDate.of(year, month, day);

        long hours = Duration.between(createdDate.atStartOfDay(), currentDate.atStartOfDay()).toHours();

        if (hours >= 24) {
            int days = (int) hours / 24; // 일(day)로 변환
            return days + "일 전";
        } else {
            return (int) hours + "시간 전";
        }
    }

    public static String getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentTime.format(formatter);
    }
}
