package com.team2.eduops.controller;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.QuizVO;
import com.team2.eduops.model.StudentVO;

public class PageController {
	UserController uc = new UserController();
	NoticeController nc = new NoticeController();
	LoginController lc = new LoginController();
	JoinController jc = new JoinController();
	QuizController qc = new QuizController();

	StudentVO stdVo;
	AdminVO admVo;
	
	////// 프로그램 돌리는 메소드들 ////////
	
// main 메소드 안에서 종료하기 전까지 계속 돌아갈 메소드
	public void runPage() {
		int userNo;
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
				nc.displayNotice();
				break;
			case 3:
				runStudentQuizPage(stdVo);
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
				runNoticePage(admVo);
				break;
			case 3:
				runAdminQuizPage(admVo);
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
	
	public void runNoticePage(AdminVO admVo) {

		boolean isNoticePageRun = true;
		while(isNoticePageRun) {
			showNoticePage();
			
			int menuNo = ConnectController.scanIntData();
			switch (menuNo) {
			case 1:
				nc.addNotice();
				break;
			case 2:
				nc.displayNotice();
				break;
			case 3:
				nc.modifyNotice();
				break;
			case 0:
				isNoticePageRun = false;
				break;
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}
	}

	public void runStudentQuizPage(StudentVO stdVo) {
		QuizVO vo = new QuizVO();

		boolean isRunStudentQuizPage = true;
		while (isRunStudentQuizPage) {
			showStudentQuizPage();
			int menuNo = ConnectController.scanIntData();

			switch (menuNo) {
			case 1:
				qc.addQuizAnswer(stdVo);
				break;
//	                case 2:
//	                	selectAll(vo.getClassName());
//	                	line();
//	                	update(vo.getClassName());
//	                	menulist();
//	                	break;
			case 3:
				qc.selectQuizAnswerAll();
				break;
			case 0:
				isRunStudentQuizPage = false;
				// exception
				break;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~3번 중에서 선택하세요.");
			} // end switch
		} // end while
	}
	
	public void runAdminQuizPage(AdminVO admVo) {

		boolean isRunAdminQuizPage = true;
		while (isRunAdminQuizPage) {
			showAdminQuizPage();
			int menuNo = ConnectController.scanIntData();

			switch (menuNo) {
			case 1:
				// 퀴즈 추가
				qc.addQuiz(admVo);
				break;
			case 2:
				// 주차별 보기
				qc.displayQuizByDate();
				break;
			case 3:
				// 팀별 보기
				qc.displayQuizByTeam();
				break;
			case 0:
				// exception
				isRunAdminQuizPage = false;
				break;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~3번 중에서 선택하세요.");
			} // end switch
		} // end while
	}
	
	
	
	//////// CLI 화면 띄우기 메소드들 /////////
	public void showEntrancePage() {
		// 로그인회원가입 페이지 띄우기
		System.out.println("===로그인/회원가입 페이지===");
		System.out.println("1. 학생 로그인");
		System.out.println("2. 관리자 로그인");
		System.out.println("3. 학생 회원가입");
		System.out.println("0. 프로그램 종료");
	}

	public void showStudentPage(String std_name) {
		// 학생 홈 페이지 띄우기
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
		// 관리자 홈 페이지 띄우기
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
	
	public void showNoticePage() {
		System.out.println("=== 2-1. 공지 관리 ===");
		System.out.println("공지 관리 페이지입니다.");
		System.out.println("1. 공지 추가");
		System.out.println("2. 공지 보기");
		System.out.println("3. 공지 수정");
		System.out.println("0. 뒤로 가기");
	}
	
	public void showStudentQuizPage() {
		System.out.println("\n -=-=-=-=-= 퀴즈 코드 제출 =-=-=-=-=-");
		System.out.println("\t 1. 퀴즈 번호 및 코드 제출"); // 퀴즈 코드, 퀴즈 번호 담당자(관리자)
//	        System.out.println("\t 2. 퀴즈 제출 수정");  // 퀴즈 코드 , 퀴즈 번호, 학생번호 수정, where 퀴즈번호, 학생번호 설정
		System.out.println("\t 3. 전체보기");
		System.out.println("\t 0. 뒤로 가기");
		System.out.println("\t >> 원하는 메뉴 선택 하세요.  ");
	}
	
	public void showAdminQuizPage() {
		System.out.println("=-=-=-=-=-  퀴즈 보기 -=-=-=-=-=");
        System.out.println("\t 1. 퀴즈 추가 ");
        System.out.println("\t 2. 퀴즈 날짜별 보기 ");
        System.out.println("\t 3. 퀴즈 팀별 보기 ");
        System.out.println("\t 0. 뒤로 가기");
	}
}
