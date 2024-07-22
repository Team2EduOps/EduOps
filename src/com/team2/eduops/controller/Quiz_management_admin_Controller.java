package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Quiz_management_admin_Controller {
    public static void main(String[] args) {
        ConnectController.connect();
        
        while (true) {
        	try {
            System.out.println("=-=-=-=-=-  퀴즈 보기 -=-=-=-=-=");
            System.out.println("\t 1. 퀴즈 추가 ");
            System.out.println("\t 2. 퀴즈 날짜별 보기 ");
            System.out.println("\t 3. 퀴즈 팀별 보기 ");
            System.out.println("\t 4. 종료 ");
            
            // 메뉴 번호 입력받기
            int menuNo = ConnectController.scanIntData();
            String sql = null;
            switch (menuNo) {
            
            	case 1:
            		Quiz_name_Controller.menu();  // 알고리즘 문제 선정
    				break;
                case 2:
                    // 날짜별 보기 처리
                    System.out.println("조회할 날짜를 입력하세요 (형식: yyyy/MM/dd): ");
                    String selectDate = ConnectController.scanData();
                    
                    // 날짜 포맷 확인 및 변환
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    java.sql.Date sqlDate;
                    try {
                        java.util.Date utilDate = inputDateFormat.parse(selectDate);
                        sqlDate = new java.sql.Date(utilDate.getTime());
                    } catch (ParseException e) {
                        System.out.println("날짜 형식이 잘못되었습니다. 형식: yyyy/MM/dd");
                        continue;
                    }

                    sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, B.quiz_date " +
                          "FROM Quiz A " +
                          "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no " +
                          "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                          "WHERE TRUNC(B.quiz_date) = ?";

                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setDate(1, sqlDate);

                        try (ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int count = rsmd.getColumnCount();  // 컬럼 수

                            // 열 너비 정의 (예상 데이터 크기에 따라 조정)
                            int col1Width = 20; // std_name 용
                            int col2Width = 20; // quiz_name 용
                            int col3Width = 35; // quiz_text 용
                            int col4Width = 20; // date 용
                            
                            // 헤더 출력
                            System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                    "학생이름", "퀴즈 이름", "퀴즈 내용", "날짜");

                            // 날짜 포맷 정의
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            // 결과 집합의 각 행 출력
                            while (rs.next()) {
                                String stdName = rs.getString("STD_NAME");
                                String quizName = rs.getString("QUIZ_NAME");
                                String quizText = rs.getString("QUIZ_TEXT");
                                java.sql.Date quizDate = rs.getDate("QUIZ_DATE");
                                String date = dateFormat.format(quizDate); // 포맷된 날짜 문자열

                                // 긴 문자열 자르기
                                quizText = truncateString(quizText, col3Width - 3);

                                // 데이터 행 출력
                                System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                        stdName, quizName, quizText, date);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    // 팀별 보기 처리
                    System.out.println("조회할 팀 이름을 입력하세요: ");
                    String teamName = ConnectController.scanData();

                    sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, C.team_name " +
                          "FROM Quiz A " +
                          "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no " +
                          "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                          "WHERE C.team_name = ?";

                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setString(1, teamName);

                        try (ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int count = rsmd.getColumnCount();  // 컬럼 수

                            // 열 너비 정의 (예상 데이터 크기에 따라 조정)
                            int col1Width = 20; // std_name 용
                            int col2Width = 20; // quiz_name 용
                            int col3Width = 35; // quiz_text 용
                            int col4Width = 20; // team_name 용
                            
                            // 헤더 출력
                            System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                    "학생이름", "퀴즈 이름", "퀴즈 내용", "팀이름");

                            // 결과 집합의 각 행 출력
                            while (rs.next()) {
                                String stdName = rs.getString("STD_NAME");
                                String quizName = rs.getString("QUIZ_NAME");
                                String quizText = rs.getString("QUIZ_TEXT");
                                String team = rs.getString("TEAM_NAME");

                                // 긴 문자열 자르기
                                quizText = truncateString(quizText, col3Width - 3);

                                // 데이터 행 출력
                                System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                        stdName, quizName, quizText, team);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.out.println("프로그램 종료합니다. ! ! ! ");
                    System.exit(0);
            } //switch end
        }catch(Quiz_name_Controller.ReturnToMainMenuException e){
        	//메인 메뉴로 돌아가기
			System.out.println("메인 메뉴로 돌아갑니다. ");
        }
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
}
