package com.team2.eduops.model;

public class AlgorithmVO {
	public final String ClassName = "ALGORITHM";

	// Student 테이블의 컬럼명 필드로 선언
	private int std_no, al_no;
	private String al_text;

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
	
	public int getAl_no() {
		return al_no;
	}
	
	public void setAl_no(int al_no) {
		this.al_no = al_no;
	}
	
	public String getAl_text() {
		return al_text;
	}
	
	public void setAl_text(String al_text) {
		this.al_text = al_text;
	}
	

}
