package com.team2.eduops.controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.QuizNameVO;
import com.team2.eduops.model.QuizVO;
import com.team2.eduops.model.StudentVO;

public class QuizController {
	QuizVO quizVo = new QuizVO();
	QuizNameVO quizNameVo = new QuizNameVO();

	// 퀴즈 문제 제출
	public void addQuiz(AdminVO admVo) {
		selectQuizAll();
		UtilController.line();
		insertQuiz(admVo);
		UtilController.line();
		selectQuizAll();
	}

	// 퀴즈코드 추가 메소드
	public void addQuizAnswer(StudentVO stdVo) {
//		selectQuizAnswer(stdVo);
//		UtilController.line();
		insertQuizAnswer(stdVo);
//		UtilController.line();
//		selectQuizAnswer(stdVo);
	}

	//////// insert ////////////////////////////
/*
	// insert // 퀴즈 코드 제출
	public void insertQuizAnswer(StudentVO stdVo) {
		String sql = "INSERT INTO " + quizVo.getClassName() + " (QUIZ_TEXT, STD_NO, QUIZ_NO) VALUES (?,?,?)";
		int result = -1;
		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
//			System.out.println("퀴즈 코드: ");
//			String quizCode = ConnectController.scanData();
			System.out.println("퀴즈 코드를 입력하세요 (종료하려면 'END' 입력):");
			String QuizText = getQuizTextFromInput();
			//System.out.println("퀴즈 번호: ");
			System.out.println("퀴즈 번호를 입력하세요 (메뉴로 돌아가려면 0 입력):");
			String quizNo = ConnectController.scanData();
			
			// 퀴즈 번호가 0이면 메서드를 종료하고 메뉴로 돌아감
	        if ("0".equals(quizNo)) {
	            System.out.println("메뉴로 돌아갑니다.");
	            return;
	        }
			
			int stdNo = stdVo.getStd_no();
			
			pstmt.setString(1, QuizText);
			pstmt.setInt(2, stdNo);
			pstmt.setString(3, quizNo);
			result = ConnectController.executePstmtUpdate(pstmt);
			System.out.println(result + "개 입력완료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!UtilController.isNegative(result)) {
			ConnectController.commit();
		}
	}
*/
	// insert // 퀴즈 코드 제출
	public void insertQuizAnswer(StudentVO stdVo) {
	    String sql = "INSERT INTO " + quizVo.getClassName() + " (QUIZ_TEXT, STD_NO, QUIZ_NO) VALUES (?,?,?)";
	    int result = -1;
	    
	    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
	        // 퀴즈 텍스트 입력 받기
	        System.out.println("퀴즈 코드를 입력하세요 (종료하려면 'END' 입력):");
	        String QuizText = getQuizTextFromInput();

	        // 유효한 퀴즈 번호를 받을 때까지 루프
	        while (true) {
	            System.out.println("퀴즈 번호를 입력하세요 (메뉴로 돌아가려면 0 입력):");
	            String quizNo = ConnectController.scanData();

	            // 메뉴로 돌아가기
	            if ("0".equals(quizNo)) {
	                System.out.println("메뉴로 돌아갑니다.");
	                return; // 메서드 종료
	            }

	            // 퀴즈 번호 유효성 검사
	            if (isValidQuizNo(quizNo)) {
	                // 유효한 퀴즈 번호일 경우 데이터 삽입
	                int stdNo = stdVo.getStd_no();

	                pstmt.setString(1, QuizText);
	                pstmt.setInt(2, stdNo);
	                pstmt.setString(3, quizNo);
	                result = ConnectController.executePstmtUpdate(pstmt);
	                System.out.println(result + "개 입력완료");

	                if (!UtilController.isNegative(result)) {
	                    ConnectController.commit();
	                }
	                break; // 성공적으로 입력이 완료되면 루프 종료
	            } else {
	                // 유효하지 않은 퀴즈 번호 입력 시 메시지 출력
	                System.out.println("유효하지 않은 퀴즈 번호입니다. 다시 시도하세요.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// 퀴즈 번호 유효성 검사 메서드
	private boolean isValidQuizNo(String quizNo) {
	    String validationQuery = "SELECT 1 FROM " + quizNameVo.getClassName() + " WHERE QUIZ_NO = ?";
	    try (PreparedStatement validationPstmt = ConnectController.getPstmt(validationQuery)) {
	        validationPstmt.setString(1, quizNo);
	        ResultSet rs = validationPstmt.executeQuery();
	        return rs.next(); // 결과가 있으면 true, 없으면 false
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // 예외 발생 시 false 반환
	}


	private String getQuizTextFromInput() {
		Scanner scanner = new Scanner(System.in);
		StringBuilder QuizText = new StringBuilder();
		String line;

		while (true) {
			line = scanner.nextLine();
			if (line.equalsIgnoreCase("END")) {
				break;
			}
			QuizText.append(line).append("\n");
		}

		return QuizText.toString();
	}

	
	
	// insert // 퀴즈 문제 제출 - 이름, 담당자(관리자)
	public void insertQuiz(AdminVO admVo) {
	    String sql = "INSERT INTO " + quizNameVo.getClassName() + " (QUIZ_NAME, ADM_NO) VALUES (?,?)";
	    int result = -1;

	    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
	        String quizName = null;

	        // 퀴즈 이름 입력 받기
	        while (quizName == null || quizName.trim().isEmpty()) {
	            System.out.println("퀴즈 이름을 입력하세요 (메뉴로 돌아가려면 0 입력):");
	            quizName = ConnectController.scanData();

	            // 퀴즈 이름이 0이면 메서드를 종료하고 메뉴로 돌아감
	            if ("0".equals(quizName)) {
	                System.out.println("메뉴로 돌아갑니다.");
	                return;
	            }
	            
	            // 빈 문자열이나 공백이 입력된 경우 다시 입력 받기
	            if (quizName.trim().isEmpty()) {
	                System.out.println("퀴즈 이름은 비어 있을 수 없습니다. 다시 입력하세요.");
	                quizName = null; // 재입력을 위해 null로 설정
	            }
	        }

	        int admNo = admVo.getAdm_no();
	        pstmt.setString(1, quizName);
	        pstmt.setInt(2, admNo);

	        result = ConnectController.executePstmtUpdate(pstmt);
	        System.out.println(result + "개 입력완료");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    if (!UtilController.isNegative(result)) {
	        ConnectController.commit();
	    }
	}

	
	
/*
	// insert // 퀴즈 문제 제출 - 이름, 담당자(관리자)
	public void insertQuiz(AdminVO admVo) {

		String sql = "INSERT INTO " + quizNameVo.getClassName() + " (QUIZ_NAME, ADM_NO) VALUES (?,?)";
		int result = -1;
		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
			System.out.println("퀴즈 이름: (메뉴로 돌아가려면 0 입력)");
			String quizName = ConnectController.scanData();
			
			// 알고리즘 주소가 0이면 메서드를 종료하고 메뉴로 돌아감
						if ("0".equals(quizName)) {
				            System.out.println("메뉴로 돌아갑니다.");
				            return;
				        }
			int admNo = admVo.getAdm_no();
			pstmt.setString(1, quizName);
			pstmt.setInt(2, admNo);
			result = ConnectController.executePstmtUpdate(pstmt);
			System.out.println(result + "개 입력완료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!UtilController.isNegative(result)) {
			ConnectController.commit();
		}
	}
*/
	//////// insert ////////////////////////////

	////// selectAll ////////
	// select all //퀴즈 코드 보기- 모든 데이터 선택
	public void selectQuizAnswer(StudentVO stdVo) {
		String sql = "SELECT QUIZ_TEXT, STD_NO, QUIZ_NO FROM " + quizVo.getClassName() + " WHERE STD_NO = ?";

		try {

			PreparedStatement pstmt = ConnectController.getPstmt(sql);
			pstmt.setInt(1, stdVo.getStd_no());
			
			ResultSet rs = ConnectController.executePstmtQuery(pstmt);
			// 열 너비 정의 (예상 데이터 크기에 따라 조정)
			int col1Width = 35; // AL_TEXT 용
			int col2Width = 20; // STD_NO 용
			int col3Width = 20; // AL_NO 용

//			// 헤더 출력
//			System.out.printf("%-" + col1Width + "s%" + col2Width + "s%" + col3Width + "s\n", "퀴즈 코드", "제출자 번호",
//					"퀴즈 번호");

			// 결과 집합의 각 행 출력
			while (rs.next()) {
				String quizText = rs.getString("QUIZ_TEXT");
				int stdNo = rs.getInt("STD_NO");
				int quizNo = rs.getInt("QUIZ_NO");

//				// 긴 문자열 자르기
//				quizText = UtilController.truncateString(quizText, col1Width - 3);
//
//				// 데이터 행 출력
//				System.out.printf("%-" + col1Width + "s%" + col2Width + "d%" + col3Width + "d\n", quizText, stdNo,
//						quizNo);

				// 원하는 출력 형식으로 결과 출력
				System.out.println("퀴즈코드 - ");
				System.out.println(quizText);
				System.out.println("학생 번호 - " + stdNo);
				System.out.println("퀴즈 번호 - " + quizNo);
				System.out.println(); // 빈 줄을 추가하여 각 레코드 구분
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// select all // 문제보기
	public void selectQuizAll() {

		String sql = "SELECT * FROM " + quizNameVo.getClassName();

		try (PreparedStatement pstmt = ConnectController.getPstmt(sql);
				ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {

			// 각 열의 너비를 고정합니다.
			int col1Width = 15;
			int col2Width = 20;
			int col3Width = 20;
			int col4Width = 30;

			System.out.printf("%-" + col1Width + "s%-" + col2Width + "s%-" + col3Width + "s%-" + col4Width + "s\n",
					"퀴즈 번호", "퀴즈 이름", "담당자 번호", "날짜");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			// 데이터 레코드를 출력합니다.
			while (rs.next()) {
				int quizno = rs.getInt("QUIZ_NO");
				String quizname = rs.getString("QUIZ_NAME");
				int admno = rs.getInt("ADM_NO");
				java.sql.Date sqlDate = rs.getDate("QUIZ_DATE");
				String date = dateFormat.format(sqlDate); // 포맷된 날짜 문자열

				// 긴 텍스트 자르기
				quizname = UtilController.truncateString(quizname, col2Width - 3);
				date = UtilController.truncateString(date, col4Width - 3);

				// 데이터 출력
				System.out.printf("%-" + col1Width + "d%" + col2Width + "s%" + col3Width + "d%" + col4Width + "s\n",
						quizno, quizname, admno, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	////// selectAll ////////

	/////// select 조건문 /////////
	// 퀴즈 날짜별 보기 - 관리자
	/*
	 * public void displayQuizByDate() { // 날짜 포맷 확인 및 변환 SimpleDateFormat
	 * inputDateFormat = new SimpleDateFormat("yy/MM/dd"); Date sqlDate = null;
	 * String sql; PreparedStatement pstmt; ResultSet rs;
	 * 
	 * // 날짜별 보기 처리 while (sqlDate == null) {
	 * System.out.println("조회할 날짜를 입력하세요 (형식: yy/MM/dd): "); String selectDate =
	 * ConnectController.scanData();
	 * 
	 * try { java.util.Date utilDate = inputDateFormat.parse(selectDate); sqlDate =
	 * new Date(utilDate.getTime()); } catch (ParseException e) { sqlDate = null;
	 * System.out.println("날짜 형식이 잘못되었습니다. 형식: yy/MM/dd"); continue; } }
	 * 
	 * sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, B.quiz_date " +
	 * "FROM Quiz A " + "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no " +
	 * "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
	 * "WHERE TRUNC(B.quiz_date) = ?";
	 * 
	 * pstmt = ConnectController.getPstmt(sql);
	 * 
	 * try { pstmt.setDate(1, sqlDate); } catch (SQLException e) {
	 * e.printStackTrace(); System.out.println("pstmt set데이터 중 문제 발생"); }
	 * 
	 * rs = ConnectController.executePstmtQuery(pstmt); if
	 * (UtilController.isNull(rs)) {
	 * System.out.println("executePstmtQuery 중 문제 발생"); return; } // 열 너비 정의 (예상 데이터
	 * 크기에 따라 조정) int col1Width = 20; // std_name 용 int col2Width = 20; // quiz_name
	 * 용 int col3Width = 35; // quiz_text 용 int col4Width = 20; // date 용
	 * 
	 * // 헤더 출력 System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" +
	 * col3Width + "s %-" + col4Width + "s\n", "학생이름", "퀴즈 이름", "퀴즈 내용", "날짜");
	 * 
	 * // 날짜 포맷 정의 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	 * try { // 결과 집합의 각 행 출력 while (rs.next()) { String stdName =
	 * rs.getString("STD_NAME"); String quizName = rs.getString("QUIZ_NAME"); String
	 * quizText = rs.getString("QUIZ_TEXT"); Date quizDate =
	 * rs.getDate("QUIZ_DATE"); String date = dateFormat.format(quizDate); // 포맷된 날짜
	 * 문자열
	 * 
	 * // 긴 문자열 자르기 quizText = UtilController.truncateString(quizText, col3Width -
	 * 3);
	 * 
	 * // 데이터 행 출력 System.out.printf( "%-" + col1Width + "s %-" + col2Width + "s %-"
	 * + col3Width + "s %-" + col4Width + "s\n", stdName, quizName, quizText, date);
	 * } } catch (SQLException e) { e.printStackTrace();
	 * System.out.println("rs.next() 진행중 문제 발생"); } }
	 */
	// 퀴즈 날짜별 보기 - 관리자 (수정버전)
	public void displayQuizByDate() {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yy/MM/dd");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date sqlDate = null;
		String sql;
		PreparedStatement pstmt;
		ResultSet rs;

		// 날짜 입력 및 데이터 조회
		while (true) {
			System.out.println("조회할 날짜를 입력하세요 (형식: yy/MM/dd), 메뉴로 돌아가려면 0 입력):");
			String selectDate = ConnectController.scanData();

			// 날짜가 0이면 메서드를 종료하고 메뉴로 돌아감
	        if ("0".equals(selectDate)) {
	            System.out.println("메뉴로 돌아갑니다.");
	            return;
	        }
			
			try {
				java.util.Date utilDate = inputDateFormat.parse(selectDate);
				sqlDate = new Date(utilDate.getTime());
			} catch (ParseException e) {
				System.out.println("날짜 형식이 잘못되었습니다. 형식: yy/MM/dd");
				continue; // 날짜 형식이 잘못되었으면 다시 입력 받기
			}

			sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, B.quiz_date " + "FROM Quiz A "
					+ "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no "
					+ "INNER JOIN STUDENT C ON A.std_no = C.std_no " + "WHERE TRUNC(B.quiz_date) = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setDate(1, sqlDate);
				rs = ConnectController.executePstmtQuery(pstmt);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("쿼리 실행 중 문제 발생");
				return; // 쿼리 실행 중 문제 발생 시 메서드 종료
			}

			if (UtilController.isNull(rs)) {
				System.out.println("해당 날짜에 데이터가 존재하지 않습니다.");
				return; // 데이터가 없는 경우 메서드 종료
			}

			// 결과 집합의 각 행 출력
			try {
				while (rs.next()) {
					String stdName = rs.getString("STD_NAME");
					String quizName = rs.getString("QUIZ_NAME");
					String quizText = rs.getString("QUIZ_TEXT");
					Date quizDate = rs.getDate("QUIZ_DATE");
					String date = outputDateFormat.format(quizDate); // 포맷된 날짜 문자열

					// 원하는 출력 형식으로 결과 출력
					System.out.println("학생이름 - " + stdName);
					System.out.println("퀴즈 이름 - " + quizName);
					System.out.println("퀴즈 내용 - ");
					System.out.println(quizText);
					System.out.println("날짜 - " + date);
					System.out.println(); // 빈 줄을 추가하여 각 레코드 구분
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("결과 출력 중 문제 발생");
			}

			// 다른 날짜 조회 여부 묻기
			System.out.println("다른 날짜를 조회하시겠습니까? (y/n)");
			String response = ConnectController.scanData();
			if (!response.equalsIgnoreCase("y")) {
				return; // 다시검색
			}
		}
	}

	/*
	 * // 퀴즈 팀별 보기 - 관리자 public void displayQuizByTeam() { String teamName = null;
	 * String sql; PreparedStatement pstmt; ResultSet rs;
	 * 
	 * while (teamName == null) { // 팀별 보기 처리
	 * System.out.println("조회할 팀 이름을 입력하세요: "); teamName =
	 * ConnectController.scanData();
	 * 
	 * 
	 * sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, C.team_name " +
	 * "FROM Quiz A " + "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no " +
	 * "INNER JOIN STUDENT C ON A.std_no = C.std_no " + "WHERE C.team_name = ?";
	 * 
	 * pstmt = ConnectController.getPstmt(sql);
	 * 
	 * try { pstmt.setString(1, teamName);
	 * 
	 * } catch (SQLException e) { e.printStackTrace();
	 * System.out.println("pstmt set데이터 중 문제 발생"); }
	 * 
	 * rs = ConnectController.executePstmtQuery(pstmt); if
	 * (UtilController.isNull(rs)) { teamName = null;
	 * System.out.println("executeQuery 도중 문제 발생"); continue; }
	 * 
	 * try { if(!rs.next()) { teamName = null;
	 * System.out.println("해당하는 팀의 데이터가 존재하지 않습니다."); return; } } catch(Exception e)
	 * { e.printStackTrace(); System.out.println("rs 값 next() 중 문제 발생"); }
	 * 
	 * 
	 * // 열 너비 정의 (예상 데이터 크기에 따라 조정) int col1Width = 20; // std_name 용 int col2Width
	 * = 20; // quiz_name 용 int col3Width = 35; // quiz_text 용 int col4Width = 20;
	 * // team_name 용
	 * 
	 * // 헤더 출력 System.out.printf( "%-" + col1Width + "s %-" + col2Width + "s %-" +
	 * col3Width + "s %-" + col4Width + "s\n", "학생이름", "퀴즈 이름", "퀴즈 내용", "팀이름");
	 * 
	 * 
	 * try { // 결과 집합의 각 행 출력 do { String stdName = rs.getString("STD_NAME"); String
	 * quizName = rs.getString("QUIZ_NAME"); String quizText =
	 * rs.getString("QUIZ_TEXT"); String team = rs.getString("TEAM_NAME");
	 * 
	 * // 긴 문자열 자르기 quizText = UtilController.truncateString(quizText, col3Width -
	 * 3);
	 * 
	 * // 데이터 행 출력 System.out.printf( "%-" + col1Width + "s %-" + col2Width + "s %-"
	 * + col3Width + "s %-" + col4Width + "s\n", stdName, quizName, quizText, team);
	 * } while(rs.next()); } catch (SQLException e) { e.printStackTrace();
	 * System.out.println("rs 내용 출력 실행 중 문제 발생"); } } }
	 */
	// 팀별 보기 처리 수정버전
	public void displayQuizByTeam() {
		String teamName = null;
		String sql;
		PreparedStatement pstmt;
		ResultSet rs;

		while (true) {
			System.out.println("조회할 팀 이름을 입력하세요, 메뉴로 돌아가려면 0 입력 : ");
			teamName = ConnectController.scanData();
			
			// 팀이름이 0이면 메서드를 종료하고 메뉴로 돌아감
	        if ("0".equals(teamName)) {
	            System.out.println("메뉴로 돌아갑니다.");
	            return;
	        }

			sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, C.team_name " + "FROM Quiz A "
					+ "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no "
					+ "INNER JOIN STUDENT C ON A.std_no = C.std_no " + "WHERE C.team_name = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setString(1, teamName);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("pstmt set 데이터 중 문제 발생");
				continue; // 문제 발생 시 다시 팀 이름 입력 받기
			}

			rs = ConnectController.executePstmtQuery(pstmt);
			if (UtilController.isNull(rs)) {
				teamName = null; // 다시 팀 이름 입력 받기
				System.out.println("해당 팀의 데이터가 존재하지 않습니다.");
				continue;
			}

			try {
				// 결과 집합의 각 행 출력
				boolean hasData = false;
				while (rs.next()) {
					hasData = true;
					String stdName = rs.getString("STD_NAME");
					String quizName = rs.getString("QUIZ_NAME");
					String quizText = rs.getString("QUIZ_TEXT");
					String team = rs.getString("TEAM_NAME");

					// 원하는 출력 형식으로 결과 출력
					System.out.println("학생이름 - " + stdName);
					System.out.println("퀴즈 이름 - " + quizName);
					System.out.println("코드 - ");
					System.out.println(quizText);
					System.out.println("팀이름 - " + team); // 빈 줄을 추가하여 각 레코드 구분
				}

				if (!hasData) {
					System.out.println("해당 팀의 데이터가 존재하지 않습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("쿼리 실행 중 문제 발생");
			}

			// 팀 이름 입력을 다시 받을지 여부를 사용자에게 묻기
			UtilController.line();
			System.out.println("다른 팀 이름을 조회하시겠습니까? (y/n)");
			String response = ConnectController.scanData();
			if (!response.equalsIgnoreCase("y")) {
				return; // 종료
			}
		}
	}

	/////// select 조건문 /////////

	//////// update ///////////////////

	/*
	 * // update public void updateQuizAnswer(String ClassName) { while (true) {
	 * 
	 * System.out.println("0 선택 ==> 업데이트 탈출합니다.");
	 * System.out.print("수정할 STD_NO 입력: ");
	 * 
	 * int stdNo = ConnectController.scanIntData();
	 * 
	 * if (stdNo == 0) { break; }
	 * 
	 * System.out.print("수정할 QUIZ_NO 입력: "); int quizNo =
	 * ConnectController.scanIntData();
	 * 
	 * System.out.println("수정할 필드를 선택하세요."); System.out.println("1. 퀴즈 코드");
	 * System.out.println("2. 제출자 번호"); System.out.println("3. 퀴즈 번호");
	 * System.out.print("수정할 필드 선택: "); int choice =
	 * ConnectController.scanIntData();
	 * 
	 * String sql = null; switch (choice) { case 1: sql = "UPDATE " + ClassName +
	 * " SET QUIZ_TEXT = ? WHERE STD_NO AND WHERE QUIZ_NO= ?";
	 * System.out.print("새 퀴즈 코드 입력: "); String newquizname =
	 * ConnectController.scanData(); try (PreparedStatement pstmt =
	 * ConnectController.getPstmt(sql)) { pstmt.setString(1, newquizname);
	 * pstmt.setInt(2, stdNo); pstmt.setInt(3, quizNo); int result =
	 * ConnectController.executePstmtUpdate(pstmt); System.out.println(result +
	 * "개의 레코드가 수정되었습니다."); } catch (SQLException e) { e.printStackTrace(); } break;
	 * case 2: sql = "UPDATE " + ClassName +
	 * " SET STD_NO = ? WHERE STD_NO AND WHERE QUIZ_NO= ?";
	 * System.out.print("새 제출자 번호 입력: "); String newstdno =
	 * ConnectController.scanData(); try (PreparedStatement pstmt =
	 * ConnectController.getPstmt(sql)) { pstmt.setString(1, newstdno);
	 * pstmt.setInt(2, stdNo); pstmt.setInt(3, quizNo); int result =
	 * ConnectController.executePstmtUpdate(pstmt); System.out.println(result +
	 * "개의 레코드가 수정되었습니다."); } catch (SQLException e) { e.printStackTrace(); } break;
	 * case 3: sql = "UPDATE " + ClassName +
	 * " SET QUIZ_NO = ? WHERE STD_NO AND WHERE QUIZ_NO= ?";
	 * System.out.print("새 퀴즈 번호 입력: "); String newquizno =
	 * ConnectController.scanData(); try (PreparedStatement pstmt =
	 * ConnectController.getPstmt(sql)) { pstmt.setString(1, newquizno);
	 * pstmt.setInt(2, stdNo); pstmt.setInt(3, quizNo); int result =
	 * ConnectController.executePstmtUpdate(pstmt); System.out.println(result +
	 * "개의 레코드가 수정되었습니다."); } catch (SQLException e) { e.printStackTrace(); } break;
	 * default: System.out.println("잘못된 선택입니다."); break; } } }
	 */

	//////// update ///////////////////

	//// ReturnToMainMenuException 사용하여 뒤로가기 /////
	/*
	 * public static void quizmanagement() { while (true) { try {
	 * 
	 * 
	 * // 메뉴 번호 입력받기 int menuNo = ConnectController.scanIntData(); String sql =
	 * null; switch (menuNo) {
	 * 
	 * case 1: Quiz_name_Controller.menu(); // 알고리즘 문제 선정 break; case 2:
	 * 
	 * break; case 3:
	 * 
	 * break; case 4: System.out.println("프로그램 종료합니다. ! ! ! "); System.exit(0); }
	 * //switch end }catch(Quiz_name_Controller.ReturnToMainMenuException e){ //메인
	 * 메뉴로 돌아가기 System.out.println("메인 메뉴로 돌아갑니다. "); } }
	 * 
	 * 
	 * public static class ReturnToMainMenuException extends RuntimeException {}
	 */

	/*
	 * // update public static void update(String ClassName) { while (true) {
	 * 
	 * System.out.println("0 선택 ==> 업데이트 탈출합니다.");
	 * System.out.print("수정할 Quiz_NO 입력: "); int quizNo =
	 * ConnectController.scanIntData();
	 * 
	 * if (quizNo == 0) { break; }
	 * 
	 * System.out.println("수정할 필드를 선택하세요."); System.out.println("1. 퀴즈 이름");
	 * System.out.println("2. 담당자 번호"); System.out.print("수정할 필드 선택: "); int choice
	 * = ConnectController.scanIntData();
	 * 
	 * String sql = null; switch (choice) { case 1: sql = "UPDATE " + ClassName +
	 * " SET QUIZ_NAME = ? WHERE QUIZ_NO = ?"; System.out.print("새 퀴즈 이름 입력: ");
	 * String newquizname = ConnectController.scanData(); try (PreparedStatement
	 * pstmt = ConnectController.getPstmt(sql)) { pstmt.setString(1, newquizname);
	 * pstmt.setInt(2, quizNo); int result =
	 * ConnectController.executePstmtUpdate(pstmt); System.out.println(result +
	 * "개의 레코드가 수정되었습니다."); } catch (SQLException e) { e.printStackTrace(); } break;
	 * case 2: sql = "UPDATE " + ClassName + " SET ADM_NO = ? WHERE QUIZ_NO = ?";
	 * System.out.print("새 담당자 번호 입력: "); String newadmno =
	 * ConnectController.scanData(); try (PreparedStatement pstmt =
	 * ConnectController.getPstmt(sql)) { pstmt.setString(1, newadmno);
	 * pstmt.setInt(2, quizNo); int result =
	 * ConnectController.executePstmtUpdate(pstmt); System.out.println(result +
	 * "개의 레코드가 수정되었습니다."); } catch (SQLException e) { e.printStackTrace(); } break;
	 * default: System.out.println("잘못된 선택입니다."); break; } } }
	 */

}