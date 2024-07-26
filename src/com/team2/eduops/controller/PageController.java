package com.team2.eduops.controller;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.StudentVO;

public class PageController {
	MessageController mc = new MessageController();
	UserController uc = new UserController();
	NoticeController nc = new NoticeController();
	LoginController lc = new LoginController();
	JoinController jc = new JoinController();
	QuizController qc = new QuizController();
	AlgorithmController ac = new AlgorithmController();
	CheckInOut cio = new CheckInOut();
	AttendStudentController asc = new AttendStudentController();
	AttendProfessorContoller apc = new AttendProfessorContoller();

	StudentVO stdVo;
	AdminVO admVo;

	////// 프로그램 돌리는 메소드들 ////////

// main 메소드 안에서 종료하기 전까지 계속 돌아갈 메소드
	public void runPage() {
		int userNo;

		while (true) {
			userNo = -1;
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

		while (true) {
			mc.showStudentPage(stdVo, cio.showcheckIO(stdVo));

			int menuNo = ConnectController.scanIntData();
			switch (menuNo) {
			case 1:
				// 입실
				cio.checkIO(stdVo); 
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
				// 근태 관리
				runStudentAttendPage(stdVo);
				break;
			case 6:
				// 유저 정보 변경
				runStdUserPage(stdVo);
			case 0:
				return;
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}

	}

	public void runAdminPage(int userNo) { // 관리자번호

		// 관리자 번호로 관리자 정보 받아오기
		admVo = uc.getAdmData(userNo);

		while (true) {
			mc.showAdminPage(admVo.getAdm_name());

			int menuNo = ConnectController.scanIntData();
			switch (menuNo) {
			case 1:
				// 출결 관리
				runAdminAttendancePage();
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
				return;
			case 6:
				runAdminStudentPage();
				break;
			case 7:
				runAdmUserPage(admVo);
			case 0:
				return;
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}
	}

	public void runNoticePage(AdminVO admVo) {

		while (true) {
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
				return;
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}
	}

	public void runStudentQuizPage(StudentVO stdVo) {
		while (true) {
			mc.showStudentQuizPage();
			int menuNo = ConnectController.scanIntData();

			switch (menuNo) {
			case 1:
				qc.insertQuizAnswer(stdVo);
				break;
			case 2:
				qc.selectQuizAll();
				break;
			case 3:
				qc.selectQuizAnswer(stdVo);
				break;
			case 0:
				return;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~3번 중에서 선택하세요.");
			} // end switch
		} // end while
	}

	public void runAdminQuizPage(AdminVO admVo) {
		while (true) {
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
				return;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~3번 중에서 선택하세요.");
			} // end switch
		} // end while
	}

	public void runStudentAlgorithmPage(StudentVO stdVo) {
		while (true) {
			mc.showStudentAlgorithmPage();
			int menuNo = ConnectController.scanIntData();

			switch (menuNo) {
			case 1:
				// 알고리즘 등록
				ac.addAlgorithmName(stdVo);
				break;
			case 2:
				// 알고리즘 제출
				ac.insertAlgorithmAnswer(stdVo);
				break;
			case 3:
				// 전체보기
				ac.selectAlgorithmAll();
				break;
			case 4:
				ac.selectAlgorithmAnswer(stdVo);
			case 0:
				return;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~4번 중에서 선택하세요.");
			} // end switch
		} // end while
	}

	public void runAdminAlgorithmPage(AdminVO admVo) {
		while (true) {
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
				return;
			default:
				System.out.println("없는 번호 선택하였습니다. 1~2번 중에서 선택하세요.");
			} // end switch
		} // end while
	}

	
	
	
	
	public void runStudentAttendPage(StudentVO stdVo){
		boolean backpage = true;
		while (backpage) {
			mc.showStudentAttendPage(); // 메뉴 보이기
			// 메뉴 고르기
			switch (ConnectController.scanIntData()) {
			case -1:
				System.out.println("잘못된 입력값입니다. 다시 입력하여주세요.");
				break;
			case 0:
				System.out.println("뒤 페이지로 이동합니다.");
				backpage = false;
				break;
			case 1:
				//일자별 근태 조회
				System.out.println("일자별 근태 조회 페이지입니다.");
				asc.lookupDaily(stdVo);
				break;

			case 2:
				System.out.println("월별 근태 조회 페이지입니다.");
				asc.lookupMonthly(stdVo);
				break;

			case 3:
				System.out.println("누적 지원금 조회 페이지입니다.");
				runCashMenuPage(stdVo);
				break;

			case 4:
				System.out.println("휴가신청 페이지입니다.");
				asc.applyVacation(stdVo);
				break;

			default:
				System.out.println("메뉴에 없는 번호를 선택하셨습니다. 1~4번 중에서 선택하세요.");
				break;
			} // switch end
		} // while end
	}// atttendMenu end

	public void runCashMenuPage(StudentVO stdVo) {
		boolean backpage2 = true;
		while (backpage2) {
			mc.showStudentAttendCashPage();
			switch (ConnectController.scanIntData()) {
			case -1:
				System.out.println("잘못된 입력값입니다. 다시 입력하여주세요.");
				break;
			case 0:
				System.out.println("뒤 페이지로 이동합니다.");
				backpage2 = false;
				break;
			case 1:
				System.out.print("현재 누적 지원금:");
				asc.lookupCashPresent(stdVo);
				break;
			case 2:
				System.out.println("월별 지원금 조회 페이지입니다.");
				asc.lookupCashPast(stdVo);
				break;
			default:
				System.out.println("메뉴에 없는 번호를 선택하였습니다. 1~2번 중에서 선택하세요.");
				break;
			}
		}
	}

	public void runAdminAttendancePage() {
		boolean backpage = true;
		while (backpage) {
			mc.showAdminAttenancePage();
			switch (ConnectController.scanIntData()) {
				case -1:
					System.out.println("/t 잘못된 입력값입니다. 다시 입력하여주세요.");
					break;
				case 0:
					System.out.println("/t 뒤 페이지로 이동합니다.");
					backpage = false;
					break;
				case 1:
					System.out.println("\t 1-1. 출결 보기");
					System.out.println("\t 출결 보기 페이지입니다.");
					//함수 넣기
					break;
				case 2:
					System.out.println("\t 1-2. 출결 변경");
					System.out.println("\t 출결 변경 페이지입니다.");
					//함수 넣기
				default:
					System.out.println("메뉴에 없는 번호를 선택하였습니다. 1~2번 중에서 선택하세요.");
					break;
			}
		}
	}


	public void runAdminStudentPage(){
		boolean backpage = true;
		while (backpage) {
			mc.showAdminStudentPage();
			switch (ConnectController.scanIntData()) {
				case -1:
					System.out.println("/t 잘못된 입력값입니다. 다시 입력하여주세요.");
					break;
				case 0:
					System.out.println("/t 뒤 페이지로 이동합니다.");
					backpage = false;
					break;
				case 1:
					System.out.println("\t 6-1. 학생 보기");
					System.out.println("\t 학생 보기 페이지입니다.");
					apc.lookupStudent();
					break;
				case 2:
					System.out.println("\t 6-2. 휴가 승인");
					System.out.println("\t 휴가 승인 페이지입니다.");
					int vacationCode =apc.lookupVacation();
					if(apc.selectVacation(vacationCode)){
						apc.updateVacation(vacationCode);
					}

					break;
				default:
					System.out.println("메뉴에 없는 번호를 선택하였습니다. 1~2번 중에서 선택하세요.");
					break;
			}
		}
	}
	
	
	
	public void runStdUserPage(StudentVO stdVo) {
		while (true) {
			mc.showStdUserPage();
			switch (ConnectController.scanIntData()) {
			case -1:
				System.out.println("잘못된 입력값입니다. 다시 입력해 주세요.");
				break;
			case 1:
				uc.updateStdPw(stdVo);
				break;
			case 2:
				uc.updateTeam(stdVo);
				break;
			case 3:
				uc.updateSeatNo(stdVo);
			case 0:
				return;
			default:
				System.out.println("메뉴에 없는 번호를 선택하였습니다. 1~3번 중에서 선택하세요.");
			}
		}
	}

	public void runAdmUserPage(AdminVO admVo) {
		while (true) {
			mc.showAdmUserPage();
			switch (ConnectController.scanIntData()) {
			case -1:
				System.out.println("잘못된 입력값입니다. 다시 입력해 주세요.");
				break;
			case 1:
				uc.updateAdmPw(admVo);
				break;
			case 0:
				return;
			default:
				System.out.println("잘못 누르셨습니다. 다시 입력해주세요.");
			}
		}
	}
}