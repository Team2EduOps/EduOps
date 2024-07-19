package com.team2.eduops.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import com.team2.eduops.dbConn.util.*;

// db 연결 기능 관련 메소드들
public class ConnectController {
	// DB 연결, 삽입, 삭제, 수정, 검색...
	static Scanner sc = new Scanner(System.in);
	static Connection conn = null; // 문자열, 객체는 기본 초기값 null
	static PreparedStatement pstmt;
	static ResultSet rs;

	
	// connect
	public static void connect() {
		try {
			conn = ConnectionHelper.getConnection("oracle");
			conn.setAutoCommit(false); // 자동 커밋 끄기
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// close
	public static void close() {
		try {
			CloseHelper.close(rs);
			CloseHelper.close(pstmt);
			CloseHelper.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// rollback
	// rollback 메소드 실행 성공 시 1, 실패 시 -1 값 리턴
	public static int rollback() {
		int result = -1;
		try {
			conn.rollback();
			result = 1;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// commmit
	// commit 메소드 실행 성공 시 1, 실패 시 -1 값 리턴
	public static int commit() {
		int result = -1;
		try {
			conn.commit();
			result = 1;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	////////////////////////////////
	
	
	// Scanner 객체 사용해서 String 값 받기 메소드
	// isNull(String str) 메소드로 null값 체크 후 사용
	public static String scanData() {
		String str = null;
		try {
			str = sc.nextLine();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return str;
		}
	}

	// data를 String 값으로 scan하여 parseInt 시도 -> 성공 시 입력된 정수값 리턴, 실패 시 음수값인 -1 값 리턴
	// isNegative(int num) 메소드로 음수값 리턴 받았는지 확인 후 사용
	public static int scanIntData() {
		int i = -1;
		try {
			i = Integer.parseInt(sc.nextLine());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return i;
		}
	}

	////////////////////////////////
	
	// 준비한 쿼리문 String값 매개변수로 받아서 null값으로 비워둔 pstmt 안에 넣은 후 return
	// 받아서 쓸 때 isNull(pstmt) 우선 실행하여 true값 받으면 문제 상황 발생
	public static PreparedStatement getPstmt(String sql) {
		pstmt = null;
		try {
		pstmt = conn.prepareStatement(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	
	// 쿼리문 단순실행 -> 성공 시 성공 결과값 정수 리턴, 실패 시 -1 리턴
	public static int executePstmtUpdate(PreparedStatement pstmt) {
		int result = -1;
		try {
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result; 
	}

	// 쿼리문 실행 후 db에서 값 받아옴 -> 성공 시 ResultSet rs 안에 값 담아서 return, 실패 시 null값 리턴
	public static ResultSet executePstmtQuery(PreparedStatement pstmt) {
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return rs; 
	}
	
	///////////////////////////////////
	
	public static boolean isNegative(int num) {
		if(num < 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isNull(String str) {
		if(str == null) {
			return true;
		}
		return false;
	}
	
	public static boolean isNull(PreparedStatement pstmt) {
		if(pstmt == null) {
			return true;
		}
		return false;
	}
	
	public static boolean isNull(ResultSet rs) {
		if(rs == null) {
			return true;
		}
		return false;
	}
}