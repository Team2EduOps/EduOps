package com.team2.eduops.controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

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
//		selectAlgorithmAnswer(stdVo);
//		UtilController.line();
		insertAlgorithmAnswer(stdVo);
//		UtilController.line();
//		selectAlgorithmAnswer(stdVo);
	}

	/*
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
*/
	
	//알고리즘 날짜별 보기 수정버전
    public void displayAlgorithmByDate() {
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

            sql = "SELECT C.std_name, B.al_name, A.al_text, B.al_date " +
                  "FROM Algorithm A " +
                  "INNER JOIN ALGORITHM_NAME B ON A.al_no = B.al_no " +
                  "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                  "WHERE TRUNC(B.al_date) = ?";

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
                    String alName = rs.getString("AL_NAME");
                    String alText = rs.getString("AL_TEXT");
                    Date alDate = rs.getDate("AL_DATE");
                    String date = outputDateFormat.format(alDate); // 포맷된 날짜 문자열

                    // 원하는 출력 형식으로 결과 출력
                    System.out.println("학생이름 - " + stdName);
                    System.out.println("알고리즘 이름 - " + alName);
                    System.out.println("코드 - ");
                    System.out.println(alText);
                    System.out.println("날짜 - " + date);
                    System.out.println();  // 빈 줄을 추가하여 각 레코드 구분
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("결과 출력 중 문제 발생");
            }

            // 다른 날짜 조회 여부 묻기
            System.out.println("다른 날짜를 조회하시겠습니까? (y/n)");
            String response = ConnectController.scanData();
            if (!response.equalsIgnoreCase("y")) {
                return; // 종료
            }
        }
    }

	
/*
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
                    //alText = UtilController.truncateString(alText, col3Width - 3);

                 // 데이터 행 출력
                   System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                           stdName, alName, alText, team);
                    
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	*/

	// 알고리즘 팀별로 보기 수정버전
	public void displayAlgorithmByTeam() {
	    String teamName = null;
	    String sql;
	    PreparedStatement pstmt;
	    ResultSet rs;

	    while (true) {
	        // 팀별 보기 처리
	        System.out.println("조회할 팀 이름을 입력하세요, 메뉴로 돌아가려면 0 입력 : ");
	        teamName = ConnectController.scanData();
	        
	     // 팀이름이 0이면 메서드를 종료하고 메뉴로 돌아감
	        if ("0".equals(teamName)) {
	            System.out.println("메뉴로 돌아갑니다.");
	            return;
	        }

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
	            System.out.println("pstmt set 데이터 중 문제 발생");
                continue; // 문제 발생 시 다시 팀 이름 입력 받기
	        }

	        rs = ConnectController.executePstmtQuery(pstmt);
	        if (UtilController.isNull(rs)) {
	            teamName = null;
	            System.out.println("해당 팀이 존재하지 않습니다.");
	            continue;
	        }

	        try {
	            // 결과 집합의 각 행 출력
                boolean hasData = false;
	            while (rs.next()) {
	            	hasData = true;
	                String stdName = rs.getString("STD_NAME");
	                String alName = rs.getString("AL_NAME");
	                String alText = rs.getString("AL_TEXT");
	                String team = rs.getString("TEAM_NAME");

	                // 원하는 출력 형식으로 결과 출력
	                System.out.println("학생이름 - " + stdName);
	                System.out.println("알고리즘 이름 - " + alName);
	                System.out.println("코드 - ");
	                System.out.println(alText);
	                System.out.println("팀이름 - " + team);  // 빈 줄을 추가하여 각 레코드 구분
	            }
	            
                if (!hasData) {
                    System.out.println("해당 팀의 데이터가 존재하지 않습니다.");
                }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("쿼리 실행 중 문제 발생");
	        }
	        
	     // 계속 검색할지 여부를 묻는 부분
	        UtilController.line();
            System.out.println("다른 팀을 검색하시겠습니까? (y/n): ");
            String response = ConnectController.scanData();
            if (!response.equalsIgnoreCase("y")) {
                return; // 종료
            }
	    }
	}

	
	
	/*
	// insert // 알고리즘 번호 및 코드 기입
	public void insertAlgorithmAnswer(StudentVO stdvo) {
		String sql = "INSERT INTO " + algorithmVo.getClassName() + " ( AL_TEXT, STD_NO, AL_NO ) VALUES (?,?,?)";
		int result = -1;
		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
			//System.out.println("알고리즘 코드 : ");
			//String algorithmText = ConnectController.scanData();
			System.out.println("알고리즘 코드를 입력하세요 (종료하려면 'END' 입력):");
            String algorithmText = getAlgorithmTextFromInput();
//			System.out.println("알고리즘 번호 : ");
//			int algorithmNo = ConnectController.scanIntData();
            
            System.out.println("알고리즘 번호를 입력하세요 (메뉴로 돌아가려면 0 입력):");
			String algorithmNo = ConnectController.scanData();
			
			// 퀴즈 번호가 0이면 메서드를 종료하고 메뉴로 돌아감
	        if ("0".equals(algorithmNo)) {
	            System.out.println("메뉴로 돌아갑니다.");
	            return;
	        }
			
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
	*/
	
	public void insertAlgorithmAnswer(StudentVO stdvo) {
	    String sql = "INSERT INTO " + algorithmVo.getClassName() + " (AL_TEXT, STD_NO, AL_NO) VALUES (?,?,?)";
	    int result = -1;

	    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
	        // 알고리즘 텍스트 입력 받기
	        System.out.println("알고리즘 코드를 입력하세요 (종료하려면 'END' 입력):");
	        String algorithmText = getAlgorithmTextFromInput();

	        // 유효한 알고리즘 번호를 받을 때까지 루프
	        while (true) {
	            System.out.println("알고리즘 번호를 입력하세요 (메뉴로 돌아가려면 0 입력):");
	            String algorithmNo = ConnectController.scanData();

	            // 메뉴로 돌아가기
	            if ("0".equals(algorithmNo)) {
	                System.out.println("메뉴로 돌아갑니다.");
	                return; // 메서드 종료
	            }

	            // 알고리즘 번호 유효성 검사
	            if (isValidAlgorithmNo(algorithmNo)) {
	                try {
	                    int stdNo = stdvo.getStd_no();

	                    pstmt.setString(1, algorithmText);
	                    pstmt.setInt(2, stdNo);
	                    pstmt.setString(3, algorithmNo);

	                    // 디버깅용: 쿼리와 파라미터 출력
//	                    System.out.println("Executing query: " + pstmt.toString());
//	                    System.out.println("Parameters: " + algorithmText + ", " + stdNo + ", " + algorithmNo);

	                    result = ConnectController.executePstmtUpdate(pstmt);
	                    System.out.println(result + "개 입력완료");

	                    if (!UtilController.isNegative(result)) {
	                        ConnectController.commit();
	                    }
	                    break; // 성공적으로 입력이 완료되면 루프 종료
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            } else {
	                // 유효하지 않은 알고리즘 번호 입력 시 메시지 출력
	                System.out.println("유효하지 않은 알고리즘 번호입니다. 다시 시도하세요.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// 알고리즘 번호 유효성 검사 메서드
	private boolean isValidAlgorithmNo(String algorithmNo) {
	    String validationQuery = "SELECT 1 FROM " + algorithmNameVo.getClassName() + " WHERE AL_NO = ?";
	    try (PreparedStatement validationPstmt = ConnectController.getPstmt(validationQuery)) {
	        validationPstmt.setString(1, algorithmNo);
	        ResultSet rs = validationPstmt.executeQuery();
	        return rs.next(); // 결과가 있으면 true, 없으면 false
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // 예외 발생 시 false 반환
	}


	
	private String getAlgorithmTextFromInput() {
        Scanner scanner = new Scanner(System.in);
        StringBuilder algorithmText = new StringBuilder();
        String line;
        
        while (true) {
            line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            algorithmText.append(line).append("\n");
        }
        
        return algorithmText.toString();
    }

	// select all //모든 데이터 선택
	public void selectAlgorithmAnswer(StudentVO stdVo) {
		String sql = "SELECT AL_TEXT, STD_NO, AL_NO FROM " + algorithmVo.getClassName() + " WHERE STD_NO = ?";

		try  {
			PreparedStatement pstmt = ConnectController.getPstmt(sql);
			pstmt.setInt(1, stdVo.getStd_no());
			ResultSet rs = ConnectController.executePstmtQuery(pstmt);
			// 열 너비 정의 (예상 데이터 크기에 따라 조정)
			int col1Width = 35; // AL_TEXT 용
			int col2Width = 20; // STD_NO 용
			int col3Width = 20; // AL_NO 용

//			// 헤더 출력
//			System.out.printf("%-" + col1Width + "s%" + col2Width + "s%" + col3Width + "s\n", "알고리즘 코드", "제출자 번호",
//					"알고리즘 번호");

			// 결과 집합의 각 행 출력
			while (rs.next()) {
				String alText = rs.getString("AL_TEXT");
				int stdNo = rs.getInt("STD_NO");
				int alNo = rs.getInt("AL_NO");

//				// 긴 문자열 자르기
//				alText = UtilController.truncateString(alText, col1Width - 3);
//
//				// 데이터 행 출력
//				System.out.printf("%-" + col1Width + "s%" + col2Width + "d%" + col3Width + "d\n", alText, stdNo, alNo);
				
				// 원하는 출력 형식으로 결과 출력
	            System.out.println("알고리즘 코드 - ");
	            System.out.println(alText);
	            System.out.println("학생 번호 - " + stdNo);
	            System.out.println("알고리즘 번호 - " + alNo);
	            System.out.println();  // 빈 줄을 추가하여 각 레코드 구분
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertAlgorithm(StudentVO stdvo) {
	    String sql = "INSERT INTO " + algorithmNameVo.getClassName() + " (AL_URL, AL_NAME, STD_NO) VALUES (?,?,?)";
	    int result = -1;

	    try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
	        // 알고리즘 주소 입력 받기
	        String algorithmUrl = null;
	        while (algorithmUrl == null || algorithmUrl.trim().isEmpty()) {
	            System.out.println("알고리즘 주소를 입력하세요 (메뉴로 돌아가려면 0 입력):");
	            algorithmUrl = ConnectController.scanData();

	            // 알고리즘 주소가 0이면 메서드를 종료하고 메뉴로 돌아감
	            if ("0".equals(algorithmUrl)) {
	                System.out.println("메뉴로 돌아갑니다.");
	                return;
	            }
	            
	            if (algorithmUrl.trim().isEmpty()) {
	                System.out.println("알고리즘 주소는 비어 있을 수 없습니다. 다시 입력하세요.");
	                algorithmUrl = null; // 재입력을 위해 null로 설정
	            }
	        }

	        // 알고리즘 이름 입력 받기
	        String algorithmName = null;
	        while (algorithmName == null || algorithmName.trim().isEmpty()) {
	            System.out.println("알고리즘 이름을 입력하세요 (메뉴로 돌아가려면 0 입력):");
	            algorithmName = ConnectController.scanData();

	            // 알고리즘 이름이 0이면 메서드를 종료하고 메뉴로 돌아감
	            if ("0".equals(algorithmName)) {
	                System.out.println("메뉴로 돌아갑니다.");
	                return;
	            }
	            
	            if (algorithmName.trim().isEmpty()) {
	                System.out.println("알고리즘 이름은 비어 있을 수 없습니다. 다시 입력하세요.");
	                algorithmName = null; // 재입력을 위해 null로 설정
	            }
	        }

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

	
	
/*
	// insert 문제 출제 // 알고리즘 주소, 알고리즘 이름, 담당자번호
	public void insertAlgorithm(StudentVO stdvo) {
		String sql = "INSERT INTO " + algorithmNameVo.getClassName() + " (AL_URL, AL_NAME, STD_NO) VALUES (?,?,?)";
		int result = -1;
		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
			//System.out.println("알고리즘 주소 : ");
			System.out.println("알고리즘 주소를 입력하세요 (메뉴로 돌아가려면 0 입력):");
			String algorithmUrl = ConnectController.scanData();
			
			// 알고리즘 주소가 0이면 메서드를 종료하고 메뉴로 돌아감
			if ("0".equals(algorithmUrl)) {
	            System.out.println("메뉴로 돌아갑니다.");
	            return;
	        }
			
			System.out.println("알고리즘 이름 : (메뉴로 돌아가려면 0 입력)");
			String algorithmName = ConnectController.scanData();
			if ("0".equals(algorithmName)) {
	            System.out.println("메뉴로 돌아갑니다.");
	            return;
	        }
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
*/
	
	
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