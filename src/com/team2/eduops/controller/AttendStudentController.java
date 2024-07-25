package com.team2.eduops.controller;

import com.team2.eduops.model.StudentVO;

import java.sql.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//date형식으로 받아오기 위한 library

//학생 출결 관리 관련 메소드들
public class AttendStudentController {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //*********날짜 형식 맞는지 확인 메소드************
    private static boolean isValidDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return false;
        }
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    //*************5-1. lookupDaily-일자별 조회(근태 page)************
    public void lookupDaily(StudentVO stdVo) {
        String inputDate;
        boolean bool = true;
        while (bool) {
            System.out.print("\t OOOO-OO-OO 형식에 맞춰 입력해주세요:");
            inputDate = ConnectController.scanData();

            if (isValidDate(inputDate)) { //날짜형식에 맞다면!
                String sql = "SELECT ATTEND_STATUS FROM ATTENDANCE WHERE TO_CHAR(ATTEND_DATE,'YYYY-MM-DD') = '" + inputDate + "' AND STD_NO = " + stdVo.getSeat_no();
                PreparedStatement pstmt = ConnectController.getPstmt(sql);
                ResultSet rs = ConnectController.executePstmtQuery(pstmt);
                if (UtilController.isNull(rs)) {
                    System.out.println("lookupDaily 함수: rs=Null오류");
                }
                //case1- DB에 없는 값, 튜플 X
                try {
                    if (!rs.isBeforeFirst()) {
                        System.out.println("\t 해당 날짜에 데이터가 없습니다.");
                    } else {
                        while (rs.next()) {
                            Object columnValue = rs.getObject("Attend_status");

                            // case2-오늘 퇴실 처리 X ,튜플 O, 상태 X
                            if (columnValue == null) {
                                System.out.println("\t 오늘은 퇴실 처리를 하지 않아 조회가 불가합니다.");
                                bool = false;
                            }

                            // case3- DB에 튜플 O, 상태 O
                            int result = rs.getInt("Attend_status");
                            switch (result) {
                                case 0:
                                    System.out.println("\t " + inputDate + "날 결석하였습니다.");
                                    break;
                                case 1:
                                    System.out.println("\t " + inputDate + "날 출석하였습니다.");
                                    break;
                                case 2:
                                    System.out.println("\t " + inputDate + "날 공가(출석)하였습니다.");
                                    break;
                                case 3:
                                    System.out.println("\t " + inputDate + "날 지각하였습니다.");
                                    break;
                                case 4:
                                    System.out.println("\t " + inputDate + "날 조퇴하였습니다.");
                                    break;
                                default:
                                    break;
                            }
                            bool = false;
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("lookupDaily함수: rs-SQLEXception 문데");
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("\t 올바른 형식이 아닙니다.. 다시 입력해 주세요.");
            }//if end
        }
    }//while end

    //********년 월 형식 맞는지 확인 메소드**********
    public static boolean isValidYearMonth(String date) {
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM");
        final String DATE_PATTERN = "yyyy-MM";
        try {
            Date parseDate = DATE_FORMAT.parse(date);
            return DATE_FORMAT.format(parseDate).equals(date);
        } catch (ParseException e) {
            return false;
        }
    }

    //*************5-2. 월별 조회(근태 page):lookupMonthly***************
    public void lookupMonthly(StudentVO stdVo) {
        boolean bool = true;
        while (bool) {
            System.out.print("\t YYYY-MM 형식 입력: ");
            String inputMonth = ConnectController.scanData();

            if (isValidYearMonth(inputMonth)) { //맞는 형식으로 입력!
                String firstdate = inputMonth + "-01";
                String lastdate = inputMonth + "-31";
                String sql = "SELECT TO_CHAR(ATTEND_DATE,'YYYY-MM-DD') ,ATTEND_STATUS " +
                        "FROM ATTENDANCE WHERE ATTEND_DATE BETWEEN TO_DATE('" + firstdate + "','YYYY-MM-DD') AND TO_DATE('" + lastdate + "','YYYY-MM-DD') " +
                        "AND STD_NO = " + stdVo.getStd_no();
                PreparedStatement pstmt = ConnectController.getPstmt(sql);
                ResultSet rs = ConnectController.executePstmtQuery(pstmt);
                //case1- DB에 없는 값, 튜플 X
                try {
                    if (!rs.isBeforeFirst()) {
                        System.out.println("\t 해당 월에 데이터가 없습니다.");
                    } else {
                        System.out.println("\t Attend_status= 0:결석,1:출석,2:공가,3:지각,4:조퇴");
                        while (rs.next()) {
                            Object columnValue = rs.getObject("Attend_status");
                            // case2- DB에 튜플 O, 상태 O
                            System.out.print("\t 날짜: " + rs.getString(1));
                            System.out.println("\t Attend_status: " + rs.getInt(2));

                            // case3-오늘 퇴실 처리 X ,튜플 O, 상태 X
                            if (columnValue == null) {
                                System.out.println("\t 오늘은 퇴실 처리를 하지 않아 조회가 불가합니다.");
                            } //if end
                        }//for end
                        bool = false;
                    } //내부 while end
                } catch (SQLException e) {
                    System.out.println("lookupmonthly: rs-SQLExeption e오류");
                    throw new RuntimeException(e);
                }
            }
        }
    }//lookupMonthly end

    //**********5-3.누적 지원금 메뉴 : showCashMenu********
    //*************applyVacation- 휴가 신청(근태 page)*************************
    public void applyVacation(StudentVO stdVo) {
        LocalDate localDate = LocalDate.now();
        Date sqlDate = java.sql.Date.valueOf(localDate);
        String sql = "INSERT INTO VACATION(VACATE_DATE, VACATE_FILE, STD_NO, ADM_NO) VALUES(?, ?, ?, ?)";
        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        String path = ConnectController.scanData();

        File f = new File("./" + path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            System.out.println("applyVacation함수- FileNotFoundException 오류");
            throw new RuntimeException(e);
        }

        try {
            pstmt.setDate(1, (java.sql.Date) sqlDate);
            pstmt.setBinaryStream(2, fis, (int) f.length());
            pstmt.setInt(3, stdVo.getStd_no());
            pstmt.setInt(4, 1);
        } catch (SQLException e) {
            System.out.println("applyVacation함수 - pstmt- SQLException 오류");
            throw new RuntimeException(e);
        }
        int success = ConnectController.executePstmtUpdate(pstmt);
        ConnectController.commit();

        if (success != -1) {
            System.out.println("파일이 추가되었습니다.");
        }
    }//showcashmenu end

    //*********현재 누적 지원금 조회*************
    public void lookupCashPresent(StudentVO stdVo) {
        // 현재 날짜와 시간을 가져옵니다.
        Date now = new Date();
        // SimpleDateFormat을 사용하여 "yyyy-MM" 형식으로 포맷팅합니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String formattedDate = sdf.format(now);
        // 출석 상태 개수를 저장할 배열
        int[] statusCount = new int[5]; // 0: 결석, 1: 출석, 2: 공가, 3: 지각, 4: 조퇴
        String firstdate = formattedDate + "-01";
        String lastdate = formattedDate + "-31";
        String sql = "SELECT ATTEND_STATUS " +
                "FROM ATTENDANCE WHERE ATTEND_DATE BETWEEN TO_DATE('" + firstdate + "','YYYY-MM-DD') AND TO_DATE('" + lastdate + "','YYYY-MM-DD') " +
                "AND STD_NO = " + stdVo.getStd_no();

        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
        //case1- DB에 없는 값, 튜플 X
        try {
            if (!rs.isBeforeFirst()) {
                System.out.println("\t 해당 월에 데이터가 없습니다.");
            } else {
                while (rs.next()) {
                    int status = rs.getInt("attend_status");
                    if (status >= 0 && status <= 4) {
                        statusCount[status]++;
                    }
                }
                System.out.println("결석(0): " + statusCount[0]);
                System.out.println("출석(1): " + statusCount[1]);
                System.out.println("공가(2): " + statusCount[2]);
                System.out.println("지각(3): " + statusCount[3]);
                System.out.println("조퇴(4): " + statusCount[4]);
                int amount = 300000;
                int index0Value = statusCount[0];
                amount -= index0Value * 10000;

                // 인덱스 3의 값을 3으로 나눈 몫에 따라 1만원씩 감소
                int index3Value = statusCount[3];
                amount -= (index3Value / 3) * 10000;

                // 결과 출력
                System.out.println("현재 누적 금액: " + amount + "원");
            } //내부 while end
        } catch (SQLException e) {
            System.out.println("lookupCashPresent 함수 오류: rs-SQLEXception");
            throw new RuntimeException(e);
        }
    }//lookupCashPresent end

    //lookupCashPast- 지난 누적 지원금 조회 월별(근태 page)
    public void lookupCashPast(StudentVO stdVo) {
        boolean bool = true;
        while (bool) {
            System.out.print("\t YYYY-MM 형식 입력: ");
            String inputMonth = ConnectController.scanData();

            if (isValidYearMonth(inputMonth)) { //맞는 형식으로 입력!
                int[] statusCount = new int[5]; // 0: 결석, 1: 출석, 2: 공가, 3: 지각, 4: 조퇴
                String firstdate = inputMonth + "-01";
                String lastdate = inputMonth + "-31";
                String sql = "SELECT ATTEND_STATUS " +
                        "FROM ATTENDANCE WHERE ATTEND_DATE BETWEEN TO_DATE('" + firstdate + "','YYYY-MM-DD') AND TO_DATE('" + lastdate + "','YYYY-MM-DD') " +
                        "AND STD_NO = " + stdVo.getStd_name();
                PreparedStatement pstmt = ConnectController.getPstmt(sql);
                ResultSet rs = ConnectController.executePstmtQuery(pstmt);
                //case1- DB에 없는 값, 튜플 X
                try {
                    if (!rs.isBeforeFirst()) {
                        System.out.println("\t 해당 월에 데이터가 없습니다.");
                    } else {
                        while (rs.next()) {
                            int status = rs.getInt("attend_status");
                            if (status >= 0 && status <= 4) {
                                statusCount[status]++;
                            }
                        }//while end
                        System.out.println("결석(0): " + statusCount[0]);
                        System.out.println("출석(1): " + statusCount[1]);
                        System.out.println("공가(2): " + statusCount[2]);
                        System.out.println("지각(3): " + statusCount[3]);
                        System.out.println("조퇴(4): " + statusCount[4]);
                        // 인덱스 0의 값에 따라 1만원씩 감소
                        int amount = 300000;
                        int index0Value = statusCount[0];
                        amount -= index0Value * 10000;

                        // 인덱스 3의 값을 3으로 나눈 몫에 따라 1만원씩 감소
                        int index3Value = statusCount[3];
                        amount -= (index3Value / 3) * 10000;

                        // 결과 출력
                        System.out.println(inputMonth + "월 금액: " + amount + "원");

                    } //if-else end: 월 데이터 구분
                } catch (SQLException e) {
                    System.out.println("lookupCashPash-rs-SQLException 오류");
                    throw new RuntimeException(e);
                }//try-catch end: rs 관련
                bool=false;
            } else System.out.println("\t 올바른 형식이 아닙니다.. 다시 입력해 주세요.");
            //if-else end: 맞는 형식 인지 확인
        }//큰 while end
    }//lookupCashPast end
}//클래스 end
