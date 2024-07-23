package com.team2.eduops.controller;

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
