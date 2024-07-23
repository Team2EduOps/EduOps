package com.team2.eduops.controller;

import java.sql.*;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.SQLException;

/*
**********학생메뉴_1.입실 1.퇴실 메뉴 ************
*/

public class CheckInOut{
    LocalDate today;
    Date sqlDate;
        // localdate(현재 날짜) 를 java.sql.Date로 변환
    LocalDateTime localDateTime;
    Timestamp timestamp;
        // LocalDateTime을 java.sql.Timestamp로 변환

    PreparedStatement pstmt;
    ResultSet rs;

    //********입실 명단을 list에 추가 +DB에 삽입********
    public void update(int stdno) throws SQLException {
        today = LocalDate.now();
        sqlDate = Date.valueOf(today);
        localDateTime = LocalDateTime.now();
        timestamp = Timestamp.valueOf(localDateTime);

        System.out.println("\t 입실이 완료되었습니다.: "+ today);
        String sql = "INSERT INTO ATTENDANCE (std_no,attend_date, ci_time) VALUES (?,?,?)";
        pstmt = ConnectController.getPstmt(sql);
        pstmt.setInt(1,stdno);
        pstmt.setDate(2,sqlDate);
        pstmt.setTimestamp(3,timestamp);
    }

    //*********퇴실시 명단에서 삭제+DB에 update(CO_time과 Attend_status)**********
    public void delete(int stdno) throws SQLException {
        today = LocalDate.now();
        sqlDate = Date.valueOf(today);
        localDateTime = LocalDateTime.now();
        timestamp = Timestamp.valueOf(localDateTime);
        LocalTime cIt=null;
        LocalTime cOt=null;
        LocalTime entryTime=null;
        LocalTime exitTime=null;

        System.out.println("\t 퇴실이 완료되었습니다.: "+ today);
        String sql = "UPDATE ATTENDANCE set CO_TIME = "+timestamp+ " where STD_NO = "+stdno+"and ATTEND_DATE = "+sqlDate;
        pstmt = ConnectController.getPstmt(sql);

        //*******0:결석(default), 1: 출석, 2: 공가, 3: 지각 ,4: 조퇴**********
        sql = "SELECT CI_TIME,CO_TIME from ATTENDANCE where std_no = "+stdno+ " AND ATTEND_DATE="+sqlDate;
        pstmt = ConnectController.getPstmt(sql);
        rs = ConnectController.executePstmtQuery(pstmt);
        if(rs.next()){
            cIt = rs.getTimestamp("CI_TIME").toLocalDateTime().toLocalTime();
            cOt = rs.getTimestamp("CO_TIME").toLocalDateTime().toLocalTime();
                //비교시간 :9시와 18시
            entryTime = LocalTime.of(9,0);
            exitTime = LocalTime.of(18,0);
        }

        int attendStatus = 0; //결석:0 // 공가:1
        if(cIt.isBefore(entryTime)&&cOt.isAfter(exitTime)){
            attendStatus=1;  // 지각:3
        }else if(cIt.isAfter(entryTime)&&cOt.isAfter(exitTime)){
            attendStatus=3;  // 조퇴:4
            }else{  attendStatus=4;}
        //attendStatus update
        sql = "UPDATE ATTENDANCE set ATTEND_STATUS = "+attendStatus+ " where STD_NO = "+stdno+" and ATTEND_DATE = "+sqlDate;
        pstmt = ConnectController.getPstmt(sql);
    }

    //********최종: 명단에 있는지 확인하여, 입/퇴실 여부 출력 :********
    public void checkIO(int stdno) throws SQLException {

        sqlDate = Date.valueOf(LocalDate.now());
        String sql = "SELECT * from ATTENDANCE where std_no = "+stdno+ " AND ATTEND_DATE = "+sqlDate;
        pstmt = ConnectController.getPstmt(sql);
        rs = ConnectController.executePstmtQuery(pstmt);

        if (!rs.isBeforeFirst()) { //값이 없으면, 입실 생성
            System.out.println("\t 1. 입실");
            update(stdno);
        } else { //값이 있으면, 퇴실 생성
            System.out.println("\t 1. 퇴실");
            delete(stdno);
        }
    }
    public void main(String[] args) throws SQLException {
        checkIO(1);
    }
}


