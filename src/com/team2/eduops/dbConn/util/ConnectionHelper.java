package com.team2.eduops.dbConn.util;

import java.sql.Connection;
import java.sql.DriverManager;


//	singleton 방식으로 db 연결 메소드 작성함
public class ConnectionHelper {
	private static Connection conn;
	private ConnectionHelper() { }
	
	public static Connection getConnection(String dsn) {
		if(conn != null) {
			return conn;
		}
		
		try {
			if(dsn.equalsIgnoreCase("oracle")) {
				Class.forName("oracle.jdbc.OracleDriver");
				conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "oracle");
						
			} else if(dsn.equalsIgnoreCase("mysql")) {
				Class.forName("oracle.jdbc.OracleDriver");
				conn = DriverManager.getConnection("jdbc:mysql://192.168.0.17:3306/SampleDB", "test", "oracle");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			return conn;
		}
	}
	
	
}
