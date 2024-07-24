package com.team2.eduops.controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.team2.eduops.model.AlgorithmNameVO;
import com.team2.eduops.model.AlgorithmVO;
import com.team2.eduops.model.StudentVO;

public class AlgorithmController {
	AlgorithmVO algorithmVo = new AlgorithmVO();
	AlgorithmNameVO algorithmNameVo = new AlgorithmNameVO();

	public void addAlgorithmName(StudentVO stdVo) { // 문제 출제
		selectAlgorithmAll();
		UtilController.line();
		insertAlgorithm(stdVo);
		UtilController.line();
		selectAlgorithmAll();
	}

	public void addAlgorithmAnswer(StudentVO stdVo) { // 문제 코드 제출
		selectAlgorithmAnswerAll();
		UtilController.line();
		insertAlgorithmAnswer(stdVo);
		UtilController.line();
		selectAlgorithmAnswerAll();
	}

	public void displayAlgorithmByDate() {
		// 날짜 포맷 확인 및 변환
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yy/MM/dd");
		Date sqlDate = null;
		String sql;
		PreparedStatement pstmt;
		ResultSet rs;

		// 날짜별 보기 처리
		while (sqlDate == null) {
			System.out.println("조회할 날짜를 입력하세요 (형식: yy/MM/dd): ");
			String selectDate = ConnectController.scanData();

			try {
				java.util.Date utilDate = inputDateFormat.parse(selectDate);
				sqlDate = new Date(utilDate.getTime());
			} catch (ParseException e) {
				sqlDate = null;
				System.out.println("날짜 형식이 잘못되었습니다. 형식: yy/MM/dd");
				break;
			}

		

		sql = "SELECT C.std_name, B.al_name, A.al_text, B.al_date " + "FROM Algorithm A "
				+ "INNER JOIN ALGORITHM_NAME B ON A.al_no = B.al_no " + "INNER JOIN STUDENT C ON A.std_no = C.std_no "
				+ "WHERE TRUNC(B.al_date) = ?";

		pstmt = ConnectController.getPstmt(sql);

		try {
			pstmt.setDate(1, sqlDate);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = ConnectController.executePstmtQuery(pstmt);
		if (UtilController.isNull(rs)) {
			System.out.println("해당 날짜에 데이터가 존재하지 않습니다");
		}

		// 열 너비 정의 (예상 데이터 크기에 따라 조정)
		int col1Width = 20; // std_name 용
		int col2Width = 20; // al_name 용
		int col3Width = 35; // al_text 용
		int col4Width = 20; // date 용

		// 헤더 출력
		System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
				"학생이름", "알고리즘 이름", "알고리즘 코드", "날짜");

		// 날짜 포맷 정의
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			// 결과 집합의 각 행 출력
			while (rs.next()) {
				String stdName = rs.getString("STD_NAME");
				String alName = rs.getString("AL_NAME");
				String alText = rs.getString("AL_TEXT");
				Date alDate = rs.getDate("AL_DATE");
				String date = dateFormat.format(alDate); // 포맷된 날짜 문자열

				// 긴 문자열 자르기
				alText = UtilController.truncateString(alText, col3Width - 3);

				// 데이터 행 출력
				System.out.printf(
						"%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
						stdName, alName, alText, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
	}

	//팀이름 보기
	public void displayAlgorithmByTeam() {
		String teamName = null;
		String sql;
		PreparedStatement pstmt;
		ResultSet rs;
		while (teamName == null) {
			// 팀별 보기 처리
			System.out.println("조회할 팀 이름을 입력하세요: ");
			teamName = ConnectController.scanData();
			
			

			sql = "SELECT C.std_name, B.al_name, A.al_text, C.team_name " +
                    "FROM Algorithm A " +
                    "INNER JOIN ALGORITHM_NAME B ON A.al_no = B.al_no " +
                    "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                    "WHERE C.team_name = ?";

			pstmt = ConnectController.getPstmt(sql);
			
			try {
				
				pstmt.setString(1, teamName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		

			rs = ConnectController.executePstmtQuery(pstmt);
			if (UtilController.isNull(rs)) {
				teamName = null;
				System.out.println("해당 팀이 존재하지 않습니다. ");
				return;
			}
			
			try {

				// 열 너비 정의 (예상 데이터 크기에 따라 조정)
                int col1Width = 20; // std_name 용
                int col2Width = 20; // al_name 용
                int col3Width = 35; // al_text 용
                int col4Width = 20; // team_name 용
                
                // 헤더 출력
                System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                        "학생이름", "알고리즘 이름", "알고리즘 코드", "팀이름");

                // 결과 집합의 각 행 출력
                while (rs.next()) {
                    String stdName = rs.getString("STD_NAME");
                    String alName = rs.getString("AL_NAME");
                    String alText = rs.getString("AL_TEXT");
                    String team = rs.getString("TEAM_NAME");

                    // 긴 문자열 자르기
                    alText = UtilController.truncateString(alText, col3Width - 3);

                 // 데이터 행 출력
                    System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                            stdName, alName, alText, team);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	// insert // 알고리즘 번호 및 코드 기입
	public void insertAlgorithmAnswer(StudentVO stdvo) {
		String sql = "INSERT INTO " + algorithmVo.getClassName() + " ( AL_TEXT, STD_NO, AL_NO ) VALUES (?,?,?)";
		int result = -1;
		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
			System.out.println("알고리즘 코드 : ");
			String algorithmText = ConnectController.scanData();
			System.out.println("알고리즘 번호 : ");
			String algorithmNo = ConnectController.scanData();
			int stdNo = stdvo.getStd_no();
			pstmt.setString(1, algorithmText);
			pstmt.setInt(2, stdNo);
			pstmt.setString(3, algorithmNo);
			result = ConnectController.executePstmtUpdate(pstmt);
			System.out.println(result + "개 입력완료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(!UtilController.isNegative(result)) {
			ConnectController.commit();
		}
	}

	// select all //모든 데이터 선택
	public void selectAlgorithmAnswerAll() {
		String sql = "SELECT AL_TEXT, STD_NO, AL_NO FROM " + algorithmVo.getClassName();

		try (PreparedStatement pstmt = ConnectController.getPstmt(sql);
				ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {

			// 열 너비 정의 (예상 데이터 크기에 따라 조정)
			int col1Width = 35; // AL_TEXT 용
			int col2Width = 20; // STD_NO 용
			int col3Width = 20; // AL_NO 용

			// 헤더 출력
			System.out.printf("%-" + col1Width + "s%" + col2Width + "s%" + col3Width + "s\n", "알고리즘 코드", "제출자 번호",
					"알고리즘 번호");

			// 결과 집합의 각 행 출력
			while (rs.next()) {
				String alText = rs.getString("AL_TEXT");
				int stdNo = rs.getInt("STD_NO");
				int alNo = rs.getInt("AL_NO");

				// 긴 문자열 자르기
				alText = UtilController.truncateString(alText, col1Width - 3);

				// 데이터 행 출력
				System.out.printf("%-" + col1Width + "s%" + col2Width + "d%" + col3Width + "d\n", alText, stdNo, alNo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// insert 문제 출제 // 알고리즘 주소, 알고리즘 이름, 담당자번호
	public void insertAlgorithm(StudentVO stdvo) {
		String sql = "INSERT INTO " + algorithmNameVo.getClassName() + " (AL_URL, AL_NAME, STD_NO) VALUES (?,?,?)";
		int result = -1;
		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
			System.out.println("알고리즘 주소 : ");
			String algorithmUrl = ConnectController.scanData();
			System.out.println("알고리즘 이름 : ");
			String algorithmName = ConnectController.scanData();
			int stdNo = stdvo.getStd_no();
			pstmt.setString(1, algorithmUrl);
			pstmt.setString(2, algorithmName);
			pstmt.setInt(3, stdNo);
			
			result = ConnectController.executePstmtUpdate(pstmt);
			System.out.println(result + "개 입력완료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!UtilController.isNegative(result)) {
			ConnectController.commit();
		}
	}

	// select all
	public void selectAlgorithmAll() {
		String sql = "SELECT * FROM " + algorithmNameVo.getClassName();

		try (PreparedStatement pstmt = ConnectController.getPstmt(sql);
				ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {

			// 각 열의 너비를 고정합니다.
			int col1Width = 15;
			int col2Width = 35;
			int col3Width = 20;
			int col4Width = 20;
			int col5Width = 30;

			System.out.printf("%-" + col1Width + "s%-" + col2Width + "s%-" + col3Width + "s%-" + col4Width + "s%-"
					+ col5Width + "s\n", "알고리즘 번호", "알고리즘 주소", "알고리즘 이름", "담당자 번호", "날짜");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			// 데이터 레코드를 출력합니다.
			while (rs.next()) {
				int alno = rs.getInt("AL_NO");
				String alurl = rs.getString("AL_URL");
				String alname = rs.getString("AL_NAME");
				int stdno = rs.getInt("STD_NO");
				Date sqlDate = rs.getDate("AL_DATE");
				String date = dateFormat.format(sqlDate); // 포맷된 날짜 문자열

				// 긴 텍스트 자르기
				alurl = UtilController.truncateString(alurl, col2Width - 3);
				alname = UtilController.truncateString(alname, col3Width - 3);
				date = UtilController.truncateString(date, col5Width - 3);

				// 데이터 출력
				System.out.printf("%-" + col1Width + "d%" + col2Width + "s%" + col3Width + "s%" + col4Width + "d%"
						+ col5Width + "s\n", alno, alurl, alname, stdno, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

} // class end