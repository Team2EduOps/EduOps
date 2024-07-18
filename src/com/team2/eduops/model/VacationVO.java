package com.team2.eduops.model;

public class VacationVO {
	// 바뀌지 않을 테이블명 상수값 지정
	public final String ClassName = "VACATION";

	// Student 테이블의 컬럼명 필드로 선언
	private int vacate_code;
	private String vacate_date, vacate_file;
	private int std_no, adm_no;

	// 테이블명은 상수값이라 바뀌지 않으므로 setter 없이 getter만 생성
	public String getClassName() {
		return ClassName;
	}

	// 필드들 getter/setter 생성
	public int getVacate_code() {
		return vacate_code;
	}

	public void setVacate_code(int vacate_code) {
		this.vacate_code = vacate_code;
	}

	public String getVacate_date() {
		return vacate_date;
	}

	public void setVacate_date(String vacate_date) {
		this.vacate_date = vacate_date;
	}

	public int getStd_no() {
		return std_no;
	}

	public void setStd_no(int std_no) {
		this.std_no = std_no;
	}

	public int getAdm_no() {
		return adm_no;
	}

	public void setAdm_no(int adm_no) {
		this.adm_no = adm_no;
	}

	public String getVacate_file() {
		return vacate_file;
	}

	public void setVacate_file(String vacate_file) {
		this.vacate_file = vacate_file;
	}

}
