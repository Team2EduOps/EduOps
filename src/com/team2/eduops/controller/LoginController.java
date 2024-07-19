package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.team2.eduops.model.StudentVO;

// 로그인 기능 관련 메소드들
public class LoginController {

	public int studentLogin() {
		int studentNo = -1;
		String id;
		String pw;
		StudentVO stdVo = new StudentVO();

		showStudentLoginPage();

		System.out.println("아이디를 입력하세요: ");
		id = ConnectController.scanData();
		System.out.println("비밀번호를 입력하세요: ");
		pw = ConnectController.scanData();

		String sql = "Select std_no, std_pw from " + stdVo.getClassName() + " where std_id = ?";
		PreparedStatement pstmt = ConnectController.getPstmt(sql);
		if (ConnectController.isNull(pstmt)) {
			System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
			return studentNo;
		}
		try {
			pstmt.setString(1, id);
		} catch (Exception e) {
//			e.printStackTrace();
			e.getMessage();
			return studentNo;
		}

		ResultSet rs = ConnectController.executePstmtQuery(pstmt);

		if (ConnectController.isNull(rs)) {
			return studentNo;
		}

		try {
			rs.next();
			if (pw.equals(rs.getString("std_pw"))) {
				studentNo = rs.getInt("std_no");
			}
		} catch (Exception e) {
//			e.printStackTrace();
			e.getMessage();
		}

		return studentNo;

	}

	
	
	
	
	
	
	
	public int adminLogin() {
		int adminNo = -1;

		return adminNo;

	}

	public boolean checkLogin(int userNo) {
		if (userNo == -1) {
			System.out.println("오류가 발생했습니다. 다시 로그인해주세요.");
			return false;
		}
		return true;
	}

	public void showStudentLoginPage() {
		System.out.println("===학생 로그인 페이지===");
		System.out.println("안녕하세요 :)");
	}
}
