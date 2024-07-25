package com.team2.eduops.controller;

import java.sql.*;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.team2.eduops.model.StudentVO;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
/* *********학생메뉴_1.입실 1.퇴실 메뉴 ************ */

public class CheckInOut {
	static LocalDate localDate;
	static Date sqlDate;
	// localdate(현재 날짜) 를 java.sql.Date로 변환
	static LocalDateTime localDateTime;
	static Timestamp timestamp;
	// LocalDateTime을 java.sql.Timestamp로 변환

	static PreparedStatement pstmt;
	static ResultSet rs;

	// ********입실 명단을 list에 추가 +DB에 삽입********
	public static void updateIO(StudentVO stdVo) {
		localDate = LocalDate.now();
		sqlDate = Date.valueOf(localDate);
		localDateTime = LocalDateTime.now();
		timestamp = Timestamp.valueOf(localDateTime);

		System.out.println("\t 입실이 완료되었습니다.: " + localDateTime);
		String sql = "INSERT INTO ATTENDANCE (std_no,attend_date, ci_time) VALUES (?,?,?)";
		pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, stdVo.getStd_no());
			pstmt.setDate(2, sqlDate);
			pstmt.setTimestamp(3, timestamp);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		ConnectController.executePstmtUpdate(pstmt);
		if (ConnectController.commit() == -1) {
			System.out.println("커밋 오류");
		}
	}

	// *********퇴실시 명단에서 삭제+DB에 update(CO_time과 Attend_status)**********
	public static void deleteIO(StudentVO stdVo) {
		localDate = LocalDate.now();
		sqlDate = Date.valueOf(localDate);
		localDateTime = LocalDateTime.now();
		timestamp = Timestamp.valueOf(localDateTime);
		LocalTime cIt = null, cOt = null, entryTime = null, exitTime = null;

		System.out.println("\t 퇴실이 완료되었습니다.: " + localDateTime);
		String sql = "UPDATE ATTENDANCE set CO_TIME = ? where STD_NO = ? and ATTEND_DATE = ?";
		pstmt = ConnectController.getPstmt(sql);
		
		try {
			pstmt.setTimestamp(1, timestamp);
			pstmt.setInt(2, stdVo.getStd_no());
			pstmt.setDate(3, sqlDate);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		ConnectController.executePstmtUpdate(pstmt);
		if (ConnectController.commit() == -1) {
			System.out.println("커밋 오류");
		}

		// *******0:결석(default), 1: 출석, 2: 공가, 3: 지각 ,4: 조퇴**********
		sql = "SELECT CI_TIME,CO_TIME from ATTENDANCE where std_no = ? AND ATTEND_DATE = ?";
		pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, stdVo.getStd_no());
			pstmt.setDate(2, sqlDate);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		rs = ConnectController.executePstmtQuery(pstmt);
		
		if(UtilController.isNull(rs)) {
			System.out.println("문제 발생");
		}
		
		try {
			if (rs.next()) {
				cIt = rs.getTimestamp("CI_TIME").toLocalDateTime().toLocalTime();
				cOt = rs.getTimestamp("CO_TIME").toLocalDateTime().toLocalTime();
				// 비교시간 :9시와 18시
				entryTime = LocalTime.of(9, 0);
				exitTime = LocalTime.of(18, 0);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		// attendStatus update
		int attendStatus = 0; // 결석:0 // 공가:1
		if (cIt != null) {
			if (cIt.isBefore(entryTime) && cOt.isAfter(exitTime)) {
				attendStatus = 1; // 지각:3
			} else if (cIt.isAfter(entryTime) && cOt.isAfter(exitTime)) {
				attendStatus = 3; // 조퇴:4
			} else {
				attendStatus = 4;
			}
		} else {
			System.out.println("입실을 하지 않았습니다. 입실 먼저해주세요.");
		}

		sql = "UPDATE ATTENDANCE set ATTEND_STATUS = ? where STD_NO = ? and ATTEND_DATE = ?";
		pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, attendStatus);
			pstmt.setInt(2, stdVo.getStd_no());
			pstmt.setDate(3, sqlDate);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		ConnectController.executePstmtUpdate(pstmt);
		if (ConnectController.commit() == -1) {
			System.out.println("커밋 오류");
		}
	}



	// ********명단에 있는지 확인하여, 입/퇴실 메뉴 보이기 :********
	public int showcheckIO(StudentVO stdVo) {
		int checkIo = -1;

		while (checkIo == -1) {
			localDate = LocalDate.now();
			sqlDate = Date.valueOf(localDate);
			System.out.println(localDate);

			// 튜플 가져오기 : 값이 없는 경우 _결석과 입실 "입실"띄우기
			String sql = "SELECT * from ATTENDANCE where std_no = ? AND ATTEND_DATE = ?";
			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setInt(1, stdVo.getStd_no());
				pstmt.setDate(2, sqlDate);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}

			rs = ConnectController.executePstmtQuery(pstmt);
            try {
				if (UtilController.isNull(rs)) {
					continue;
				}
				if (!rs.isBeforeFirst()) { // 값이 없으면, 입실+결석(0) 모두 "입실" 띄워야함
					checkIo = 0;
				}

            } catch (SQLException e) {
				checkIo = -1;
				System.out.println("showcheckIO_rs 오류");
                e.printStackTrace();
				continue;
            }
			// attend_status 가져오기 : 업로드 되는 경우_1(출석),2(공가),3(지각),4(조퇴)
			sql = "SELECT ATTEND_STATUS from ATTENDANCE where std_no = ? AND ATTEND_DATE = ?";
			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setInt(1, stdVo.getStd_no());
				pstmt.setDate(2, sqlDate);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}
			try {
				if (UtilController.isNull(rs)) {
					continue;
				}
				if (!rs.isBeforeFirst()) { // 튜플인 있으면서 attend_status 값이 없으면, "퇴실"출력:5
					checkIo = 1;
				}else{
					checkIo=rs.getInt("ATTEND_STATUS"); //1(출석),2(공가),3(지각),4(조퇴) 반환
				}

			} catch (SQLException e) {
				checkIo = -1;
				System.out.println("showcheckIO_rs 오류");
				e.printStackTrace();
				continue;
			}
		}
        return checkIo;
    } //**********showchkeckIO*********************

	// ********최종:입실 또는 퇴실 *****************
	public void checkIO(StudentVO stdVo) {

		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
		String formattedDate = localDate.format(formatter);
		int checkIo = showcheckIO(stdVo);
		switch (checkIo) {
			case 0:
				updateIO(stdVo);
				break;
			case 1:
				deleteIO(stdVo);
				break;
			case 2: System.out.println(formattedDate+ ": 공가");
				break;
			case 3: System.out.println(formattedDate+": 지각");
				break;
			case 4: System.out.println(formattedDate+": 조퇴");
				break;
			case 5:
				deleteIO(stdVo);
				break;
			default:
				break;
		}// switch end
	}//checkIO end
}