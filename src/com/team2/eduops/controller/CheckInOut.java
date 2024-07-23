package com.team2.eduops.controller;

import java.sql.*;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.SQLException;


/* *********학생메뉴_1.입실 1.퇴실 메뉴 ************ */

public class CheckInOut{
    static LocalDate localDate;
    static Date sqlDate;
        // localdate(현재 날짜) 를 java.sql.Date로 변환
    static LocalDateTime localDateTime;
    static Timestamp timestamp;
        // LocalDateTime을 java.sql.Timestamp로 변환

    static PreparedStatement pstmt;
    static ResultSet rs;

    //********입실 명단을 list에 추가 +DB에 삽입********
    public static void update(int stdno) throws SQLException {
        localDate = LocalDate.now();
        sqlDate = Date.valueOf(localDate);
        localDateTime = LocalDateTime.now();
        timestamp = Timestamp.valueOf(localDateTime);

        System.out.println("\t 입실이 완료되었습니다.: "+ localDateTime);
        String sql = "INSERT INTO ATTENDANCE (std_no,attend_date, ci_time) VALUES (?,?,?)";
        pstmt = ConnectController.getPstmt(sql);
        pstmt.setInt(1,stdno);
        pstmt.setDate(2,sqlDate);
        pstmt.setTimestamp(3,timestamp);
        ConnectController.executePstmtUpdate(pstmt);
        if(ConnectController.commit()==-1) {
            System.out.println("커밋 오류");
        }
    }

    //*********퇴실시 명단에서 삭제+DB에 update(CO_time과 Attend_status)**********
    public static void delete(int stdno) throws SQLException {
        localDate = LocalDate.now();
        sqlDate = Date.valueOf(localDate);
        localDateTime = LocalDateTime.now();
        timestamp = Timestamp.valueOf(localDateTime);
        LocalTime cIt=null,cOt=null,entryTime=null,exitTime=null;

        System.out.println("\t 퇴실이 완료되었습니다.: "+ localDateTime);
        String sql = "UPDATE ATTENDANCE set CO_TIME = ? where STD_NO = ? and ATTEND_DATE = ?";
        pstmt = ConnectController.getPstmt(sql);
        pstmt.setTimestamp(1,timestamp);
        pstmt.setInt(2,stdno);
        pstmt.setDate(3,sqlDate);
        ConnectController.executePstmtUpdate(pstmt);
        if(ConnectController.commit()==-1) {
            System.out.println("커밋 오류");
        }

        //*******0:결석(default), 1: 출석, 2: 공가, 3: 지각 ,4: 조퇴**********
        sql = "SELECT CI_TIME,CO_TIME from ATTENDANCE where std_no = ? AND ATTEND_DATE = ?";
        pstmt = ConnectController.getPstmt(sql);
        pstmt.setInt(1,stdno);
        pstmt.setDate(2,sqlDate);
        rs = ConnectController.executePstmtQuery(pstmt);
        if(rs.next()){
            cIt = rs.getTimestamp("CI_TIME").toLocalDateTime().toLocalTime();
            cOt = rs.getTimestamp("CO_TIME").toLocalDateTime().toLocalTime();
                //비교시간 :9시와 18시
            entryTime = LocalTime.of(9,0);
            exitTime = LocalTime.of(18,0);
        }

        //attendStatus update
        int attendStatus = 0; //결석:0 // 공가:1
        if(cIt!=null){
            if(cIt.isBefore(entryTime)&&cOt.isAfter(exitTime)){
                attendStatus=1;  // 지각:3
            }else if(cIt.isAfter(entryTime)&&cOt.isAfter(exitTime)){
                attendStatus=3;  // 조퇴:4
            }else{  attendStatus=4;}
        }else{ System.out.println("입실을 하지 않았습니다. 입실 먼저해주세요."); }

        sql = "UPDATE ATTENDANCE set ATTEND_STATUS = ? where STD_NO = ? and ATTEND_DATE = ?";
        pstmt = ConnectController.getPstmt(sql);
        pstmt.setInt(1,attendStatus);
        pstmt.setInt(2,stdno);
        pstmt.setDate(3,sqlDate);
        ConnectController.executePstmtUpdate(pstmt);
        if(ConnectController.commit()==-1) {
            System.out.println("커밋 오류");
        }
    }

    //********명단에 있는지 확인하여, 입/퇴실 메뉴 보이기 :********
    public static boolean showcheckIO(int stdno) throws SQLException {
        localDate = LocalDate.now();
        sqlDate = Date.valueOf(localDate);
        System.out.println(localDate);
        String sql = "SELECT * from ATTENDANCE where std_no = ? AND ATTEND_DATE = ?";
        pstmt = ConnectController.getPstmt(sql);
        pstmt.setInt(1,stdno);
        pstmt.setDate(2,sqlDate);
        rs = ConnectController.executePstmtQuery(pstmt);

        if (!rs.isBeforeFirst()) { //값이 없으면, 입실 생성
            return true;
        } else { //값이 있으면, 퇴실 생성
            return false;
        }
    }

    //********최종:입실 또는 퇴실 *****************
    public static void checkIO(int stdno) throws SQLException {
        if(showcheckIO(stdno)){
            System.out.println("\t 1. 입실");
            update(stdno);
        }else{
            System.out.println("\t 1. 퇴실");
            delete(stdno);
        }
    }
}


