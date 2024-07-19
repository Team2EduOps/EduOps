package com.team2.eduops.model;

public class StudentVO {

	// 바뀌지 않을 테이블명 상수값 지정
	public final String ClassName = "STUDENT";
	public final String UserType = "학생";
	public final String SqlType = "std";

	// Student 테이블의 컬럼명 필드로 선언
	private int std_no;
	private String std_id, std_pw, std_name, team_name;
	private int seat_no;

	// 테이블명은 상수값이라 바뀌지 않으므로 setter 없이 getter만 생성
	public String getClassName() {
		return ClassName;
	}
	
	public String getUserType() {
		return UserType;
	}
	
	public String getSqlType() {
		return SqlType;
	}
	
	
	

	// 필드들 getter/setter 생성
	public int getStd_no() {
		return std_no;
	}

	public void setStd_no(int std_no) {
		this.std_no = std_no;
	}

	public String getStd_id() {
		return std_id;
	}

	public void setStd_id(String std_id) {
		this.std_id = std_id;
	}

	public String getStd_pw() {
		return std_pw;
	}

	public void setStd_pw(String std_pw) {
		this.std_pw = std_pw;
	}

	public String getStd_name() {
		return std_name;
	}

	public void setStd_name(String std_name) {
		this.std_name = std_name;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public int getSeat_no() {
		return seat_no;
	}

	public void setSeat_no(int seat_no) {
		this.seat_no = seat_no;
	}
}
