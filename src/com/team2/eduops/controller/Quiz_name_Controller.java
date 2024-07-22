package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.team2.eduops.controller.Algorithm_name_Controller.ReturnToMainMenuException;
import com.team2.eduops.model.QuizNameVO;

public class Quiz_name_Controller {   //퀴즈 문제 선정 ( 관리자 )
	
	public static class ReturnToMainMenuException extends RuntimeException {}
    
    
  
    
    public static void menulist() {
    	System.out.println("\n -=-=-=-=-= 퀴즈 문제 선정 =-=-=-=-=-");
        System.out.println("\t 1. 퀴즈 문제 선정");  // 퀴즈 이름, 담당자(관리자)
//        System.out.println("\t 2. 퀴즈 문제 수정");  // 퀴즈 번호 및 코드 기입, 제출자 번호
        System.out.println("\t 3. 전체보기");
        System.out.println("\t 4. 저장 (commit)");
        System.out.println("\t 5. 프로그램 종료");
        System.out.println("\t 0. 메인 메뉴로 돌아가기 ");
        System.out.println("\t >> 원하는 메뉴 선택 하세요.    ");
    }
    
    // 퀴즈 문제 등록 (퀴즈 이름, 알고리즘 주소)
    public static void menu() {
        QuizNameVO vo = new QuizNameVO();
        menulist();

        while (true) {
            int menuNo = ConnectController.scanIntData();

            switch (menuNo) {
                case 1:
                    selectAll(vo.getClassName());
                    line();
                    insert(vo.getClassName());
                    line();
                    selectAll(vo.getClassName());
                    menulist();
                    break;                
//                case 2:
//                	selectAll(vo.getClassName());
//                	line();
//                	update(vo.getClassName());
//                	menulist();
//                	break;
                case 3:
                    selectAll(vo.getClassName());
                    line();
                    menulist();
                    break;
                case 4:
                    if (ConnectController.commit() > 0) {
                        System.out.println("성공적으로 완료 되었습니다.");
                    }
                    break;
                case 5:
                    ConnectController.close();
                    System.out.println("프로그램 종료합니다. ! ! ! ");
                    System.exit(0);
                    break;
                case 0:
                    throw new ReturnToMainMenuException();  // 메인 메뉴로 돌아가기 위해 예외 던짐
                default:
                    System.out.println("없는 번호 선택하였습니다. 1~4번 중에서 선택하세요.");
            } //end switch
        } //end while
    } //end menu

    // insert              // 퀴즈 이름, 담당자(관리자)
    public static void insert(String ClassName) {
        String sql = "INSERT INTO " + ClassName + " (QUIZ_NAME, ADM_NO) VALUES (?,?)";

        try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
            System.out.println("퀴즈 이름, 담당자 번호 :");
            String str1 = ConnectController.scanData();
            String str2 = ConnectController.scanData();
            pstmt.setString(1, str1);
            pstmt.setString(2, str2);
            int result = ConnectController.executePstmtUpdate(pstmt);
            System.out.println(result + "개 입력완료");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // update
    public static void update(String ClassName) {
        while (true) {

            System.out.println("0 선택 ==> 업데이트 탈출합니다.");
            System.out.print("수정할 Quiz_NO 입력: ");
            int quizNo = ConnectController.scanIntData();

            if (quizNo == 0) {
                break;
            }

            System.out.println("수정할 필드를 선택하세요.");
            System.out.println("1. 퀴즈 이름");
            System.out.println("2. 담당자 번호");
            System.out.print("수정할 필드 선택: ");
            int choice = ConnectController.scanIntData();

            String sql = null;
            switch (choice) {
                case 1:
                    sql = "UPDATE " + ClassName + " SET QUIZ_NAME = ? WHERE QUIZ_NO = ?";
                    System.out.print("새 퀴즈 이름 입력: ");
                    String newquizname = ConnectController.scanData();
                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setString(1, newquizname);
                        pstmt.setInt(2, quizNo);
                        int result = ConnectController.executePstmtUpdate(pstmt);
                        System.out.println(result + "개의 레코드가 수정되었습니다.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    sql = "UPDATE " + ClassName + " SET ADM_NO = ? WHERE QUIZ_NO = ?";
                    System.out.print("새 담당자 번호 입력: ");
                    String newadmno = ConnectController.scanData();
                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setString(1, newadmno);
                        pstmt.setInt(2, quizNo);
                        int result = ConnectController.executePstmtUpdate(pstmt);
                        System.out.println(result + "개의 레코드가 수정되었습니다.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
                    break;
            }
        }
    }

    
    // select all
    public static void selectAll(String ClassName) {
        String sql = "SELECT * FROM " + ClassName;

        try (PreparedStatement pstmt = ConnectController.getPstmt(sql);
             ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();  // 컬럼 (필드) 개수

            // 각 열의 너비를 고정합니다.
            int col1Width = 15;
            int col2Width = 20;
            int col3Width = 20;
            int col4Width = 30;

            System.out.printf("%-" + col1Width + "s%-" + col2Width + "s%-" + col3Width + "s%-" + col4Width + "s\n",
                    "퀴즈 번호", "퀴즈 이름", "담당자 번호", "날짜");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // 데이터 레코드를 출력합니다.
            while (rs.next()) {
                int quizno = rs.getInt("QUIZ_NO");
                String quizname = rs.getString("QUIZ_NAME");
                int admno = rs.getInt("ADM_NO");
                java.sql.Date sqlDate = rs.getDate("QUIZ_DATE");
                String date = dateFormat.format(sqlDate); // 포맷된 날짜 문자열

                // 긴 텍스트 자르기
                quizname = truncateString(quizname, col2Width - 3);
                date = truncateString(date, col4Width - 3);

                // 데이터 출력
                System.out.printf("%-" + col1Width + "d%" + col2Width + "s%" + col3Width + "d%" + col4Width + "s\n",
                        quizno, quizname, admno, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 문자열을 지정된 최대 길이로 자릅니다.
    private static String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        } else {
            return str.substring(0, maxLength) + "...";
        }
    }
    
    public static void line() {
    	System.out.println("");
    }
}
