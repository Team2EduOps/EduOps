package com.team2.eduops.controller;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import java.util.Scanner;

import com.team2.eduops.model.AttendanceVO;
import com.team2.eduops.controller.ConnectController;


//학생 출결 관리 관련 메소드들
public class AttendStudentController {

    //근태 관리 메뉴 보이기
     public static void showAttendMenu() throws SQLException, ClassNotFoundException{
            System.out.println("\n------5.근태관리-------");
            System.out.println("근태 관리 페이지입니다.");
            System.out.println("\t 5-1. 일자별");
            System.out.println("\t 5-2. 월별");
            System.out.println("\t 5-3. 누적 지원금 조회");
            System.out.println("\t 5-4. 휴가 신청");
            System.out.println("\t >> 원하는 메뉴 선택 하세요.   ");
     } //showAttendMenu end

    public static void showCashMenu() throws SQLException,ClassNotFoundException{
        System.out.println("\n------5-3.누적 지원금 조회-------");
        System.out.println("누적 지원금 조회페이지입니다.");
        System.out.println("\t 1. 현재 누적 지원금");
        System.out.println("\t 2. 지난 지원금: 월별");
    }
    //근태 관리 메뉴 선택에 따른 동작

     public static void attendMenu() throws SQLException, ClassNotFoundException{
         boolean backpage=true;
         while(backpage){
             showAttendMenu();  // 메뉴 보이기
             //메뉴 고르기
             switch(ConnectController.scanIntData()){
                 case -1 : System.out.println("잘못된 입력값입니다. 다시 입력하여주세요.");
                            break;
                 case 0: System.out.println("뒤 페이지로 이동합니다.");
                            backpage=false;
                            break;
                 case 1 : System.out.println("일자별 근태 조회 페이지입니다.");
                        lookupDaily();
                        break;

                 case 2 : System.out.println("월별 근태 조회 페이지입니다.");
                        lookupMonthly();
                        break;

                 case 3 : System.out.println("누적 지원금 조회 페이지입니다.");
                        boolean backpage2=true;
                        while(backpage2){
                            showCashMenu();
                            switch(ConnectController.scanIntData()){
                                case -1 : System.out.println("잘못된 입력값입니다. 다시 입력하여주세요.");
                                            break;
                                case 0 : System.out.println("뒤 페이지로 이동합니다.");
                                        backpage2=false;
                                        break;
                                case 1 : System.out.print("현재 누적 지원금:");
                                        lookupCashPresent();
                                        break;
                                case 2 : System.out.println("월별 지원금 조회 페이지입니다.");
                                        lookupCashPast();
                                        break;
                                default : System.out.println("메뉴에 없는 번호를 선택하였습니다. 1~2번 중에서 선택하세요.");
                                        break;
                            }
                        }//case 3 while end
                        break;

                 case 4: System.out.println("휴가신청 페이지입니다.");
                        applyVacation();
                        break;

                 default : System.out.println("메뉴에 없는 번호를 선택하셨습니다. 1~4번 중에서 선택하세요.");
                        break;
             } //switch end
         } // while end
     }// atttendMenu end

    //날짜 형식 정의 method
    private static boolean isValidDate(String dateString){
         if(dateString == null || dateString.isEmpty()){
             return false;
         }
         dateFormat.setLeniend(false);
         try{
             dateFormat.parse(dateString);
             return true;
         } catch(ParseException e){
             return false;
         }
    }
    //lookupDaily-일자별 조회(근태 page)
     public static void lookupDaily(int stdno){
         ConnectController co =new ConnectController();
        private static final String DATE_FORMAT = "yyyy-MM-dd";
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        rs;
         String inputDate=" ";

        while(true){
            System.out.println("OOOO-OO-OO으로 입력해주세요.");
             inputDate= co.scanData();

            if (isValidDate(inputDate)) {
                java.util.Date parsedDate = dateFormat.parse(inputDate);
                Date formattedDate = new Date(parsedDate.getTime());
                pstmt = conn.prepareStatement("SELECT ATTEND_STATUS FROM ATTENDANCE WHERE TO_CHAR(ATTEND_DATE,'YYYY-MM-DD') = ? and STD_NO = ?");

                pstmt.setString(1,formattedDate);
                pstmt.setInt(2,stdno);
                rs= pstmt.executeQuery(); // atted_status

                break;

            } else{
                System.out.println("올바른 형식이 아닙니다.. 다시 입력해 주세요.");
            }//if end
        }//while end
         switch(rs){
             case 0:
                 System.out.println(inputDate+"날 결석하였습니다.");
             case 1:
                 System.out.println(inputDate+"날 출석하였습니다.");
             case 2:
                 System.out.println(inputDate+"날 공가(출석)하였습니다.");
             case 3:
                 System.out.println(inputDate+"날 지각하였습니다.");
             case 4:
                 System.out.println(inputDate+"날 조퇴하였습니다.");
         }
     }



     //lookupMonthly-월별 조회(근태 page)

     public static void lookupMonthly(int stdno){
     /*
     lookupMonthly 속 날짜 선택
     -> 출력 해당 날짜의 근태 상태, 출석 시간, 퇴근 시간
      */
         ConnectController co = new ConnectController();
         while(true){
             System.out.print("O월 입력: ");
             int inputMonth= co.scanIntData();
             pstmt = conn.prepareStatement("SELECT ATTEND_STATUS FROM ATTENDANCE WHERE TO_CHAR(ATTEND_DATE,'YYYY-MM-DD') = ? and STD_NO = ?");

             break;
         } else{
             System.out.println("올바른 형식이 아닙니다. 다시 입력해 주세요.");
         }//if end
     }



    //applyVacation- 휴가 신청(근태 page)

     public static void applyVacation(int stdno){

     }

     //lookupCashPresent - 누적 지원금 조회(근태 page)
     public static void lookupCashPresent(int stdno){

     }

     //lookupCashPast- 지난 누적 지원금 조회 월별(근태 page)
     public static void lookupCashPast(int stdno){

     }
}
