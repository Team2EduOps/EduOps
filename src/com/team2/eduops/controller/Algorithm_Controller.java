package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.team2.eduops.model.AlgorithmVO;


public class Algorithm_Controller {
	
	public static void main(String[] args) {
        ConnectController.connect();
        menu();
    }
	
	
	public static void menu() {
		AlgorithmVO vo = new AlgorithmVO();
        System.out.println("\n -=-=-=-=-= 알고리즘 문제 제출 =-=-=-=-=-");
        System.out.println("\t 1. 알고리즘 번호 및 코드 제출");  // 알고리즘 번호 및 코드 기입, 제출자 번호
        System.out.println("\t 2. 제출 목록");
        System.out.println("\t 3. 저장 (commit)");
        System.out.println("\t 4. 프로그램 종료");
        System.out.println("\t >> 원하는 메뉴 선택 하세요.    ");

        while (true) {
            int menuNo = ConnectController.scanIntData();

            switch (menuNo) {
                case 1:
                    selectAll(vo.getClassName());
                    insert(vo.getClassName());
                    selectAll(vo.getClassName());
                    break;
                case 2:
                    selectAll(vo.getClassName());
                    break;
                case 3:
                    if (ConnectController.commit() > 0) {
                        System.out.println("성공적으로 완료 되었습니다.");
                    }
                    break;
                case 4:
                    ConnectController.close();
                    System.out.println("프로그램 종료합니다. ! ! ! ");
                    System.exit(0);
                    break;
                default:
                    System.out.println("없는 번호 선택하였습니다. 1~4번 중에서 선택하세요.");
            } //end switch
        } //end while
    } //end menu
	
	// insert              // 알고리즘 번호 및 코드 기입
    public static void insert(String ClassName) {
        String sql = "INSERT INTO " + ClassName + " ( AL_TEXT,STD_NO,AL_NO ) VALUES (?,?,?)";

        try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
            System.out.println("알고리즘 코드, 제출자 번호, 알고리즘 번호 ");
            String str1 = ConnectController.scanData();
            String str2 = ConnectController.scanData();
            String str3 = ConnectController.scanData();
            pstmt.setString(1, str1);
            pstmt.setString(2, str2);
            pstmt.setString(3, str3);
            int result = ConnectController.executePstmtUpdate(pstmt);
            System.out.println(result + "개 입력완료");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // select all            //모든 데이터 선택
    public static void selectAll(String ClassName) {
        String sql = "SELECT AL_TEXT, STD_NO, AL_NO FROM " + ClassName;

        try (PreparedStatement pstmt = ConnectController.getPstmt(sql);
             ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();  // 컬럼 수

            // 열 너비 정의 (예상 데이터 크기에 따라 조정)
            int col1Width = 35; // AL_TEXT 용
            int col2Width = 20; // STD_NO 용
            int col3Width = 20; // AL_NO 용

            // 헤더 출력
            System.out.printf("%-" + col1Width + "s%" + col2Width + "s%" + col3Width + "s\n",
                    "알고리즘 코드", "제출자 번호", "알고리즘 번호");

            // 결과 집합의 각 행 출력
            while (rs.next()) {
                String alText = rs.getString("AL_TEXT");
                int stdNo = rs.getInt("STD_NO");
                int alNo = rs.getInt("AL_NO");

                // 긴 문자열 자르기
                alText = truncateString(alText, col1Width - 3);

                // 데이터 행 출력
                System.out.printf("%-" + col1Width + "s%" + col2Width + "d%" + col3Width + "d\n",
                        alText, stdNo, alNo);
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

    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

