package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UtilController {
	
	////// 매개변수 체크하여 boolean 값 리턴 메소드들 ////////
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
	//////// 매개변수 체크하여 boolean 값 리턴 메소드들 ////////


	// 문자열을 지정된 최대 길이로 자릅니다.
	public static String truncateString(String str, int maxLength) {
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
