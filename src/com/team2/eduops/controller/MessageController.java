package com.team2.eduops.controller;

import com.team2.eduops.model.StudentVO;

public class MessageController {
	//////// CLI 화면 띄우기 메소드들 /////////
	public void showEntrancePage() {
		// 로그인회원가입 페이지 띄우기
		System.out.println("===로그인/회원가입 페이지===");
		System.out.println("1. 학생 로그인");
		System.out.println("2. 관리자 로그인");
		System.out.println("3. 학생 회원가입");
		System.out.println("0. 프로그램 종료");
	}

	public void showStudentPage(StudentVO stdVo, int checkIo) {

		String std_name = stdVo.getStd_name();
		String checkIoStr = "퇴실";
		if (checkIo == 1) {
			checkIoStr = "입실";
		}

		// 학생 홈 페이지 띄우기
		System.out.println("===학생 홈 페이지===");
		System.out.println("안녕하세요 " + std_name + "님");
		System.out.println("1. " + checkIoStr);
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

	public void showStudentAlgorithmPage() {
		System.out.println("\n -=-=-=-=-= 알고리즘 문제 선정 =-=-=-=-=-");
		System.out.println("\t 1. 알고리즘 문제 선정"); // 알고리즘 주소, 알고리즘 이름, 담당자
		System.out.println("\t 2. 알고리즘 번호 및 코드 제출"); // 알고리즘 번호 및 코드 기입, 제출자 번호
//        System.out.println("\t 2. 알고리즘 문제 수정");  // 알고리즘 주소, 알고리즘 이름, 담당자 (al_no 로 수정)
		System.out.println("\t 3. 전체보기");
		System.out.println("\t 0. 뒤로가기 ");
		System.out.println("\t >> 원하는 메뉴 선택 하세요.    ");
	}

	public void showAdminAlgorithmPage() {
		System.out.println("=-=-=-=-=-  알고리즘 보기 -=-=-=-=-=");
		System.out.println("\t 1. 알고리즘 날짜별 보기 ");
		System.out.println("\t 2. 알고리즘 팀별 보기 ");
		System.out.println("\t 0. 뒤로 가기 ");
		System.out.println("\t >> 원하는 메뉴 선택 하세요.    ");

	}

    public void showStudentAttendPage() {
        System.out.println("\n------5.근태관리-------");
        System.out.println("근태 관리 페이지입니다.");
        System.out.println("\t 5-1. 일자별");
        System.out.println("\t 5-2. 월별");
        System.out.println("\t 5-3. 누적 지원금 조회");
        System.out.println("\t 5-4. 휴가 신청");
        System.out.println("\t 뒤 페이지 이동: 0");
        System.out.println("\t >> 원하는 메뉴 선택 하세요.   ");
    }

	public void showStudentAttendCashPage() {
		System.out.println("\n------5-3.누적 지원금 조회-------");
		System.out.println("누적 지원금 조회페이지입니다.");
		System.out.println("\t 1. 현재 누적 지원금");
		System.out.println("\t 2. 지난 지원금: 월별");
		System.out.println("\t 뒤 페이지 이동: 0");
		System.out.println("\t >> 원하는 메뉴 선택 하세요.   ");
	}

	public void showAdminStudentPage() {
		System.out.println("\n-----6.학생 관리-------");
		System.out.println("\t 1. 학생 보기");
		System.out.println("\t 2. 휴가 승인");
		System.out.println("\t 뒤 페이지 이동: 0");
		System.out.println("\t >> 원하는 메뉴 선택 하세요.   ");
	}
}
