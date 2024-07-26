package com.team2.eduops.controller;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/* **********매일 출석부data 갱신,예약하는 스케쥴러:메인에 들어가야함 *******/
public class ScheduleAttend {
  /*  static LocalDate localDate;
    static Date sqlDate;
    static Timestamp timestamp;
    static PreparedStatement pstmt;
    static ResultSet rs;

    //******main이 돌고 있을 때, 6:01에 결석자 insert*******
    public static void insertTimer(){
        Timer timer = new Timer();
        TimerTask dailyTask = new TimerTask() {
            LocalDate localDate = LocalDate.now();
            Date sqlDate = Date.valueOf(localDate);
            LocalDateTime localDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            @Override
            public void run() {
                String sql = "INSERT INTO ATTENDANCE (STD_NO, ATTEND_DATE, ATTEND_VALUES) VALUES (?,?,?)";
                pstmt = ConnectController.getPstmt(sql);
                pstmt.setInt(1,stdno);
                pstmt.setDate(2,sqlDate);
                pstmt.setTimestamp(3,timestamp);
                ConnectController.executePstmtUpdate(pstmt);
                if(ConnectController.commit()==-1) {
                    System.out.println("커밋 오류");
                }
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();        // 최초 실행 시간
        long period = 24 * 60 * 60 * 1000;        // 24시간(86400000 밀리초) 주기
        timer.scheduleAtFixedRate(dailyTask, delay, period);        // 타이머 스케줄링

    }
*/
}