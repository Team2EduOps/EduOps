
package com.team2.eduops.controller;

import com.team2.eduops.model.StudentVO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AttendProfessorController {
	// ********출결 보기 *********
	// ********오늘 출석을 보기 위한 STD_NAME, SEAT_NO, ATTEND_STATUS, CI_TIME, CO_TIME을 학생별로
	// 저장할 클래스 생성
	public class TodayAttendance {
		String stdName;
		int seatNo;
		int attendStatus;
		LocalTime ciTime;
		LocalTime coTime;

		public TodayAttendance(String stdName, int seatNo, int attendStatus, LocalTime ciTime, LocalTime coTime) {
			this.stdName = stdName;
			this.seatNo = seatNo;
			this.attendStatus = attendStatus;
			this.ciTime = ciTime;
			this.coTime = coTime;
		}

		public int getSeatNo() {
			return seatNo;
		}

		public int getAttendStatus() {
			return attendStatus;
		}

		public String getStdName() {
			return stdName;
		}

		public LocalTime getCiTime() {
			return ciTime;
		}

		public LocalTime getCoTime() {
			return coTime;
		}

		public void setAttendStatus(int attendStatus) {
			this.attendStatus = attendStatus;
		}
	}

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
			ArrayList<TodayAttendance> todayList = new ArrayList<>();
			while (rs.next()) {
				String stdName = rs.getString("STD_NAME");
				int seatNo = rs.getInt("SEAT_NO");
				int attendStatus = rs.getInt("ATTEND_STATUS");
				LocalTime ciTime = rs.getTime("CI_TIME").toLocalTime();
				LocalTime coTime = rs.getTime("CO_TIME").toLocalTime();
				todayList.add(new TodayAttendance(stdName, seatNo, attendStatus, ciTime, coTime));
			}

			LocalTime ciBaseTime = LocalTime.of(9, 0, 0); // 09:00:00
			LocalTime coBaseTime = LocalTime.of(18, 0, 0); // 18:00:00
			LocalTime xTime = LocalTime.of(0, 0, 0); // 00:00:00

			todayList.sort((a, b) -> Integer.compare(a.getSeatNo(), b.getSeatNo()));
			String[][] todayArray = new String[todayList.size()][2];

			// ciTime과 coTime으로 오늘의 attendStatus값 결정
			for (int i = 0; i < todayList.size(); i++) {
				TodayAttendance tA = todayList.get(i);
				if (tA.getAttendStatus() == 0) {
					if (!tA.getCiTime().equals(xTime) && tA.getCiTime().isAfter(ciBaseTime)) {
						tA.setAttendStatus(3); // 지각
						if (tA.getCoTime().isBefore(coBaseTime)) {
							tA.setAttendStatus(4); // 지각 후 조퇴
						}
					}
					if (!tA.getCiTime().equals(xTime) && tA.getCiTime().isBefore(ciBaseTime)) {
						tA.setAttendStatus(1); // 출석
						if (tA.getCoTime().isBefore(coBaseTime)) {
							tA.setAttendStatus(4); // 출석 후 조퇴
						}
					}
				}
				String printStatus = "";
				switch (tA.getAttendStatus()) {
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
				todayArray[i][1] = tA.getStdName();
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
			System.out.println("Vacation code \t Std_name \t Vacation Date");
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
			System.out.print("해당 휴가를 승인하실 건가요?(1:yes,2:no)");

			if (ConnectController.scanIntData() != 1) {
				bool = false;
			}
		}
		return bool;
	}

	// **********휴가 승인***************
	public void updateVacation(int vacationCode) {
		String sql = "SELECT vacate_date, VACATE_FILE,std_no FROM VACATION WHERE vacate_code = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InputStream is = null;
		FileOutputStream fos = null;
		boolean bool = true;
		Date vacationDate = null;
		int stdNo = 0;
		pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, vacationCode);// 파라미터 설정
			rs = ConnectController.executePstmtQuery(pstmt);
			// 결과가 존재하면, 데이터 처리
			while (rs.next()) {
				vacationDate = rs.getDate("vacate_date");
				stdNo = rs.getInt("std_no");
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// update 먼저 시도: 만약 조퇴이거나 지각을 사유로 처리한거면, 공가인거면 정보가 저장됨
		String updateSQL = "UPDATE attendance SET attend_status = ? WHERE std_no = ? AND attend_date = ?";
		pstmt = ConnectController.getPstmt(updateSQL);
		try {
			pstmt.setInt(1, 2);
			pstmt.setInt(2, stdNo);
			pstmt.setDate(3, vacationDate);// 근태 상태를 2로 설정
			int result = ConnectController.executePstmtUpdate(pstmt);
			if (ConnectController.commit() == -1) {
				System.out.println("커밋 오류");
			}
			// update 될 행이 없을 경우: 0값 반환 :insert함
			if (result == 0) {
				String insertSQL = "INSERT INTO ATTENDANCE (std_no,attend_date,attend_status) VALUES (?,?,?)";
				pstmt = ConnectController.getPstmt(insertSQL);
				try {
					pstmt.setInt(1, stdNo);
					pstmt.setDate(2, vacationDate);
					pstmt.setInt(3, 2);// 근태 상태를 2로 설정
					ConnectController.executePstmtUpdate(pstmt);
					if (ConnectController.commit() == -1) {
						System.out.println("커밋 오류");
					}
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

		String deleteSQL = "DELETE FROM VACATION WHERE vacate_code = ?";
		pstmt = ConnectController.getPstmt(deleteSQL);
		try {
			pstmt.setInt(1, vacationCode);
			int rowsAffected = ConnectController.executePstmtUpdate(pstmt);
			if (ConnectController.commit() == -1) {
				System.out.println("커밋 오류");
			}
			System.out.println(rowsAffected + ": 휴가 승인");
		} catch (SQLException e) {
			System.out.println("updateAttendance함수 오류 -pstmt-SQLException");
			throw new RuntimeException(e);
		}
	}
	//////////////////////////////////////////////

	public void displayAttendance() {
		System.out.println("\t 1-1. 출결 보기");
		System.out.println("\t 출결 보기 페이지입니다.");

		// 예시 날짜 범위: 월요일부터 금요일
		LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endDate = startDate.plusDays(4); // 금요일

		System.out.println(startDate);
		System.out.println(endDate);

		// 날짜 형식 지정
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// java.sql.Date로 변환
		java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);
		java.sql.Date sqlEndDate = java.sql.Date.valueOf(endDate);

		String sql;
		PreparedStatement pstmt;
		ResultSet rs;

		// 학생번호 전체 가져오기
		sql = "select std_no, std_name from Student order by std_no asc";

		pstmt = ConnectController.getPstmt(sql);

		rs = ConnectController.executePstmtQuery(pstmt);

		HashMap<Integer, String> stdList = new HashMap<>();

		try {
			while (rs.next()) {
				stdList.put(rs.getInt("std_no"), rs.getString("std_name"));
			}
		} catch (Exception e) {
			System.out.println("학생 번호값 리스트에 넣는중 오류 발생");
			return;
		}

//		SELECT S.std_name, A.attend_status FROM Attendance A JOIN Student S ON A.std_no = S.std_no WHERE A.std_no = 1 AND A.attend_date BETWEEN TO_DATE('2024-07-01', 'YYYY-MM-DD') AND TO_DATE('2024-08-31', 'YYYY-MM-DD') ORDER BY A.attend_date ASC;

		sql = "SELECT S.std_name, A.attend_status, A.attend_date FROM Attendance A "
				+ "JOIN Student S ON A.std_no = S.std_no " + "WHERE A.std_no = ? AND A.attend_date BETWEEN ? AND ? "
				+ "ORDER BY A.attend_date ASC";

		for (Map.Entry<Integer, String> entry : stdList.entrySet()) {
			int stdNo = entry.getKey();
			String stdName = entry.getValue();

			pstmt = ConnectController.getPstmt(sql);
			try {
				pstmt.setInt(1, stdNo);
				pstmt.setDate(2, sqlStartDate);
				pstmt.setDate(3, sqlEndDate);
			} catch (Exception e) {
				System.out.println("pstmt set데이터 중 오류 발생");
				return;
			}

			rs = ConnectController.executePstmtQuery(pstmt);

			String mon = "X";
			String tue = "X";
			String wed = "X";
			String thu = "X";
			String fri = "X";

			try {
				while (rs.next()) {
					stdName = rs.getString("std_name");

					LocalDate attendDate = rs.getDate("attend_date").toLocalDate();
					int attendStatus = rs.getInt("attend_status");
					String printStatus = "X";

					switch (attendStatus) {
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

					// 각 요일에 해당하는 출석 상태를 설정
					switch (attendDate.getDayOfWeek()) {
					case MONDAY:
						mon = printStatus;
						break;
					case TUESDAY:
						tue = printStatus;
						break;
					case WEDNESDAY:
						wed = printStatus;
						break;
					case THURSDAY:
						thu = printStatus;
						break;
					case FRIDAY:
						fri = printStatus;
						break;
					default:
						// 다른 요일은 무시
						break;
					}

					// 각 요일의 출석 상태 출력
				}
				System.out.printf("%d\t%s\t%s\t%s\t%s\t%s\t%s\n", stdNo, stdName, mon, tue, wed, thu, fri);
				UtilController.line();
			} catch (Exception e) {
				System.out.println("문제 발생");
			}
		}

	}

	//////////////////////////////////////////////

	// 출결 변경
	public void updateAttendance() {
		int stdNo = -1;

		String strDate = null;
		String stdName;

		String sql;
		PreparedStatement pstmt;

		System.out.println("\t 1-2. 출결 변경");
		System.out.println("\t 출결 변경 페이지입니다.");

		// 출결 보기 메소드 호출하여 학생 출석 상황 뿌리기

		while (stdNo == -1) {
			// 변경할 학생 번호 입력 받기
			System.out.println("출결을 변경할 학생 번호를 입력해 주세요: ");
			stdNo = ConnectController.scanIntData();

			if (UtilController.isNegative(stdNo)) {
				System.out.println("학생 번호값이 정확하지 않습니다.");
				return;
			}

			// 학생 번호 맞는지 확인 메소드 (y/n)
			System.out.printf("입력하신 학생 번호가 %d가 맞습니까? Y/N", stdNo);

			String answer = ConnectController.scanData();

			if (!answer.equalsIgnoreCase("Y")) {
				stdNo = -1;
				System.out.println("학생 번호를 다시 입력해 주세요.");
			}
		}
		// 출결 변경할 날짜 입력 받기(String 입력 받고 date 타입 변경)

		while (strDate == null) {
			System.out.println("출결을 변경할 날짜를 입력해 주세요(yyyy-MM-dd): ");
			strDate = ConnectController.scanData();

			// 입력 형식 맞는지 확인 메소드 - 잘못된 날짜 형식입니다
			if (!isValidDate(strDate)) {
				strDate = null;
				System.out.println("날짜를 형식에 맞게 입력해 주세요.");
				continue;
			}

		}

		// 학생 번호와 출결 날짜로 그 날 출결 데이터 있는지 확인 후 없으면 없다고 출력 후 리턴

		// 학생 번호로 학생 이름 select 해오기
		stdName = selectStdName(stdNo);

		// '학생 이름'의 'yy/MM/dd' 일자의 정보를 변경합니다
		int statusNo = -1;
		while (statusNo == -1) {
			System.out.printf("%s의 %s 일자 정보를 변경합니다.", stdName, strDate);
			UtilController.line();
			System.out.println("1. 출석  2. 병가  3. 지각  4. 조퇴");

			// 1 /2 /3 /4 입력 받기
			statusNo = ConnectController.scanIntData();

			// 예외처리 - 정보값을 정확히 입력해 주세요
			switch (statusNo) {
			case 1, 2, 3, 4:
				break;
			default:
				statusNo = -1;
				System.out.println("정보값을 정확히 입력해 주세요.");
			}
		}

		// update문 실행
		sql = "update Attendance set attend_status = ? where std_no = ? and attend_date = ?";
		pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, statusNo);
			pstmt.setInt(2, stdNo);
			pstmt.setString(3, strDate);
		} catch (Exception e) {
			System.out.println("pstmt set데이터 중 에러 발생");
			return;
		}
		int result = -1;
		try {
			result = ConnectController.executePstmtUpdate(pstmt);
		} catch (Exception e) {
			System.out.println("executeUpdate 중 에러 발생");
			System.out.println("출결 변경이 실행되지 않았습니다.");
			return;
		}

		// 변경이 완료되었습니다
		if (result == 1) {
			System.out.println("출결 변경이 성공적으로 완료되었습니다.");
			return;
		}

		System.out.println("출결 변경 중 문제가 발생했습니다.");
		System.out.println("출결 변경이 실행되지 않았습니다.");

	}

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	// *********날짜 형식 맞는지 확인 메소드************
	private boolean isValidDate(String dateString) {
		if (dateString == null || dateString.isEmpty()) {
			return false;
		}
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(dateString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public String selectStdName(int stdNo) {
		String stdName = null;
		String sql = "select std_name from Student where std_no = ?";
		PreparedStatement pstmt = ConnectController.getPstmt(sql);

		try {
			pstmt.setInt(1, stdNo);
		} catch (Exception e) {

		}

		ResultSet rs = ConnectController.executePstmtQuery(pstmt);
		try {
			if (rs.next()) {
				stdName = rs.getString("std_name");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (UtilController.isNull(stdName)) {
			System.out.println("이름 받아오기 중 문제 발생");
		}
		return stdName;
	}

}
