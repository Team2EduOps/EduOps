package com.team2.eduops.controller;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.StudentVO;

import javax.xml.transform.Result;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendProfessorContoller {
   //********출결 보기 ********

    //********오늘 출석을 보기 위한 STD_NAME, SEAT_NO,ATTEND_STATUS, CI_TIME,CO_TIME을 학생별로 저장할 클래스 생성
    public class TodayAttendance {
        String stdName;
        int seatNo;
        int attendStatus;
        LocalTime ciTime;
        LocalTime coTime;

        public TodayAttendance(String stdName,int seatNo,int attendStatus, LocalTime ciTime, LocalTime coTime){
            this.stdName = stdName;
            this.seatNo =seatNo;
            this.attendStatus = attendStatus;
            this.ciTime = ciTime;
            this.coTime = coTime;
        }
        public int getSeatNo(){
            return seatNo;
        }
        public int getAttendStatus(){
            return attendStatus;
        }
        public String getStdName() {
            return stdName;
        }
        public LocalTime getCiTime(){
            return ciTime;
        }
        public LocalTime getCoTime(){
            return coTime;
        }
        public void setAttendStatus(int attendStatus){
            this.attendStatus=attendStatus;
        }
    }
    //********교수-학생관리-학생보기
    public void lookupStudent(){
        System.out.println("학생 보기 페이지입니다.");
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        //select문으로 STD_NAME, SEAT_NO,ATTEND_STATUS, CI_TIME,CO_TIME 가져옴
        //NVL 은 null 값일 경우, 기본값 00:00:00으로 설정
        String sql = "SELECT s.STD_NAME, " +
                "       s.SEAT_NO, " +
                "       NVL(a.ATTEND_STATUS, 0) AS ATTEND_STATUS) "+
                "       NVL(a.CI_TIME, TO_TIMESTAMP('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')) AS CI_TIME, " +
                "       NVL(a.CO_TIME, TO_TIMESTAMP('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')) AS CO_TIME " +
                "FROM student s " +
                "LEFT JOIN attendance a ON s.STD_NO = a.STD_NO " +
                "AND a.ATTEND_DATE = ?";

        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        try {
            pstmt.setDate(1, sqlDate);
        } catch (SQLException e) {
            System.out.println("lookupStudent()함수 오류- SQLException");
            throw new RuntimeException(e);
        }
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);

        //STD_NAME, SEAT_NO,ATTEND_STATUS, CI_TIME,CO_TIME 저장할 arrayList 생성
        ArrayList<TodayAttendance> todayList = new ArrayList<>();
        try {
            while (rs.next()) {
                String stdName = rs.getString("STD_NAME");
                int seatNo= rs.getInt("STD_NO");
                int attendStatus = rs.getInt("ATTEND_STATUS");
                Time sqlCiTime = rs.getTime("CI_TIME");
                LocalTime ciTime = sqlCiTime.toLocalTime();
                Time sqlCoTime = rs.getTime("CO_TIME");
                LocalTime coTime = sqlCoTime.toLocalTime();
                todayList.add(new TodayAttendance(stdName,seatNo,attendStatus, ciTime, coTime));
            }
        }catch (SQLException e){
            System.out.println("lookupstudent 함수 오류 - SQLException");
            throw new RuntimeException(e);
        }

        LocalTime ciBaseTime = LocalTime.of(9, 0, 0); // 09:00:00
        LocalTime coBaseTime = LocalTime.of(6, 0, 0); // 06:00:00
        LocalTime XTime = LocalTime.of(0,0,0); //00:00:00


        todayList.sort((a, b) -> Integer.compare(a.getSeatNo(), b.getSeatNo()));
        String[][] todayArray = new String[todayList.size()][2];
        //ciTime과 coTime으로 오늘의 attendStatus값 결정
        for(int i =0; i<todayList.size() ; i++) {
            //결석과 공가는 DB에서 가져올 때, 알아서 0과 2로 저장됨
            // 퇴실시에 attendStatus가 update되므로,
            //모든 attend_status = null ->0으로 가져옴
            //ciTime이 00:00:00일 수 없다. (00:00:00)에 입실하지 않는 이상, 이러한 예외는 시간 부족으로 처리 X
            TodayAttendance tA = todayList.get(i);
            //이미 종료된 수업에 대해서는 attendStatus가 수정되므로 돌리지 X
            if(tA.getAttendStatus()==0) {
                //지각: ciTime!=0 이고  ciTime>9시
                if (!tA.getCiTime().equals(XTime) && tA.getCiTime().isAfter(ciBaseTime)) {
                    tA.setAttendStatus(3); //지각
                    if (tA.getCoTime().isBefore(coBaseTime)) {
                        tA.setAttendStatus(4); // 지각 후 조퇴
                    }
                }
                //출석: ciTime!=0 이고 ciTime <9시
                if (!tA.getCiTime().equals(XTime) && tA.getCiTime().isBefore(ciBaseTime)) { //출석: ciTime <9시
                    tA.setAttendStatus(1); //출석
                    if (tA.getCoTime().isBefore(coBaseTime)) {
                        tA.setAttendStatus(4); // 출석 후 조퇴
                    }
                }
            }//if end
            String printStatus = "";
            switch(tA.getAttendStatus()){
                case 0: printStatus = "X"; break;
                case 2: printStatus ="ㅁ"; break;
                case 1: printStatus = "O"; break;
                case 3,4: printStatus = "△"; break;
                default: break;
            }
            todayArray[i][0]= printStatus;
            todayArray[i][1]=tA.stdName;
        }//for end
        int total = todayArray.length;
        int chunk=5;
        for (int i = 0;i<total ;i+=chunk ){ //i 는 5의 배수로 커짐
            //i+j는 현재 index값, j <5이고, (i+j)<total일 때만 형성
            for (int j = 0; j <chunk  && (i + j) < total; j++) {
                System.out.print(todayArray[i + j][0] + "\t" );
            }
            System.out.println(); // 두 번째 줄을 출력하기 전에 한 줄 바꿈
            for (int j = 0; j < chunk && (i + j) < total; j++) {
                System.out.print(todayArray[i + j][1] + "\t");
            }
            System.out.println(); // 한 줄 바꿈
        }
    }


    //*********교수-학생관리-휴가승인-휴가 보여줌
    public int lookupVacation (StudentVO stdVo) {
        String sql = "SELECT * FROM VACATION WHERE STD_NO = " + stdVo.getStd_no();
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
            System.out.println("lookupAttendance함수 - SQLException오류");
            e.printStackTrace();
        }
        System.out.print("승인할 휴가의 번호를 입력하세요.:");
        return ConnectController.scanIntData();
    }

    //*********approveVacation-교수 -학생관리 - 휴가 승인
    // select(vacation):vacation_date,
    // Update(attendance):attend_status, Insert(attendance):attend_status
    // delete(vacation):vacation_code***********
    public void updateAttendance(int vacationCode,StudentVO stdVo) {
        String sql = "SELECT vacation_date from VACATION WHERE vacation_code = "+vacationCode;
        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
        Date vacationDate = null;
        try {
            if (!rs.isBeforeFirst()) {
                System.out.println("\t 해당 날짜에 데이터가 없습니다.");
            } else {
                while (rs.next()) {
                    vacationDate = rs.getDate("vacation_date");
                } //내부 while end
            }
        } catch (SQLException e) {
            System.out.println("updateAttendance오류 : rs-SQLExeption 오류");
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
            //update 될 행이 없을 경우: 0값 반환 :insert함
            if(result==0){
                String insertSQL = "INSERT INTO ATTENDANCE (std_no,attend_date,attend_status) VALUES (?,?,?)";
                pstmt = ConnectController.getPstmt(insertSQL);
                try {
                    pstmt.setInt(1, stdVo.getStd_no());
                    pstmt.setDate(2, vacationDate);
                    pstmt.setInt(3, 2);// 근태 상태를 2로 설정

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
            ConnectController.executePstmtUpdate(pstmt);
        } catch (SQLException e) {
            System.out.println("updateAttendance함수 오류 -pstmt-SQLException");
            throw new RuntimeException(e);
        }
            int rowsAffected = ConnectController.executePstmtUpdate(pstmt);
            System.out.println(rowsAffected + ": 휴가 승인");
    }
    /*
    public class seeAttendance(AdminVO adminVO){
        String sql = "SELECT * FROM STUDENT";
        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
            while (rs.next()) {
                System.out.println(rs.getInt("STD_NO") + ":" + rs.getString("STD_NAME"));
            }

        System.out.print("확인하고자 하는 학생의 번호: ");
        int stdNo = ConnectController.scanIntData();
        }
        */
}
