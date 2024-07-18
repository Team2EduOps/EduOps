package com.team2.eduops.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

import com.team2.eduops.dbConn.util.*;


// db 연결 기능 관련 메소드들
public class ConnectController {
	// DB 연결, 삽입, 삭제, 수정, 검색...
		static Scanner sc = new Scanner(System.in);
		static Connection conn = null; // 문자열, 객체는 기본 초기값 null
		static PreparedStatement pstmt;
		static Statement stmt;
		static ResultSet rs;

		// connect
		public static void connect() {
			try {
				conn = ConnectionHelper.getConnection("oracle");
				stmt = conn.createStatement();
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
				CloseHelper.close(stmt);
				CloseHelper.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static String scanData() {
			String str = "";
			try {
				str = sc.next();
				
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				return str;			
			}
		}
		
		// data를 String 값으로 scan하여 parseInt 시도 -> 성공 시 입력된 정수값 리턴, 실패 시 음수값인 -1 값 리턴
		// 코드 짜실 때 리턴값이 -1인지 체크하여 코드 작성
		public static int scanIntData() {
			int i = -1;
			try {
				i = Integer.parseInt(sc.next());
			} catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				return i;
			}
		}
}
