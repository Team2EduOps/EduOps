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
}
