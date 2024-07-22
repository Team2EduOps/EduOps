package com.team2.eduops.controller;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.StudentVO;

public class PageController {
	UserController uc = new UserController();

// main 메소드 안에서 종료하기 전까지 계속 돌아갈 메소드
	public void runPage() {
		int userNo;
		LoginController lc = new LoginController();
		JoinController jc = new JoinController();
//		MessageController ms = new MessageController();

		while (true) {
			userNo = -1;
//			ms.showEntrancePage();
			showEntrancePage();

			int menuNo = ConnectController.scanIntData();
			switch (menuNo) {
			case 1:
				userNo = lc.userLogin(menuNo);
				if (lc.checkLogin(userNo)) {
					runStudentPage(userNo);
				}
				break;
			case 2:
				userNo = lc.userLogin(menuNo);
				if (lc.checkLogin(userNo)) {
					runAdminPage(userNo);
				}
				break;
			case 3:
				jc.studentJoin();
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				ConnectController.close();
				System.exit(0);
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}

	}

	public void runStudentPage(int userNo) { // 학생번호
		StudentVO stdVo;

		// 학생 번호로 학생 정보 받아오기
		stdVo = uc.getStdData(userNo);

		boolean isStdPageRun = true;
		while (isStdPageRun) {
			showStudentPage(stdVo.getStd_name());

			int menuNo = ConnectController.scanIntData();
			switch (menuNo) {
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;
			case 0:
				isStdPageRun = false;
				break;
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}

	}

	public void runAdminPage(int userNo) { // 관리자번호
		AdminVO admVo;
		JoinController jc = new JoinController();

		// 관리자 번호로 관리자 정보 받아오기
		admVo = uc.getAdmData(userNo);

		boolean isAdmPageRun = true;
		while (isAdmPageRun) {
			showAdminPage(admVo.getAdm_name());

			int menuNo = ConnectController.scanIntData();
			switch (menuNo) {
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:
				jc.adminJoin();
				isAdmPageRun = false;
				break;
			case 0:
				isAdmPageRun = false;
				break;
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}
	}

	public void showEntrancePage() {
		// 로그인회원가입 페이지 띄우기
		System.out.println("===로그인/회원가입 페이지===");
		System.out.println("1. 학생 로그인");
		System.out.println("2. 관리자 로그인");
		System.out.println("3. 학생 회원가입");
		System.out.println("0. 프로그램 종료");
	}

	public void showStudentPage(String std_name) {
		// 로그인회원가입 페이지 띄우기
		System.out.println("===학생 홈 페이지===");
		System.out.println("안녕하세요 " + std_name + "님");
		System.out.println("1. 입실");
		System.out.println("2. 공지 보기");
		System.out.println("3. 퀴즈 제출");
		System.out.println("4. 알고리즘 관리");
		System.out.println("5. 근태 관리");
		System.out.println("0. 로그아웃");

	}

	public void showAdminPage(String adm_name) {
		// 로그인회원가입 페이지 띄우기
		System.out.println("===관리자 홈 페이지===");
		System.out.println("안녕하세요 " + adm_name + "님");
		System.out.println("1. 출결 관리");
		System.out.println("2. 공지");
		System.out.println("3. 퀴즈");
		System.out.println("4. 숙제");
		System.out.println("5. 관리자 추가");
		System.out.println("6. 학생 관리");
		System.out.println("0. 로그아웃");
	}

}
