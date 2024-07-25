package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/** displayNotice()에 있는 출력부와 modifyNotice 상단이 출력부의 내용이 조금 다름니다.
 *  나중에 리팩토링 할 때 비슷한 부분을 합치는 쪽으로 손을 조금 보면 좋을 것 같습니다.
 */
public class NoticeController {

	public void displayNotice() {
		System.out.println("          공지");
		System.out.println("========================");
		String sql = "SELECT * from NOTICE where POSTED_DATE > TRUNC(SYSDATE) - 7"; 
		PreparedStatement pstmt = ConnectController.getPstmt(sql);
		ResultSet rs = ConnectController.executePstmtQuery(pstmt);
		try {
			while (rs.next()) {
				Date posted_date = rs.getDate("posted_date");
				String contents = rs.getString("contents");
				System.out.println(posted_date + " : " + contents);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addNotice() {
		try {
			System.out.println("추가할 공지를 입력해주세요");
			String new_notice = ConnectController.scanData();
			if(new_notice.isEmpty()) {
				return;
			}
			String sql = "INSERT INTO NOTICE(CONTENTS) VALUES(?)";
			PreparedStatement pstmt = ConnectController.getPstmt(sql);
			pstmt.setString(1, new_notice);
			int success = ConnectController.executePstmtUpdate(pstmt);
			ConnectController.commit();
			if(success != -1) {
				System.out.println("공지가 추가되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void modifyNotice() {
		System.out.println("         공지");
		System.out.println("========================");
		String sql = "SELECT * from NOTICE where POSTED_DATE > TRUNC(SYSDATE) - 7";
		PreparedStatement pstmt = ConnectController.getPstmt(sql);
		ResultSet rs = ConnectController.executePstmtQuery(pstmt);
		try {
			while (rs.next()) {
				int post_no = rs.getInt("post_no");
				Date posted_date = rs.getDate("posted_date");
				String contents = rs.getString("contents");
				System.out.println(post_no + " - " + posted_date + " : " + contents);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true) {
			System.out.println("수정할 공지의 번호를 입력해주세요");
			int postNo_to_modify = ConnectController.scanIntData();
			rs = ConnectController.executePstmtQuery(pstmt);
			if(!checkIfElementExists(rs, "post_no", postNo_to_modify)) {
				System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
			}
			else {
				System.out.println("수정할 공지의 내용을 입력해주세요");
				String new_contents = ConnectController.scanData();
				if(new_contents.isEmpty()) {
					return;
				}
				sql = "UPDATE NOTICE SET CONTENTS = '" + new_contents + "' WHERE POST_NO = " + postNo_to_modify;
				pstmt = ConnectController.getPstmt(sql);
				ConnectController.executePstmtUpdate(pstmt);
				ConnectController.commit();
				System.out.println("공지가 수정되었습니다.");
				break;
			}			
		}
		
	}
	
	private boolean checkIfElementExists(ResultSet rs, String column, int element) {
		try {
			while(rs.next()) {
				int rsElement = rs.getInt(column);
				System.out.println(rsElement);
				if(rsElement == element) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
}