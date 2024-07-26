package com.team2.eduops.controller;

import com.team2.eduops.model.AttendanceVO;
import com.team2.eduops.model.StudentVO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AttendProfessorContoller {
	AttendanceVO attendanceVo = new AttendanceVO();
	// ********출결 보기 ********

	// ********교수-학생관리-학생보기
	public void lookupStudent() {
		System.out.println("학생 보기 페이지입니다.");
		LocalDate today = LocalDate.now();
		Date sqlDate = Date.valueOf(today);

		// select문으로 STD_NAME, SEAT_NO, ATTEND_STATUS, CI_TIME, CO_TIME 가져옴
		// NVL은 null 값일 경우, 기본값 00:00:00으로 설정
		String sql = "SELECT s.STD_NAME, " + "       s.SEAT_NO, " + "       NVL(a.ATTEND_STATUS, 0) AS ATTEND_STATUS, "
				+ "       NVL(a.CI_TIME, TO_TIMESTAMP('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')) AS CI_TIME, "
				+ "       NVL(a.CO_TIME, TO_TIMESTAMP('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')) AS CO_TIME "
				+ "FROM student s " + "LEFT JOIN attendance a ON s.STD_NO = a.STD_NO " + "AND a.ATTEND_DATE = ?";

		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
			pstmt.setDate(1, sqlDate);
			ResultSet rs = pstmt.executeQuery();

			// STD_NAME, SEAT_NO, ATTEND_STATUS, CI_TIME, CO_TIME 저장할 arrayList 생성
			ArrayList<AttendanceVO> todayList = new ArrayList<>();
			while (rs.next()) {
				String stdName = rs.getString("STD_NAME");
				int seatNo = rs.getInt("SEAT_NO");
				int attendStatus = rs.getInt("ATTEND_STATUS");
				LocalTime ciTime = rs.getTime("CI_TIME").toLocalTime();
				LocalTime coTime = rs.getTime("CO_TIME").toLocalTime();

				attendanceVo.setStd_name(stdName);
				attendanceVo.setSeat_no(seatNo);
				attendanceVo.setAttend_status(attendStatus);
				attendanceVo.setCi_time(ciTime);
				attendanceVo.setCo_time(coTime);

				todayList.add(attendanceVo);
			}

			LocalTime ciBaseTime = LocalTime.of(9, 0, 0); // 09:00:00
			LocalTime coBaseTime = LocalTime.of(18, 0, 0); // 18:00:00
			LocalTime xTime = LocalTime.of(0, 0, 0); // 00:00:00

			todayList.sort((a, b) -> Integer.compare(a.getSeat_no(), b.getSeat_no()));
			String[][] todayArray = new String[todayList.size()][2];

			// ciTime과 coTime으로 오늘의 attendStatus값 결정
			for (int i = 0; i < todayList.size(); i++) {
				AttendanceVO atVo = todayList.get(i);
				if (atVo.getAttend_status() == 0) {
					if (!atVo.getCi_time().equals(xTime) && atVo.getCi_time().isAfter(ciBaseTime)) {
						atVo.setAttend_status(3);
						; // 지각 3
						if (atVo.getCo_time().isBefore(coBaseTime)) {
							atVo.setAttend_status(4);
							; // 지각 후 조퇴 4
						}
					}
					if (!atVo.getCi_time().equals(xTime) && atVo.getCi_time().isBefore(ciBaseTime)) {
						atVo.setAttend_status(1);
						; // 출석 1
						if (atVo.getCo_time().isBefore(coBaseTime)) {
							atVo.setAttend_status(4); // 출석 후 조퇴 4
						}
					}
				}
				String printStatus = "";
				switch (atVo.getAttend_status()) {
				case 0:
					printStatus = "X";
					break;
				case 2:
					printStatus = "ㅁ";
					break;
				case 1:
					printStatus = "O";
					break;
				case 3:
				case 4:
					printStatus = "△";
					break;
				default:
					break;
				}
				todayArray[i][0] = printStatus;
				todayArray[i][1] = atVo.getStd_name();
			}

			int total = todayArray.length;
			int chunk = 5;
			for (int i = 0; i < total; i += chunk) {
				for (int j = 0; j < chunk && (i + j) < total; j++) {
					System.out.print(todayArray[i + j][0] + "\t");
				}
				System.out.println(); // 두 번째 줄을 출력하기 전에 한 줄 바꿈
				for (int j = 0; j < chunk && (i + j) < total; j++) {
					System.out.print(todayArray[i + j][1] + "\t");
				}
				System.out.println(); // 한 줄 바꿈
			}
		} catch (SQLException e) {
			System.out.println("lookupStudent() 함수 오류 - SQLException");
			e.printStackTrace();
		}
	}

	// *********교수-학생관리-휴가승인-휴가 보여줌
	public int lookupVacation() {
		String sql = "SELECT v.vacate_code, v.vacate_date, v.vacate_file, s.std_no, s.std_name " + "FROM VACATION v "
				+ "JOIN STUDENT s ON v.std_no = s.std_no";

		PreparedStatement pstmt = ConnectController.getPstmt(sql);
		try {
			ResultSet rs = ConnectController.executePstmtQuery(pstmt);
			System.out.println("휴가 코드 \t 학생 이름 \t 휴가 날짜");
			while (rs.next()) {
				System.out.print(rs.getInt("Vacate_code"));
				System.out.print(rs.getString("std_name"));
				System.out.println(rs.getDate("Vacate_Date"));
			}
		} catch (SQLException e) {
			System.out.println("lookupVacation 함수 - SQLException 또는 IOException 오류");
			e.printStackTrace();
		}

		System.out.print("승인할 휴가의 번호를 입력하세요: ");
		return ConnectController.scanIntData();
	}

	// *********approveVacation-교수 -학생관리 - 휴가 승인
	// select(vacation):vacation_date,
	// Update(attendance):attend_status, Insert(attendance):attend_status
	// delete(vacation):vacation_code***********
	public boolean selectVacation(int vacationCode) {
		String sql = "SELECT vacate_date, VACATE_FILE FROM VACATION WHERE vacate_code = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InputStream is = null;
		FileOutputStream fos = null;
		boolean bool = true;

		try {
			pstmt = ConnectController.getPstmt(sql);
			pstmt.setInt(1, vacationCode); // 파라미터 설정
			rs = ConnectController.executePstmtQuery(pstmt);

			if (!rs.isBeforeFirst()) {
				System.out.println("\t 해당 날짜에 데이터가 없습니다.");
			} else {
				// 결과가 존재하면, 데이터 처리
				while (rs.next()) {
					Date vacationDate = rs.getDate("vacate_date");
					System.out.println("휴가 날짜: " + vacationDate);

					// 파일 데이터 처리
					is = rs.getBinaryStream("VACATE_FILE");
					fos = new FileOutputStream("./image.png");
					byte[] buf = new byte[512];
					int len;
					while ((len = is.read(buf)) > 0) {
						fos.write(buf, 0, len);
					}
					System.out.println("파일 다운로드 완료.");
				}
			}
		} catch (SQLException e) {
			System.out.println("updateAttendance 오류: rs-SQLExeption 오류");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("파일 처리 오류");
			e.printStackTrace();
		} finally {
			// 자원 해제
			try {
				if (is != null)
					is.close();
				if (fos != null)
					fos.close();
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (IOException | SQLException e) {
				System.out.println("자원 해제 오류");
				e.printStackTrace();
			}
			System.out.print("해당 파일을 삭제하실 건가요?(1:yes,2:no)");

			if (ConnectController.scanIntData() != 1) {
				bool = false;
			}
		}
		return bool;
	}

	public void updateAttendance(int vacationCode) {
		String sql = "SELECT std_no, vacation_date, VACATE_FILE FROM VACATION WHERE vacation_code = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InputStream is = null;
		FileOutputStream fos = null;
		boolean bool = true;
		Date vacationDate = null;
		int stdNo = -1;

		pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, vacationCode); // 파라미터 설정
			rs = ConnectController.executePstmtQuery(pstmt);
			// 결과가 존재하면, 데이터 처리
			while (rs.next()) {
				stdNo = rs.getInt("std_no");
				vacationDate = rs.getDate("vacation_date");
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		if(UtilController.isNegative(stdNo)) {
			System.out.println("휴가 승인 신청 중 문제가 발생했습니다.");
			System.out.println("다시 시도해 주세요.");
			return;
		}
		
		// update 먼저 시도: 만약 조퇴이거나 지각을 사유로 처리한거면, 공가인거면 정보가 저장됨
		String updateSQL = "UPDATE attendance SET attend_status = ? WHERE std_no = ? AND attend_date = ?";
		pstmt = ConnectController.getPstmt(updateSQL);
		try {
			pstmt.setInt(1, 2);
			pstmt.setInt(2, stdNo);
			pstmt.setDate(3, vacationDate);// 근태 상태를 2로 설정
			int result = ConnectController.executePstmtUpdate(pstmt);
			// update 될 행이 없을 경우: 0값 반환 :insert함
			if (result == 0) {
				String insertSQL = "INSERT INTO ATTENDANCE (std_no,attend_date,attend_status) VALUES (?,?,?)";
				pstmt = ConnectController.getPstmt(insertSQL);
				try {
					pstmt.setInt(1, stdNo);
					pstmt.setDate(2, vacationDate);
					pstmt.setInt(3, 2);// 근태 상태를 2로 설정
				} catch (SQLException e) {
					System.out.println("updateAttendance함수 오류_INSERT -pstmt-SQLException");
					throw new RuntimeException(e);
				}
			} // if end

		} catch (SQLException e) {
			System.out.println("updateAttendance함수 오류_UPDATE -pstmt-SQLException");
			throw new RuntimeException(e);
		}

		ConnectController.executePstmtUpdate(pstmt);
		if (ConnectController.commit() == -1) {
			System.out.println("커밋 오류");
		}

		String deleteSQL = "DELETE FROM VACATION WHERE vacation_code = ?";
		pstmt = ConnectController.getPstmt(deleteSQL);
		try {
			pstmt.setInt(1, vacationCode);
			ConnectController.executePstmtUpdate(pstmt);
		} catch (SQLException e) {
			System.out.println("updateAttendance함수 오류 -pstmt-SQLException");
			throw new RuntimeException(e);
		}
		int rowsAffected = ConnectController.executePstmtUpdate(pstmt);
		System.out.println(rowsAffected + ": 휴가 승인");
	}
}