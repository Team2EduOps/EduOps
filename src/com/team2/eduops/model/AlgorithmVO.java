package com.team2.eduops.model;

public class AlgorithmVO {
	public final String ClassName = "ALGORITHM";

	// Student 테이블의 컬럼명 필드로 선언
	private int std_no;
	private String team_name, al_name, al_url, al_text, al_date;

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

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public String getAl_name() {
		return al_name;
	}

	public void setAl_name(String al_name) {
		this.al_name = al_name;
	}

	public String getAl_url() {
		return al_url;
	}

	public void setAl_url(String al_url) {
		this.al_url = al_url;
	}

	public String getAl_text() {
		return al_text;
	}

	public void setAl_text(String al_text) {
		this.al_text = al_text;
	}

	public String getAl_date() {
		return al_date;
	}

	public void setAl_date(String al_date) {
		this.al_date = al_date;
	}

}
