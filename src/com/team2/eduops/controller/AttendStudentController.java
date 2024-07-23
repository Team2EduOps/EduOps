package com.team2.eduops.controller;

import java.sql.*;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

//date형식으로 받아오기 위한 library

import com.team2.eduops.dbConn.util.*;

//학생 출결 관리 관련 메소드들
public class AttendStudentController {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //근태 관리 메뉴 동작
   public static void attendMenu(int stdno) throws ClassNotFoundException, SQLException, ParseException {
        boolean backpage = true;
        while (backpage) {
            showAttendMenu();  // 메뉴 보이기
            //메뉴 고르기
            switch (ConnectController.scanIntData()) {
                case -1:
                    System.out.println("잘못된 입력값입니다. 다시 입력하여주세요.");
                    break;
                case 0:
                    System.out.println("뒤 페이지로 이동합니다.");
                    backpage = false;
                    break;
                case 1:
                    System.out.println("일자별 근태 조회 페이지입니다.");
                    lookupDaily(stdno);
                    break;

                case 2:
                    System.out.println("월별 근태 조회 페이지입니다.");
                    lookupMonthly(stdno);
                    break;

                case 3:
                    System.out.println("누적 지원금 조회 페이지입니다.");
                    boolean backpage2 = true;
                    while (backpage2) {
                        showCashMenu();
                        switch (ConnectController.scanIntData()) {
                            case -1:
                                System.out.println("잘못된 입력값입니다. 다시 입력하여주세요.");
                                break;
                            case 0:
                                System.out.println("뒤 페이지로 이동합니다.");
                                backpage2 = false;
                                break;
                            case 1:
                                System.out.print("현재 누적 지원금:");
                                lookupCashPresent(stdno);
                                break;
                            case 2:
                                System.out.println("월별 지원금 조회 페이지입니다.");
                                lookupCashPast(stdno);
                                break;
                            default:
                                System.out.println("메뉴에 없는 번호를 선택하였습니다. 1~2번 중에서 선택하세요.");
                                break;
                        }
                    }//case 3 while end
                    break;

                case 4:
                    System.out.println("휴가신청 페이지입니다.");
                    applyVacation(stdno);
                    break;

                default:
                    System.out.println("메뉴에 없는 번호를 선택하셨습니다. 1~4번 중에서 선택하세요.");
                    break;
            } //switch end
        } // while end
    }// atttendMenu end

    //5.근태 관리 메뉴 :showAttendMenu
    public static void showAttendMenu() {
        System.out.println("\n------5.근태관리-------");
        System.out.println("근태 관리 페이지입니다.");
        System.out.println("\t 5-1. 일자별");
        System.out.println("\t 5-2. 월별");
        System.out.println("\t 5-3. 누적 지원금 조회");
        System.out.println("\t 5-4. 휴가 신청");
        System.out.println("\t 뒤 페이지 이동: 0");
        System.out.println("\t >> 원하는 메뉴 선택 하세요.   ");
    } //end

    //5-3.누적 지원금 메뉴 : showCashMenu
     public static void showCashMenu(){
        System.out.println("\n------5-3.누적 지원금 조회-------");
        System.out.println("누적 지원금 조회페이지입니다.");
        System.out.println("\t 1. 현재 누적 지원금");
        System.out.println("\t 2. 지난 지원금: 월별");
         System.out.println("\t 뒤 페이지 이동: 0");
         System.out.println("\t >> 원하는 메뉴 선택 하세요.   ");
    }//end

    //날짜 형식 맞는지 확인 메소드
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

    //5-1. lookupDaily-일자별 조회(근태 page)
    public static void lookupDaily(int stdno) throws ParseException, SQLException {
        String inputDate;
        boolean bool=true;
        while (bool) {
            System.out.print("\t OOOO-OO-OO 형식에 맞춰 입력해주세요:");
            inputDate = ConnectController.scanData();

            if (isValidDate(inputDate)) { //날짜형식에 맞다면!
                String sql=("SELECT ATTEND_STATUS FROM ATTENDANCE WHERE TO_CHAR(ATTEND_DATE,'YYYY-MM-DD') = '" + inputDate + "' AND STD_NO = " + stdno);
                PreparedStatement pstmt = ConnectController.getPstmt(sql);
                ResultSet rs = ConnectController.executePstmtQuery(pstmt);

                //case1- DB에 없는 값, 튜플 X
                if (!rs.isBeforeFirst()) {
                    System.out.println("\t 해당 날짜에 데이터가 없습니다.");
                } else {
                    while (rs.next()) {
                        Object columnValue = rs.getObject("Attend_status");

                        // case2-오늘 퇴실 처리 X ,튜플 O, 상태 X
                        if (columnValue == null) {
                            System.out.println("\t 오늘은 퇴실 처리를 하지 않아 조회가 불가합니다.");
                            bool=false;
                        }

                        // case3- DB에 튜플 O, 상태 O
                        int result = rs.getInt("Attend_status");
                        switch (result) {
                            case 0: System.out.println("\t "+inputDate + "날 결석하였습니다."); break;
                            case 1: System.out.println("\t "+inputDate + "날 출석하였습니다."); break;
                            case 2: System.out.println("\t "+inputDate+ "날 공가(출석)하였습니다."); break;
                            case 3: System.out.println("\t "+inputDate + "날 지각하였습니다."); break;
                            case 4: System.out.println("\t "+inputDate + "날 조퇴하였습니다."); break;
                            default: break;
                        }
                        bool=false;
                    }
                }

            }else {
                System.out.println("\t 올바른 형식이 아닙니다.. 다시 입력해 주세요.");
            }//if end
            }
        }//while end

    //년 월 형식 맞는지 확인 메소드
    public static boolean isValidYearMonth(String date){
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM");
        final String DATE_PATTERN = "yyyy-MM";
        try{
            Date parseDate = DATE_FORMAT.parse(date);
            return DATE_FORMAT.format(parseDate).equals(date);
        }catch(ParseException e){
            return false;
        }
    }

    //5-2. 월별 조회(근태 page):lookupMonthly
     public static void lookupMonthly(int stdno) throws SQLException {
        boolean bool=true;
         while(bool){
             System.out.print("\t YYYY-MM 형식 입력: ");
             String inputMonth= ConnectController.scanData();

             if(isValidYearMonth(inputMonth)){ //맞는 형식으로 입력!
                 String firstdate=inputMonth+"-01";
                 String lastdate=inputMonth+"-31";
                 String sql = "SELECT TO_CHAR(ATTEND_DATE,'YYYY-MM-DD') ,ATTEND_STATUS " +
                         "FROM ATTENDANCE WHERE ATTEND_DATE BETWEEN TO_DATE('" +firstdate+"','YYYY-MM-DD') AND TO_DATE('"+lastdate+"','YYYY-MM-DD') "+
                         "AND STD_NO = "+stdno;

                 PreparedStatement pstmt = ConnectController.getPstmt(sql);
                 ResultSet rs = ConnectController.executePstmtQuery(pstmt);
                 ResultSetMetaData metaData =rs.getMetaData();
                 //case1- DB에 없는 값, 튜플 X
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
                 }else System.out.println("\t 올바른 형식이 아닙니다.. 다시 입력해 주세요.");
             }
         }
    //applyVacation- 휴가 신청(근태 page)

    public static void applyVacation (int stdno){

    }

    //lookupCashPresent - 누적 지원금 조회(근태 page)
    public static void lookupCashPresent (int stdno){

    }

    //lookupCashPast- 지난 누적 지원금 조회 월별(근태 page)
    public static void lookupCashPast (int stdno){

    }

    public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException {
     attendMenu(1);
    }
}




