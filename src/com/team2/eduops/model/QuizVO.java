package com.team2.eduops.model;

public class QuizVO {
	// 바뀌지 않을 테이블명 상수값 지정
	public final String ClassName = "QUIZ";

	// Student 테이블의 컬럼명 필드로 선언
	private int std_no, quiz_no;
	private String quiz_text;

	// 테이블명은 상수값이라 바뀌지 않으므로 setter 없이 getter만 생성
	public String getClassName() {
		return ClassName;
	}


	// 필드들 getter/setter 생성
	public int getStd_no() {
		return std_no;
	}
	
	public void setStd_no(int std_no) {
		this.std_no = std_no;
	}
	
	public int getQuiz_no() {
		return quiz_no;
	}
	
	public void setQuiz_no(int quiz_no) {
		this.quiz_no = quiz_no;
	}
	
	public String getQuiz_text() {
		return quiz_text;
	}
	
	public void setQuiz_text(String quiz_text) {
		this.quiz_text = quiz_text;
	}
	

}
