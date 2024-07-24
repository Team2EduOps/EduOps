package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.team2.eduops.model.AlgorithmVO;


public class Algorithm_Controller {
	
//	public static void main(String[] args) {
//        ConnectController.connect();
//        menu();
//    }
	public static class ReturnToMainMenuException extends RuntimeException {}

	
	public static void menulist() {
		System.out.println("\n -=-=-=-=-= 알고리즘 문제 제출 =-=-=-=-=-");
        System.out.println("\t 1. 알고리즘 번호 및 코드 제출");  // 알고리즘 번호 및 코드 기입, 제출자 번호
//        System.out.println("\t 2. 알고리즘 제출 수정");  // 알고리즘 코드 , 알고리즘 번호, 학생번호 수정, where 로 알고리즘 번호 와 학생 번호로 설정
        System.out.println("\t 3. 제출 목록");
        System.out.println("\t 4. 저장 (commit)");
        System.out.println("\t 5. 프로그램 종료");
        System.out.println("\t 0. 메인 메뉴로 돌아가기 ");
        System.out.println("\t >> 원하는 메뉴 선택 하세요.    ");
	}
	
	public static void menu() {
		AlgorithmVO vo = new AlgorithmVO();
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
	
	// insert              // 알고리즘 번호 및 코드 기입
    public static void insert(String ClassName) {
        String sql = "INSERT INTO " + ClassName + " ( AL_TEXT, STD_NO, AL_NO ) VALUES (?,?,?)";

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
    
 // update
    public static void update(String ClassName) {
        while (true) {

            System.out.println("0 선택 ==> 업데이트 탈출합니다.");
            System.out.print("수정할 STD_NO 입력: ");
            
            int stdNo = ConnectController.scanIntData();
            
            
            if (stdNo == 0) {
                break;
            }
            System.out.print("수정할 AL_NO 입력: ");
            int alNo = ConnectController.scanIntData();
            
            System.out.println("수정할 필드를 선택하세요.");
            System.out.println("1. 알고리즘 코드");
            System.out.println("2. 제출자 번호");
            System.out.println("3. 알고리즘 번호");
            System.out.print("수정할 필드 선택: ");
            int choice = ConnectController.scanIntData();

            String sql = null;
            switch (choice) {
                case 1:
                    sql = "UPDATE " + ClassName + " SET AL_TEXT = ? WHERE STD_NO = ? AND AL_NO = ?";
                    System.out.print("새 알고리즘 코드 입력: ");
                    String newText = ConnectController.scanData();
                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setString(1, newText);
                        pstmt.setInt(2, stdNo);
                        pstmt.setInt(3, alNo);
                        int result = ConnectController.executePstmtUpdate(pstmt);
                        System.out.println(result + "개의 레코드가 수정되었습니다.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    sql = "UPDATE " + ClassName + " SET STD_NO = ? WHERE STD_NO = ? AND AL_NO = ?";
                    System.out.print("새 제출자 번호 입력: ");
                    String newstdno = ConnectController.scanData();
                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setString(1, newstdno);
                        pstmt.setInt(2, stdNo);
                        pstmt.setInt(3, alNo);
                        int result = ConnectController.executePstmtUpdate(pstmt);
                        System.out.println(result + "개의 레코드가 수정되었습니다.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    sql = "UPDATE " + ClassName + " SET AL_NO = ? WHERE STD_NO = ? AND AL_NO = ? ";
                    System.out.print("새 알고리즘 번호 입력: ");
                    int newAlno = ConnectController.scanIntData();
                    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
                        pstmt.setInt(1, newAlno);
                        pstmt.setInt(2, stdNo);
                        pstmt.setInt(3, alNo);
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
    
    public static void line() {
    	System.out.println("");
    }
    
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

