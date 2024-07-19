package com.team2.eduops.controller;

public class PageController {

// main 메소드 안에서 종료하기 전까지 계속 돌아갈 메소드
	public void runPage() {
		int userNo;
		LoginController lc = new LoginController();
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
					runStudentPage(userNo);
				}
				break;
			case 3:
				JoinController.studentJoin();
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
//		showStudentPage();

	}

	public void runAdminPage(int userNo) { // 관리자번호
//		showAdminPage();
	}

	public void showEntrancePage() {
		// 로그인회원가입 페이지 띄우기
		System.out.println("===로그인/회원가입 페이지===");
		System.out.println("1. 학생 로그인");
		System.out.println("2. 관리자 로그인");
		System.out.println("3. 학생 회원가입");
		System.out.println("0. 프로그램 종료");
	}

}
