package com.team2.eduops.model;

public class NoticeVO {
	// 바뀌지 않을 테이블명 상수값 지정
	public final String ClassName = "NOTICE";

	// Student 테이블의 컬럼명 필드로 선언
	private String posted_date, contents;
	private int post_no;

	// 테이블명은 상수값이라 바뀌지 않으므로 setter 없이 getter만 생성
	public String getClassName() {
		return ClassName;
	}
	
	// 필드들 getter/setter 생성
	// Posted_date은 자동으로 생성됨으로 setter 없이 getter만 생성
	public String getPosted_date() {
		return posted_date;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	// Post_no은 자동으로 생성됨으로 setter 없이 getter만 생성
	public int getPost_no() {
		return post_no;
	}
}
