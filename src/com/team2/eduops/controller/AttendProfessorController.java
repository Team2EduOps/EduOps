package com.team2.eduops.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendProfessorContoller {
    //********lookupAttendance -교수님 메뉴 ->학생관리 -> 1. 학생 보기********
    public static void lookupAttendance (int stdno) {
        String sql = "SELECT VACATE_FILE FROM VACATION WHERE STD_NO = " + stdno;
        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
        try {
            while (rs.next()) {
                InputStream is = rs.getBinaryStream("VACATE_FILE");
                FileOutputStream fos = new FileOutputStream("./image.wpbp");
                byte[] buf = new byte[512];
                int len;
                while(true) {
                    len = is.read(buf);

                    if(len<=0) {
                        break;
                    }

                    fos.write(buf, 0, len);
                }
            }
        } catch (SQLException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //*********approveVacation-교수님 메뉴 ->학생관리 -> 2. 휴가 승인 -> 출석상태 : 공가로 변경********
    public static void updateAttendance(String stdNo, Date attendDate) throws SQLException {
        String updateSQL = "UPDATE attendance SET status = ? WHERE std_no = ? AND attend_date = ?";
        PreparedStatement pstmt = ConnectController.getPstmt(updateSQL);
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
            // PreparedStatement의 파라미터 설정
            pstmt.setInt(1, 2); // 근태 상태를 2로 설정
            pstmt.setString(2, stdNo);
            pstmt.setDate(3, attendDate);
            // 쿼리 실행
            int rowsAffected = ConnectController.executePstmtUpdate(pstmt);
            System.out.println(rowsAffected + ": 휴가 승인");
    }
}