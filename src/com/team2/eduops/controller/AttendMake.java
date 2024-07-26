package com.team2.eduops.controller;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AttendMake {
    public void initializeAllAttendance() {
        String sql = "SELECT * FROM ATTENDANCE WHERE ATTEND_DATE = TRUNC(SYSDATE)";
        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
        try {
            if (!rs.isBeforeFirst()) {
                return;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Stack<Integer> arrStdNo = new Stack<>();
        sql = "SELECT * FROM STUDENT";
        pstmt = ConnectController.getPstmt(sql);
        rs = ConnectController.executePstmtQuery(pstmt);
        try {
            while (rs.next()) {
                arrStdNo.add(rs.getInt("STD_NO"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ------------------------------------------------------------------------------
        sql = "INSERT INTO ATTENDANCE(ATTEND_CODE, ATTEND_DATE, STD_NO) VALUES(?, TRUNC(SYSDATE), ?)";
        pstmt = ConnectController.getPstmt(sql);
        int attend_code = lastAttendCode();
        try {
            while (!arrStdNo.empty()) {
                pstmt.setInt(1, attend_code++);
                pstmt.setInt(2, arrStdNo.pop());
                ConnectController.executePstmtUpdate(pstmt);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int lastAttendCode() {
        String sql = "SELECT * FROM ATTENDANCE WHERE ATTEND_CODE = (SELECT MAX(ATTEND_CODE) FROM ATTENDANCE)";
        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        ResultSet rs = ConnectController.executePstmtQuery(pstmt);
        try {
            rs.next();
            int attend_code = rs.getInt("ATTEND_CODE") + 1;
            return attend_code;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 비정상 종료
    }
}

        // ConnectController.commit();
        // ----------------------------------------------------------------
        // to check if working fine.
        // ----------------------------------------------------------------
//		sql = "SELECT * FROM ATTENDANCE WHERE ATTEND_DATE = TRUNC(SYSDATE)";
//		pstmt = ConnectController.getPstmt(sql);
//		rs = ConnectController.executePstmtQuery(pstmt);
//		try {
//			while(rs.next()) {
//				System.out.println(rs.getInt("STD_NO"));
//				System.out.println(rs.getInt("ATTEND_CODE"));
//				System.out.println(rs.getDate("ATTEND_DATE"));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

/*
//잡: 해당 시간에 수행할 과제
class TestJDKTimer extends TimerTask {
/*
    @Override
    public void run() {
        String selectQuery = "SELECT S.STD_NO, NVL(MAX(A.ATTEND_DATE), TO_DATE('1970-01-01', 'YYYY-MM-DD')) AS LAST_ATTEND_DATE " +
                "FROM STUDENT S LEFT JOIN ATTENDANCE A ON S.STD_NO = A.STD_NO " +
                "GROUP BY S.STD_NO";
        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
        ResultSet resultSet = selectStmt.executeQuery();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(30);  // 예를 들어, 지난 30일을 기준으로

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (resultSet.next()) {
            int studentNo = resultSet.getInt("STD_NO");
            LocalDate lastAttendDate = resultSet.getDate("LAST_ATTEND_DATE").toLocalDate();

            for (LocalDate date = lastAttendDate.plusDays(1); date.isBefore(today); date = date.plusDays(1)) {
                if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    String insertQuery = "INSERT INTO ATTENDANCE (ATTEND_CODE, ATTEND_DATE, STD_NO, CI_TIME, CO_TIME, ATTEND_STATUS) " +
                            "VALUES (SEQ_ATTEND_CODE.NEXTVAL, ?, ?, NULL, NULL, 0)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, date.format(formatter));
                        insertStmt.setInt(2, studentNo);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
        //수행할 job
        String sql = "INSERT INTO ATTENDANCE (STD_NO, ATTEND_DATE, ATTEND_VALUES) VALUES (?,?,?)";
        PreparedStatement pstmt = ConnectController.getPstmt(sql);
        pstmt.setInt(1,stdno);
        pstmt.setDate(2,sqlDate);
        pstmt.setTimestamp(3,timestamp);
        ConnectController.executePstmtUpdate(pstmt);
        if(ConnectController.commit()==-1) {
            System.out.println("커밋 오류");
    }
}
*/
/* **********매일 출석부data 갱신,예약하는 스케쥴러:메인에 들어가야함 *******/


/*
    //******main이 돌고 있을 때, 12:01에 결석자 insert*******
    public void insertTimer(){
            Timer t = new Timer();
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2010,Calendar.APRIL,14);
            long MillisInDay =34*60*60*1000;
            t.scheduleAtFixedRate(new TestJDKTimer(),cal.getTime(),MillisInDay);


            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();        // 최초 실행 시간
        long period = 24 * 60 * 60 * 1000;        // 24시간(86400000 밀리초) 주기
        timer.scheduleAtFixedRate(dailyTask, delay, period);        // 타이머 스케줄링

    }
*/