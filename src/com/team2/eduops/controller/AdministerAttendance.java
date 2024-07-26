package com.team2.eduops.controller;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.time.DayOfWeek;

/* **********매일 출석부data 갱신,예약하는 스케쥴러:메인에 들어가야함 *******/
public class AdministerAttendance {
 /*   static LocalDate localDate;
    static Date sqlDate;
    static Timestamp timestamp;
    static PreparedStatement pstmt;
    static ResultSet rs;
  //**************
    public void updateAttendance(int vacationCode) {
        String sql = "SELECT vacation_date, VACATE_FILE FROM VACATION WHERE vacation_code = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        boolean bool = true;
        Date vacationDate = null;
        pstmt = ConnectController.getPstmt(sql);
        try {
            pstmt.setInt(1, vacationCode);// 파라미터 설정
            rs = ConnectController.executePstmtQuery(pstmt);
                // 결과가 존재하면, 데이터 처리
            while (rs.next()) {
                    vacationDate = rs.getDate("vacation_date");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //update 먼저 시도: 만약 조퇴이거나 지각을 사유로 처리한거면, 공가인거면 정보가 저장됨
        String updateSQL = "UPDATE attendance SET attend_status = ? WHERE std_no = ? AND attend_date = ?";
        pstmt = ConnectController.getPstmt(updateSQL);
        try {
            pstmt.setInt(1, 2);
            pstmt.setInt(2, stdVo.getStd_no());
            pstmt.setDate(3, vacationDate);// 근태 상태를 2로 설정
            int result = ConnectController.executePstmtUpdate(pstmt);
            if (ConnectController.commit() == -1) {
                System.out.println("커밋 오류");
            }
            //update 될 행이 없을 경우: 0값 반환 :insert함
            if(result==0){
                String insertSQL = "INSERT INTO ATTENDANCE (std_no,attend_date,attend_status) VALUES (?,?,?)";
                pstmt = ConnectController.getPstmt(insertSQL);
                try {
                    pstmt.setInt(1, stdVo.getStd_no());
                    pstmt.setDate(2, vacationDate);
                    pstmt.setInt(3, 2);// 근태 상태를 2로 설정
                    ConnectController.executePstmtUpdate(pstmt);
                    if (ConnectController.commit() == -1) {
                        System.out.println("커밋 오류");
                    }
                } catch (SQLException e) {
                    System.out.println("updateAttendance함수 오류_INSERT -pstmt-SQLException");
                    throw new RuntimeException(e);
                }
            } //if end

        } catch (SQLException e) {
            System.out.println("updateAttendance함수 오류_UPDATE -pstmt-SQLException");
            throw new RuntimeException(e);
        }

        ConnectController.executePstmtUpdate(pstmt);
        if (ConnectController.commit() == -1) {
            System.out.println("커밋 오류");
        }

        String deleteSQL = "DELETE FROM VACATION WHERE vacation_code = ?";
        pstmt = ConnectController.getPstmt(deleteSQL);
        try {
            pstmt.setInt(1,vacationCode);
            int rowsAffected = ConnectController.executePstmtUpdate(pstmt);
            if (ConnectController.commit() == -1) {
                System.out.println("커밋 오류");
            }
            System.out.println(rowsAffected + ": 휴가 승인");
        } catch (SQLException e) {
            System.out.println("updateAttendance함수 오류 -pstmt-SQLException");
            throw new RuntimeException(e);
        }
    }
//학생을 보면서 없는 table도 생성
    public class seeAttendance(){
    String sql = "SELECT S.STD_NAME, NVL(MAX(A.ATTEND_DATE), TO_DATE('1970-01-01', 'YYYY-MM-DD')) AS LAST_ATTEND_DATE " +
            "FROM STUDENT S LEFT JOIN ATTENDANCE A ON S.STD_NO = A.STD_NO " +
            "GROUP BY S.STD_NO";
    PreparedStatement pstmt = ConnectController.getPstmt(sql);
    ResultSet rs = ConnectController.executePstmtQuery(pstmt);
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusDays(30);  // 예를 들어, 지난 30일을 기준으로

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (rs.next()) {
        int studentName = rs.getString("STD_NAME");
        LocalDate lastAttendDate = rs.getDate("LAST_ATTEND_DATE").toLocalDate();

        for (LocalDate date = lastAttendDate.plusDays(1); date.isBefore(today); date = date.plusDays(1)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                String insertQuery = "INSERT INTO ATTENDANCE ( ATTEND_DATE, STD_NO, CI_TIME, CO_TIME, ATTEND_STATUS) " +
                        "VALUES ( ?, ?, NULL, NULL, 0)";
                try (insetPstmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, date.format(formatter));
                    insertStmt.setInt(2, studentNo);
                    insertStmt.executeUpdate();
                }
            }
    }
     int stdNo = ConnectController.scanIntData();
    }
*/
    //******main이 돌고 있을 때, 6:01에 결석자 insert*******
  /*  public static void insertTimer(){

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
                pstmt.setInt(1, stdVo.o);
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