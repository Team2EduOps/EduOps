package com.team2.eduops.controller;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.sql.Timestamp;

/*
public static void class CheckInOut {
    Connection conn = DriverManager.getConnection(url,user,password);
    PreparedStatement pstmt = conn
        //입실
        public static void checkIn(int stdno) throws SQLException,ClassNotFoundException {
            System.out.println("입실이 완료되었습니다.");
            LocalDate today = LocalDate.now();
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            DateTimeFormatter formattedtime = DateTimeFormatter.ofPattern("HH:mm:ss");


            pstmt = conn. prepareStatement("INSERT INTO ATTENDANCE VALUES( ?,?,?)");
            pstmt.setDate(2, today);
            pstmt.setInt(3, stdno);
            //update
            pstmt = conn.prepareStatement("UPDATE ATTENDANCE set CI_TIME = "+ currentTimestamp + " where STD_NO = "+ std_no +" and ATTEND_DATE = "+today);
    }

    //퇴실
    public static void checkOut(int stdno) throws SQLException,ClassNotFoundException{
        System.out.pirntln("퇴실이 완료되었습니다.");
        LocalDate today = LocalDate.now();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        DateTimeFormatter formattedtime = DateTimeFormatter.ofPattern("HH:mm:ss");

        pstmt = conn.prepareStatement("UPDATE ATTENDANCE set CO_TIME = "+currentTimestamp+ " where STD_NO = "+stdno+"and ATTEND_DATE = "+today);

        public static void main(String[] args){

        }
    }
}
*/

