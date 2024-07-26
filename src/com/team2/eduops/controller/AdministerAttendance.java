//package com.team2.eduops.controller;
//import java.sql.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Calendar;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.time.DayOfWeek;
//
//
//public class AdministerAttendance {
//
//    //학생 보기
//    public void seeAttendance() {
//        String sql = "SELECT S.STD_NAME, NVL(MAX(A.ATTEND_DATE), TO_DATE('1970-01-01', 'YYYY-MM-DD')) AS LAST_ATTEND_DATE " +
//                "FROM STUDENT S LEFT JOIN ATTENDANCE A ON S.STD_NO = A.STD_NO " +
//                "GROUP BY S.STD_NO";
//        PreparedStatement pstmt = ConnectController.getPstmt(sql);
//        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
//        LocalDate today = LocalDate.now();
//        LocalDate startDate = today.minusDays(30);  // 예를 들어, 지난 30일을 기준으로
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        while (rs.next()) {
//            int studentName = rs.getString("STD_NAME");
//            LocalDate lastAttendDate = rs.getDate("LAST_ATTEND_DATE").toLocalDate();
//
//            for (LocalDate date = lastAttendDate.plusDays(1); date.isBefore(today); date = date.plusDays(1)) {
//                if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
//                    String insertQuery = "INSERT INTO ATTENDANCE ( ATTEND_DATE, STD_NO, CI_TIME, CO_TIME, ATTEND_STATUS) " +
//                            "VALUES ( ?, ?, NULL, NULL, 0)";
//                    try (Pstmt =connection.prepareStatement(insertQuery)){
//                        pstmt.setString(1, date.format(formatter));
//                        pstmt.setInt(2, studentNo);
//                        pstmt.executeUpdate();
//                    }
//                }
//            }
//        }
//    }
//}