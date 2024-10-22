package com.team2.eduops.model;

public class AttendanceVO {
	// 바뀌지 않을 테이블명 상수값 지정
	public final String ClassName = "ATTENDANCE";

	// Student 테이블의 컬럼명 필드로 선언
	private int attend_code, attend_status, std_no;
	private String attend_date, ci_time, co_time;
	

	// 테이블명은 상수값이라 바뀌지 않으므로 setter 없이 getter만 생성
	public String getClassName() {
		return ClassName;
	}


	// 필드들 getter/setter 생성
	public int getAttend_code() {
		return attend_code;
	}
	
	
	public void setAttend_code(int attend_code) {
		this.attend_code = attend_code;
	}
	
	
	public int getAttend_status() {
		return attend_status;
	}
	
	
	public void setAttend_status(int attend_status) {
		this.attend_status = attend_status;
	}
	
	
	public int getStd_no() {
		return std_no;
	}
	
	
	public void setStd_no(int std_no) {
		this.std_no = std_no;
	}
	
	
	public String getAttend_date() {
		return attend_date;
	}
	
	
	public void setAttend_date(String attend_date) {
		this.attend_date = attend_date;
	}
	
	
	public String getCi_time() {
		return ci_time;
	}
	
	
	public void setCi_time(String ci_time) {
		this.ci_time = ci_time;
	}
	
	
	public String getCo_time() {
		return co_time;
	}
	
	
	public void setCo_time(String co_time) {
		this.co_time = co_time;
	}
	
	
}
