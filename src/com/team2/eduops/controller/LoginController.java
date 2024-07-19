package com.team2.eduops.controller;

// 로그인 기능 관련 메소드들
public class LoginController {

	public int studentLogin() {
		int studentNo = -1;

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
}
