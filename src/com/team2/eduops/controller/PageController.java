package com.team2.eduops.controller;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.QuizVO;
import com.team2.eduops.model.StudentVO;

public class PageController {
	MessageController mc = new MessageController();
	UserController uc = new UserController();
	NoticeController nc = new NoticeController();
	LoginController lc = new LoginController();
	JoinController jc = new JoinController();
	QuizController qc = new QuizController();
	AlgorithmController ac = new AlgorithmController();

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
			mc.showEntrancePage();

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
			mc.showStudentPage(stdVo.getStd_name());

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
				runStudentAlgorithmPage(stdVo);
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
			mc.showAdminPage(admVo.getAdm_name());

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
				runAdminAlgorithmPage(admVo);
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
		while (isNoticePageRun) {
			mc.showNoticePage();

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
			mc.showStudentQuizPage();
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
			case 2: //문제보기
				qc.selectQuizAll();
				break;
			case 3: //코드보기
				qc.selectQuizAnswer(stdVo);
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
			mc.showAdminQuizPage();
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

	public void runStudentAlgorithmPage(StudentVO stdVo) {
		boolean isRunStudentAlgorithmPage = true;
		while (isRunStudentAlgorithmPage) {
			mc.showStudentAlgorithmPage();
			int menuNo = ConnectController.scanIntData();

			switch (menuNo) {
			case 1:
				// 알고리즘 등록
				ac.addAlgorithmName(stdVo);
				break;
			case 2:
				// 알고리즘 제출
				ac.addAlgorithmAnswer(stdVo);
				break;
			case 3:
				// 문제보기
				ac.selectAlgorithmAll();
				break;
			case 4:
				// 코드보기
				ac.selectAlgorithmAnswer(stdVo);
				break;
			case 0:
				// exception
				isRunStudentAlgorithmPage = false;
				break;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~2번 중에서 선택하세요.");
			} // end switch
		} // end while
	}

	public void runAdminAlgorithmPage(AdminVO admVo) {
		boolean isRunAdminAlgorithmPage = true;
		while (isRunAdminAlgorithmPage) {
			mc.showAdminAlgorithmPage();
			int menuNo = ConnectController.scanIntData();

			switch (menuNo) {
			case 1:
				// 주차별 보기
				ac.displayAlgorithmByDate();
				break;
			case 2:
				// 팀별 보기
				ac.displayAlgorithmByTeam();
				break;
			case 0:
				// exception
				isRunAdminAlgorithmPage = false;
				break;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~2번 중에서 선택하세요.");
			} // end switch
		} // end while
	}

}
