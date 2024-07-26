package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.StudentVO;

// 로그인 기능 관련 메소드들
public class LoginController {

	public int userLogin(int menuNo, MessageController mc) {
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
			userType = userVo.getUserType();
			sqlType = userVo.getSqlType();
		}

		String sqlNo = sqlType + "_no";
		String sqlId = sqlType + "_id";
		String sqlPw = sqlType + "_pw";

		mc.showUserLoginPage(userType);

		while (userNo == -1) {
			System.out.println("아이디를 입력하세요: ");
			id = ConnectController.scanData();
			System.out.println("비밀번호를 입력하세요: ");
			pw = ConnectController.scanData();

			String sql = "Select " + sqlNo + ", " + sqlPw + " from " + className + " where " + sqlId + " = ?";

			PreparedStatement pstmt = ConnectController.getPstmt(sql);
			if (UtilController.isNull(pstmt)) {
				System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
				continue;
			}

			try {
				pstmt.setString(1, id);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("문제 발생");
				continue;
			}

			ResultSet rs = ConnectController.executePstmtQuery(pstmt);

			if (UtilController.isNull(rs)) {
				System.out.println("문제 발생");
				continue;
			}

			// rs.next()가 에러나면 id가 잘못 입력된 것
			
			// pw랑 sqlPw랑 다르면 pw가 잘못 입력된 것
			try {
				rs.next();
				if (pw.equals(rs.getString(sqlPw))) {
					userNo = rs.getInt(sqlNo);
					System.out.println("로그인 성공!");
				} else {
					System.out.println("ID / PW가 틀렸습니다.");
					System.out.println("다시 입력해주세요.");
				}
			} catch (Exception e) {
				System.out.println("ID / PW가 틀렸습니다.");
				System.out.println("다시 입력해주세요.");
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
}
