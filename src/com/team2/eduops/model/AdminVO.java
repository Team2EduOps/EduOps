package com.team2.eduops.model;

public class AdminVO {
	// 바뀌지 않을 테이블명 상수값 지정
	public final String ClassName = "ADMIN";

	// Student 테이블의 컬럼명 필드로 선언
	private int adm_no;
	private String adm_id, adm_pw, adm_name;

	// 테이블명은 상수값이라 바뀌지 않으므로 setter 없이 getter만 생성
	public String getClassName() {
		return ClassName;
	}

	// 필드들 getter/setter 생성
	public int getAdm_no() {
		return adm_no;
	}

	public void setAdm_no(int adm_no) {
		this.adm_no = adm_no;
	}

	public String getAdm_id() {
		return adm_id;
	}

	public void setAdm_id(String adm_id) {
		this.adm_id = adm_id;
	}

	public String getAdm_pw() {
		return adm_pw;
	}

	public void setAdm_pw(String adm_pw) {
		this.adm_pw = adm_pw;
	}

	public String getAdm_name() {
		return adm_name;
	}

	public void setAdm_name(String adm_name) {
		this.adm_name = adm_name;
	}
}
