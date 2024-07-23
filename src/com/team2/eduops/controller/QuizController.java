package com.team2.eduops.controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.QuizNameVO;
import com.team2.eduops.model.QuizVO;
import com.team2.eduops.model.StudentVO;

public class QuizController {
	QuizVO quizVo = new QuizVO();
	QuizNameVO quizNameVo = new QuizNameVO();
	// 퀴즈 문제 등록 (퀴즈 이름, 알고리즘 주소)

	
	// 퀴즈코드 추가 메소드
	public void addQuizAnswer(StudentVO stdVo) {
		selectQuizAnswerAll();
		ConnectController.line();
		insertQuizAnswer(stdVo);
		ConnectController.line();
		selectQuizAnswerAll();
		menulist();
	}
	

	// insert // 퀴즈 이름, 담당자(관리자)
	public void insertQuizAnswer(StudentVO stdVo) {
		String sql = "INSERT INTO " + quizVo.getClassName() + " (QUIZ_TEXT, STD_NO, QUIZ_NO) VALUES (?,?,?)";

		try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
			System.out.println("퀴즈 코드, 학생 번호, 퀴즈 번호 :");
			String quizCode = ConnectController.scanData();
			int stdNo = stdVo.getStd_no();
			String quizNo = ConnectController.scanData();
			pstmt.setString(1, quizCode);
			pstmt.setInt(2, stdNo);
			pstmt.setString(3, quizNo);
			int result = ConnectController.executePstmtUpdate(pstmt);
			System.out.println(result + "개 입력완료");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
/*
	// update
	public void updateQuizAnswer(String ClassName) {
		while (true) {

			System.out.println("0 선택 ==> 업데이트 탈출합니다.");
			System.out.print("수정할 STD_NO 입력: ");

			int stdNo = ConnectController.scanIntData();

			if (stdNo == 0) {
				break;
			}

			System.out.print("수정할 QUIZ_NO 입력: ");
			int quizNo = ConnectController.scanIntData();

			System.out.println("수정할 필드를 선택하세요.");
			System.out.println("1. 퀴즈 코드");
			System.out.println("2. 제출자 번호");
			System.out.println("3. 퀴즈 번호");
			System.out.print("수정할 필드 선택: ");
			int choice = ConnectController.scanIntData();

			String sql = null;
			switch (choice) {
			case 1:
				sql = "UPDATE " + ClassName + " SET QUIZ_TEXT = ? WHERE STD_NO AND WHERE QUIZ_NO= ?";
				System.out.print("새 퀴즈 코드 입력: ");
				String newquizname = ConnectController.scanData();
				try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
					pstmt.setString(1, newquizname);
					pstmt.setInt(2, stdNo);
					pstmt.setInt(3, quizNo);
					int result = ConnectController.executePstmtUpdate(pstmt);
					System.out.println(result + "개의 레코드가 수정되었습니다.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				sql = "UPDATE " + ClassName + " SET STD_NO = ? WHERE STD_NO AND WHERE QUIZ_NO= ?";
				System.out.print("새 제출자 번호 입력: ");
				String newstdno = ConnectController.scanData();
				try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
					pstmt.setString(1, newstdno);
					pstmt.setInt(2, stdNo);
					pstmt.setInt(3, quizNo);
					int result = ConnectController.executePstmtUpdate(pstmt);
					System.out.println(result + "개의 레코드가 수정되었습니다.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				sql = "UPDATE " + ClassName + " SET QUIZ_NO = ? WHERE STD_NO AND WHERE QUIZ_NO= ?";
				System.out.print("새 퀴즈 번호 입력: ");
				String newquizno = ConnectController.scanData();
				try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
					pstmt.setString(1, newquizno);
					pstmt.setInt(2, stdNo);
					pstmt.setInt(3, quizNo);
					int result = ConnectController.executePstmtUpdate(pstmt);
					System.out.println(result + "개의 레코드가 수정되었습니다.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("잘못된 선택입니다.");
				break;
			}
		}
	}
*/
	// select all //모든 데이터 선택
	public void selectQuizAnswerAll() {
		String sql = "SELECT QUIZ_TEXT, STD_NO, QUIZ_NO FROM " + quizVo.getClassName();

		try (PreparedStatement pstmt = ConnectController.getPstmt(sql);
				ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {

			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount(); // 컬럼 수

			// 열 너비 정의 (예상 데이터 크기에 따라 조정)
			int col1Width = 35; // AL_TEXT 용
			int col2Width = 20; // STD_NO 용
			int col3Width = 20; // AL_NO 용

			// 헤더 출력
			System.out.printf("%-" + col1Width + "s%" + col2Width + "s%" + col3Width + "s\n", "퀴즈 코드", "제출자 번호",
					"퀴즈 번호");

			// 결과 집합의 각 행 출력
			while (rs.next()) {
				String quizText = rs.getString("QUIZ_TEXT");
				int stdNo = rs.getInt("STD_NO");
				int quizNo = rs.getInt("QUIZ_NO");

				// 긴 문자열 자르기
				quizText = ConnectController.truncateString(quizText, col1Width - 3);

				// 데이터 행 출력
				System.out.printf("%-" + col1Width + "s%" + col2Width + "d%" + col3Width + "d\n", quizText, stdNo,
						quizNo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	///////////////////////////
	
	// ReturnToMainMenuException 사용하여 뒤로가기
/*
	public static void  quizmanagement() {
	        while (true) {
	        	try {
	            
	            
	            // 메뉴 번호 입력받기
	            int menuNo = ConnectController.scanIntData();
	            String sql = null;
	            switch (menuNo) {
	            
	            	case 1:
	            		Quiz_name_Controller.menu();  // 알고리즘 문제 선정
	    				break;
	                case 2:

	                    break;
	                case 3:
	                    
	                    break;
	                case 4:
	                    System.out.println("프로그램 종료합니다. ! ! ! ");
	                    System.exit(0);
	            } //switch end
	        }catch(Quiz_name_Controller.ReturnToMainMenuException e){
	        	//메인 메뉴로 돌아가기
				System.out.println("메인 메뉴로 돌아갑니다. ");
	        }
	        }
	    
	    
	        public static class ReturnToMainMenuException extends RuntimeException {}
*/        
	        
	        
	        
	        public static void menulist() {
	        	
	        }
	        

	        // insert              // 퀴즈 이름, 담당자(관리자)
	        public void insertQuiz(AdminVO admVo) {
	        	
	            String sql = "INSERT INTO " + quizNameVo.getClassName() + " (QUIZ_NAME, ADM_NO) VALUES (?,?)";

	            try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
	                System.out.println("퀴즈 이름: ");
	                String quizName = ConnectController.scanData();
	                int admNo = admVo.getAdm_no();
	                pstmt.setString(1, quizName);
	                pstmt.setInt(2, admNo);
	                int result = ConnectController.executePstmtUpdate(pstmt);
	                System.out.println(result + "개 입력완료");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        
	     // update
	        public static void update(String ClassName) {
	            while (true) {

	                System.out.println("0 선택 ==> 업데이트 탈출합니다.");
	                System.out.print("수정할 Quiz_NO 입력: ");
	                int quizNo = ConnectController.scanIntData();

	                if (quizNo == 0) {
	                    break;
	                }

	                System.out.println("수정할 필드를 선택하세요.");
	                System.out.println("1. 퀴즈 이름");
	                System.out.println("2. 담당자 번호");
	                System.out.print("수정할 필드 선택: ");
	                int choice = ConnectController.scanIntData();

	                String sql = null;
	                switch (choice) {
	                    case 1:
	                        sql = "UPDATE " + ClassName + " SET QUIZ_NAME = ? WHERE QUIZ_NO = ?";
	                        System.out.print("새 퀴즈 이름 입력: ");
	                        String newquizname = ConnectController.scanData();
	                        try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
	                            pstmt.setString(1, newquizname);
	                            pstmt.setInt(2, quizNo);
	                            int result = ConnectController.executePstmtUpdate(pstmt);
	                            System.out.println(result + "개의 레코드가 수정되었습니다.");
	                        } catch (SQLException e) {
	                            e.printStackTrace();
	                        }
	                        break;
	                    case 2:
	                        sql = "UPDATE " + ClassName + " SET ADM_NO = ? WHERE QUIZ_NO = ?";
	                        System.out.print("새 담당자 번호 입력: ");
	                        String newadmno = ConnectController.scanData();
	                        try (PreparedStatement pstmt = ConnectController.getPstmt(sql)) {
	                            pstmt.setString(1, newadmno);
	                            pstmt.setInt(2, quizNo);
	                            int result = ConnectController.executePstmtUpdate(pstmt);
	                            System.out.println(result + "개의 레코드가 수정되었습니다.");
	                        } catch (SQLException e) {
	                            e.printStackTrace();
	                        }
	                        break;
	                    default:
	                        System.out.println("잘못된 선택입니다.");
	                        break;
	                }
	            }
	        }

	        
	        // select all
	        public void selectQuizAll() {
	        	
	        	String sql = "SELECT * FROM " + quizVo.getClassName();

	            try (PreparedStatement pstmt = ConnectController.getPstmt(sql);
	                 ResultSet rs = ConnectController.executePstmtQuery(pstmt)) {

	                ResultSetMetaData rsmd = rs.getMetaData();
	                int count = rsmd.getColumnCount();  // 컬럼 (필드) 개수

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
	                    quizname = ConnectController.truncateString(quizname, col2Width - 3);
	                    date = ConnectController.truncateString(date, col4Width - 3);

	                    // 데이터 출력
	                    System.out.printf("%-" + col1Width + "d%" + col2Width + "s%" + col3Width + "d%" + col4Width + "s\n",
	                            quizno, quizname, admno, date);
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        
	        public void addQuiz(AdminVO admVo) {
	        	selectQuizAll();
                ConnectController.line();
                insertQuiz(admVo);
                ConnectController.line();
                selectQuizAll();
	        }
	        
	       

	        
	        
	        
	        public void displayQuizByDate() {
	        	// 날짜 포맷 확인 및 변환
	        	SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	        	Date sqlDate = null;
	        	String sql;
	        	PreparedStatement pstmt;
	        	ResultSet rs;
	        	
                // 날짜별 보기 처리
	        	while(sqlDate==null) {
	        		System.out.println("조회할 날짜를 입력하세요 (형식: yy/MM/dd): ");
	        		String selectDate = ConnectController.scanData();
	        		
	        		try {
	        			java.util.Date utilDate = inputDateFormat.parse(selectDate);
	        			sqlDate = new Date(utilDate.getTime());
	        		} catch (ParseException e) {
	        			sqlDate = null;
	        			System.out.println("날짜 형식이 잘못되었습니다. 형식: yy/MM/dd");
	        			continue;
	        		}
	        		
	        	}

                sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, B.quiz_date " +
                      "FROM Quiz A " +
                      "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no " +
                      "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                      "WHERE TRUNC(B.quiz_date) = ?";

                pstmt = ConnectController.getPstmt(sql);
                
                try {
                    pstmt.setDate(1, sqlDate);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                rs = ConnectController.executePstmtQuery(pstmt);
                // 열 너비 정의 (예상 데이터 크기에 따라 조정)
                int col1Width = 20; // std_name 용
                int col2Width = 20; // quiz_name 용
                int col3Width = 35; // quiz_text 용
                int col4Width = 20; // date 용
                
                // 헤더 출력
                System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                		"학생이름", "퀴즈 이름", "퀴즈 내용", "날짜");
                
                // 날짜 포맷 정의
                SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
                try {
                	
                	// 결과 집합의 각 행 출력
                	while (rs.next()) {
                		String stdName = rs.getString("STD_NAME");
                		String quizName = rs.getString("QUIZ_NAME");
                		String quizText = rs.getString("QUIZ_TEXT");
                		Date quizDate = rs.getDate("QUIZ_DATE");
                		String date = dateFormat.format(quizDate); // 포맷된 날짜 문자열
                		
                		// 긴 문자열 자르기
                		quizText = ConnectController.truncateString(quizText, col3Width - 3);
                		
                		// 데이터 행 출력
                		System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                				stdName, quizName, quizText, date);
                	}
                } catch (SQLException e) {
                	e.printStackTrace();
                }
	        }
	        
	        public void displayQuizByTeam() {
	        	// 팀별 보기 처리
                System.out.println("조회할 팀 이름을 입력하세요: ");
                String teamName = ConnectController.scanData();
                String sql;
                PreparedStatement pstmt;
                ResultSet rs;

                sql = "SELECT C.std_name, B.quiz_name, A.quiz_text, C.team_name " +
                      "FROM Quiz A " +
                      "INNER JOIN QUIZ_NAME B ON A.quiz_no = B.quiz_no " +
                      "INNER JOIN STUDENT C ON A.std_no = C.std_no " +
                      "WHERE C.team_name = ?";
                
                pstmt = ConnectController.getPstmt(sql);

                try {
                    pstmt.setString(1, teamName);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                rs = ConnectController.executePstmtQuery(pstmt);
                try {
                	
                	// 열 너비 정의 (예상 데이터 크기에 따라 조정)
                	int col1Width = 20; // std_name 용
                	int col2Width = 20; // quiz_name 용
                	int col3Width = 35; // quiz_text 용
                	int col4Width = 20; // team_name 용
                	
                	// 헤더 출력
                	System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                			"학생이름", "퀴즈 이름", "퀴즈 내용", "팀이름");
                	
                	// 결과 집합의 각 행 출력
                	while (rs.next()) {
                		String stdName = rs.getString("STD_NAME");
                		String quizName = rs.getString("QUIZ_NAME");
                		String quizText = rs.getString("QUIZ_TEXT");
                		String team = rs.getString("TEAM_NAME");
                		
                		// 긴 문자열 자르기
                		quizText = ConnectController.truncateString(quizText, col3Width - 3);
                		
                		// 데이터 행 출력
                		System.out.printf("%-" + col1Width + "s %-" + col2Width + "s %-" + col3Width + "s %-" + col4Width + "s\n",
                				stdName, quizName, quizText, team);
                	}
                } catch (SQLException e) {
                	e.printStackTrace();
                }
	        }
	    
}
