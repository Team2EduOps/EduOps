package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Algorithm_management_admin_Controller {
    public static void main(String[] args) {
        ConnectController.connect();
        
        while (true) {
            System.out.println("=-=-=-=-=-  알고리즘 보기 -=-=-=-=-=");
            System.out.println("\t 1. 알고리즘 날짜별 보기 ");
            System.out.println("\t 2. 알고리즘 팀별 보기 ");
            System.out.println("\t 3. 종료 ");
            
            // 메뉴 번호 입력받기
            int menuNo = ConnectController.scanIntData();
            String sql = null;
            switch (menuNo) {
                case 1:
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

                    sql = "SELECT C.std_name, B.al_name, A.al_text, B.al_date " +
                          "FROM Algorithm A " +
                          "INNER JOIN ALGORITHM_NAME B ON A.al_no = B.al_no " +
                          "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                          "WHERE TRUNC(B.al_date) = ?";

                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setDate(1, sqlDate);

                        try (ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int count = rsmd.getColumnCount();  // 컬럼 수

                            // 열 너비 정의 (예상 데이터 크기에 따라 조정)
                            int col1Width = 20; // std_name 용
                            int col2Width = 20; // al_name 용
                            int col3Width = 35; // al_text 용
                            int col4Width = 20; // date 용
                            
                            // 헤더 출력
                            System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                    "학생이름", "알고리즘 이름", "알고리즘 코드", "날짜");

                            // 날짜 포맷 정의
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            // 결과 집합의 각 행 출력
                            while (rs.next()) {
                                String stdName = rs.getString("STD_NAME");
                                String alName = rs.getString("AL_NAME");
                                String alText = rs.getString("AL_TEXT");
                                java.sql.Date alDate = rs.getDate("AL_DATE");
                                String date = dateFormat.format(alDate); // 포맷된 날짜 문자열

                                // 긴 문자열 자르기
                                alText = truncateString(alText, col3Width - 3);

                                // 데이터 행 출력
                                System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                        stdName, alName, alText, date);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    // 팀별 보기 처리
                    System.out.println("조회할 팀 이름을 입력하세요: ");
                    String teamName = ConnectController.scanData();

                    sql = "SELECT C.std_name, B.al_name, A.al_text, C.team_name " +
                          "FROM Algorithm A " +
                          "INNER JOIN ALGORITHM_NAME B ON A.al_no = B.al_no " +
                          "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                          "WHERE C.team_name = ?";

                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setString(1, teamName);

                        try (ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int count = rsmd.getColumnCount();  // 컬럼 수

                            // 열 너비 정의 (예상 데이터 크기에 따라 조정)
                            int col1Width = 20; // std_name 용
                            int col2Width = 20; // al_name 용
                            int col3Width = 35; // al_text 용
                            int col4Width = 20; // team_name 용
                            
                            // 헤더 출력
                            System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                    "학생이름", "알고리즘 이름", "알고리즘 코드", "팀이름");

                            // 결과 집합의 각 행 출력
                            while (rs.next()) {
                                String stdName = rs.getString("STD_NAME");
                                String alName = rs.getString("AL_NAME");
                                String alText = rs.getString("AL_TEXT");
                                String team = rs.getString("TEAM_NAME");

                                // 긴 문자열 자르기
                                alText = truncateString(alText, col3Width - 3);

                                // 데이터 행 출력
                                System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                                        stdName, alName, alText, team);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println("프로그램 종료합니다. ! ! ! ");
                    System.exit(0);
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
