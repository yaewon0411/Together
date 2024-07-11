package together.capstone2together.util;

import together.capstone2together.domain.surveyAnswer.SurveyAnswer;
import together.capstone2together.domain.member.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import static together.capstone2together.dto.member.MemberRespDto.*;

public class CustomDataUtil {

    public static MyInfoRespDto makeMyInfo(Member member, List<SurveyAnswer> surveyAnswers){
        return new MyInfoRespDto().builder()
                .point(member.getPoint())
                .name(member.getName())
                .createdRoomCnt(member.getLedRooms().size())
                .applyRoomCnt(surveyAnswers.size())
                .build();
    }



    private static String makeToday() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
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

}
