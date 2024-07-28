package com.team2.eduops.controller;

import javax.xml.transform.Result;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

// 배열 생성을 위한 클래스
class DataRecord {
    private int intValue;
    private Date dateValue;

    public DataRecord(int intValue, Date dateValue) {
        this.intValue = intValue;
        this.dateValue = dateValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public Date getDateValue() {
        return dateValue;
    }
}

public class AttendanceUpdater {
    public void attendUpdate() {
        // SQL 쿼리
        //student attendance join해서 student가 가지는 가장 이른 날 입력받음
        ConnectController.connect();
        String sql = "SELECT s.std_no, MIN(a.attend_date) " +
                "AS earliest_attend_date      " +
                "FROM student s        " +
                "JOIN attendance a ON s.std_no = a.std_no     " +
                "GROUP BY s.std_no";

        List<DataRecord> records = new ArrayList<>();
            // 데이터베이스 연결
            PreparedStatement pstmt = ConnectController.getPstmt(sql);
            ResultSet rs = ConnectController.executePstmtQuery(pstmt);
            try {     // 결과 처리
                while (rs.next()) {
                    int stdNo = rs.getInt("std_no");
                    Date earliestDate = rs.getDate("earliest_attend_date");
                    records.add(new DataRecord(stdNo, earliestDate));
                }
            }catch(SQLException e){
                    System.out.println("rs처리 오류");
            }

            LocalDate today = LocalDate.now();
            //가장 이른날~어제 + 월~ 금만!
            for (DataRecord dr : records) {
                LocalDate increaseDate = dr.getDateValue().toLocalDate();
                //1. 체크 - 어제까지의 날짜인지
                while(increaseDate.isBefore(today)) {
                    boolean bool = true;  // 매번 초기화
                    DayOfWeek dayOfWeek = increaseDate.getDayOfWeek();

                    // 주말 체크
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        bool = false;
                    }

                    // Attendance 존재 체크
                    String selectSql = "SELECT * FROM ATTENDANCE WHERE attend_date = ? AND std_no = ?";
                    pstmt = ConnectController.getPstmt(selectSql);
                    try {
                        pstmt.setDate(1, Date.valueOf(increaseDate));
                        pstmt.setInt(2, dr.getIntValue());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    rs = ConnectController.executePstmtQuery(pstmt);
                    try {
                        if (rs.next()) {
                            bool = false;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    // Insert SQL 쿼리 준비
                    if (bool) {
                        String insertSql = "INSERT INTO attendance (std_no, attend_date, attend_status, CI_TIME, CO_TIME) VALUES (?, ?, ?, NULL, NULL)";
                        pstmt = ConnectController.getPstmt(insertSql);
                        try {
                            pstmt.setInt(1, dr.getIntValue());
                            pstmt.setDate(2, Date.valueOf(increaseDate));
                            pstmt.setInt(3, 0); // 결석자는 0으로 설정
                            ConnectController.executePstmtUpdate(pstmt);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        // 커밋
                        if (ConnectController.commit() == -1) {
                            System.out.println("커밋 오류");
                        }
                    }

                    increaseDate = increaseDate.plusDays(1);
                } // while end

            }//for end

    }//atteudUpdate end

}

/*

    public void MidnightTaskScheduler() {
        Timer timer = new Timer(true); //데몬 스레드로 설정
        Calendar now = Calendar.getInstance();
        //12:01 분
        Calendar midnight = (Calendar) now.clone();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 1);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        //오늘 밤 12시 1분 지나면, 다음날로 설정
        if (midnight.before(now)) {
            midnight.add(Calendar.DAY_OF_MONTH, 1);
        }
        //실행 시간과 간격 설정 : 지금시간과 12시 1분까지 지연함
        long delay = midnight.getTimeInMillis() - now.getTimeInMillis();
        long period = 24 * 60 * 60 * 1000; //24시간

        //TimerTask 정의
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

            }
        };

        //예약 걸기
        timer.scheduleAtFixedRate(task, delay, period);

        //메인 스레드가 계속 실행되도록 유지
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
*/
 