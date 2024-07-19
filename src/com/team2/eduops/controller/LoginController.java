package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.StudentVO;

// 로그인 기능 관련 메소드들
public class LoginController {

	public int userLogin(int menuNo) {
		int userNo = -1;
		String id;
		String pw;

		String userType = "";
		String className = "";
		String sqlType = "";

		if (menuNo == 1) {
			StudentVO userVo = new StudentVO();
			className = userVo.getClassName();
			userType = userVo.getUserType();
			sqlType = userVo.getSqlType();

		} else {
			AdminVO userVo = new AdminVO();
			className = userVo.getClassName();
			className = userVo.getClassName();
			userType = userVo.getUserType();
			sqlType = userVo.getSqlType();
		}

		String sqlNo = sqlType + "_no";
		String sqlId = sqlType + "_id";
		String sqlPw = sqlType + "_pw";

		showUserLoginPage(userType);

		while (userNo == -1) {
			System.out.println("아이디를 입력하세요: ");
			id = ConnectController.scanData();
			System.out.println("비밀번호를 입력하세요: ");
			pw = ConnectController.scanData();

			String sql = "Select " + sqlNo + ", " + sqlPw + " from " + className + " where " + sqlId + " = ?";

			PreparedStatement pstmt = ConnectController.getPstmt(sql);
			if (ConnectController.isNull(pstmt)) {
				System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
				continue;
			}

			try {
				pstmt.setString(1, id);
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("문제발생");
				e.getMessage();
				continue;
			}

			ResultSet rs = ConnectController.executePstmtQuery(pstmt);

			if (ConnectController.isNull(rs)) {
				System.out.println("문제발생");
				continue;
			}

			try {
				rs.next();
				if (pw.equals(rs.getString(sqlPw))) {
					userNo = rs.getInt(sqlNo);
				}
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("문제발생");
				e.getMessage();
				continue;
			}
		}
		return userNo;

	}

	public boolean checkLogin(int userNo) {
		if (userNo == -1) {
			System.out.println("오류가 발생했습니다. 다시 로그인해주세요.");
			return false;
		}
		return true;
	}

	public void showUserLoginPage(String userType) {
		System.out.println("===" + userType + " 로그인 페이지===");
		System.out.println("안녕하세요 :)");
	}

	public void showStudentLoginPage() {
		System.out.println("===학생 로그인 페이지===");
		System.out.println("안녕하세요 :)");
	}

	public void showAdminLoginPage() {
		System.out.println("===관리자 로그인 페이지===");
		System.out.println("안녕하세요 :)");
	}
}