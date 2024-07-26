package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.StudentVO;

public class UserController {
	public StudentVO getStdData(int stdNo) {
		StudentVO stdVo = new StudentVO();

		String sql = "Select * from " + stdVo.getClassName() + " where std_no = ?";

		PreparedStatement pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, stdNo);
		} catch (Exception e) {
//				e.printStackTrace();
			System.out.println("문제발생");
			e.getMessage();
		}

		ResultSet rs = ConnectController.executePstmtQuery(pstmt);

		if (UtilController.isNull(rs)) {
			System.out.println("문제발생");
			return stdVo;
		}

		try {
			rs.next();
			stdVo.setStd_no(rs.getInt("std_no"));
			stdVo.setStd_id(rs.getString("std_id"));
			stdVo.setStd_name(rs.getString("std_name"));
			stdVo.setSeat_no(rs.getInt("seat_no"));
			stdVo.setTeam_name(rs.getString("team_name"));
		} catch (Exception e) {
//				e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return stdVo;
	}

	public AdminVO getAdmData(int admNo) {
		AdminVO admVo = new AdminVO();

		String sql = "Select * from " + admVo.getClassName() + " where adm_no = ?";

		PreparedStatement pstmt = ConnectController.getPstmt(sql);
		try {
			pstmt.setInt(1, admNo);
		} catch (Exception e) {
//				e.printStackTrace();
			System.out.println("문제발생");
			e.getMessage();
		}

		ResultSet rs = ConnectController.executePstmtQuery(pstmt);

		if (UtilController.isNull(rs)) {
			System.out.println("문제발생");
			return admVo;
		}

		try {
			rs.next();
			admVo.setAdm_no(rs.getInt("adm_no"));
			admVo.setAdm_id(rs.getString("adm_id"));
			admVo.setAdm_name(rs.getString("adm_name"));
		} catch (Exception e) {
//				e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return admVo;
	}

	public void updateStdPw(StudentVO stdVo) {
		String sql;
		PreparedStatement pstmt;

		String newPw = null;
		String newPwChk = null;

		while (true) {
			// 비밀번호 확인 메소드
			if (!chkPw(stdVo)) {
				System.out.println("비밀번호 확인 시 문제가 발생했습니다.");
				continue;
			}

			while (newPw == null) {
				System.out.println("새 비밀번호를 입력해 주세요(최대 20자): ");
				newPw = ConnectController.scanData();
				System.out.println("새 비밀번호를 다시 한 번 입력해 주세요: ");
				newPwChk = ConnectController.scanData();

				if (!newPw.equals(newPwChk)) {
					newPw = null;
				}
			}

			sql = "update " + stdVo.getClassName() + " set std_pw = " + newPw + " where std_no = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setInt(1, stdVo.getStd_no());
			} catch (Exception e) {
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}

			int result = ConnectController.executePstmtUpdate(pstmt);

			if (result == 1) {
				ConnectController.commit();
				System.out.println("비밀번호 변경 성공!");
			} else {
				System.out.println("비밀번호 변경 중 문제가 발생했습니다.");
				System.out.println("다시 시도해주세요.");
			}
			return;
		}
	}

	public void updateTeam(StudentVO stdVo) {
		String newTeam = null;
		String sql;
		PreparedStatement pstmt;

		while (true) {
			// 비밀번호 확인 메소드
			if (!chkPw(stdVo)) {
				System.out.println("비밀번호 확인 시 문제가 발생했습니다.");
				continue;
			}

			// 팀 이름 값 받기
			// 변경할 팀 이름 다시 띄워 y/n
			String answer;
			while (newTeam == null) {
				System.out.println("새로운 팀 이름을 입력해 주세요.");
				newTeam = ConnectController.scanData();

				System.out.printf("변경할 팀 이름이 %s가 맞습니까? Y/N", newTeam);
				answer = ConnectController.scanData();
				if (!answer.equalsIgnoreCase("Y")) {
					newTeam = null;
				}
			}

			sql = "update " + stdVo.getClassName() + " set team_name = ? where std_no = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setString(1,  newTeam);
				pstmt.setInt(2, stdVo.getStd_no());
			} catch (Exception e) {
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}

			int result = ConnectController.executePstmtUpdate(pstmt);

			if (result == 1) {
				ConnectController.commit();
				System.out.println("팀 이름 변경 성공!");
			} else {
				System.out.println("팀 이름 변경 중 문제가 발생했습니다.");
				System.out.println("다시 시도해주세요.");
			}
		}
	}

	public void updateAdmPw(AdminVO admVo) {
		String sql;
		PreparedStatement pstmt;

		String newPw = null;
		String newPwChk = null;

		while (true) {
			// 비밀번호 확인 메소드
			if (!chkPw(admVo)) {
				System.out.println("비밀번호 확인 시 문제가 발생했습니다.");
				continue;
			}

			while (newPw == null) {
				System.out.println("새 비밀번호를 입력해 주세요(최대 20자): ");
				newPw = ConnectController.scanData();
				System.out.println("새 비밀번호를 다시 한 번 입력해 주세요: ");
				newPwChk = ConnectController.scanData();

				if (!newPw.equals(newPwChk)) {
					System.out.println("입력하신 비밀번호가 일치하지 않습니다.");
					System.out.println("다시 입력해 주세요.");
					UtilController.line();
					newPw = null;
				}
			}

			sql = "update " + admVo.getClassName() + " set adm_pw = ? where adm_no = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setString(1,  newPw);
				pstmt.setInt(2, admVo.getAdm_no());
			} catch (Exception e) {
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}

			int result = ConnectController.executePstmtUpdate(pstmt);

			if (result == 1) {
				ConnectController.commit();
				System.out.println("비밀번호 변경 성공!");
			} else {
				System.out.println("비밀번호 변경 중 문제가 발생했습니다.");
				System.out.println("다시 시도해주세요.");
			}
			return;
		}
	}

	public void updateSeatNo(StudentVO stdVo) {
		int seatNo = -1;
		String sql;
		PreparedStatement pstmt;

		while (true) {
			// 비밀번호 확인 메소드
			if (!chkPw(stdVo)) {
				System.out.println("비밀번호 확인 시 문제가 발생했습니다.");
				continue;
			}

			// 팀 이름 값 받기
			// 변경할 팀 이름 다시 띄워 y/n
			String answer;
			while (UtilController.isNegative(seatNo)) {
				System.out.println("새로운 자리 번호를 입력해 주세요.");
				seatNo = ConnectController.scanIntData();

				System.out.printf("변경할 자리 번호가 %d가 맞습니까? Y/N", seatNo);
				answer = ConnectController.scanData();
				if (!answer.equalsIgnoreCase("Y")) {
					seatNo = -1;
				}
			}

			sql = "update " + stdVo.getClassName() + " set seat_no = ? where std_no = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setInt(1,  seatNo);
				pstmt.setInt(2, stdVo.getStd_no());
			} catch (Exception e) {
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}

			int result = ConnectController.executePstmtUpdate(pstmt);

			if (result == 1) {
				ConnectController.commit();
				System.out.println("자리 번호 변경 성공!");
				return;
			} else if(result == 0) {
				System.out.println("중복된 자리 번호입니다.");
				System.out.println("다시 시도해주세요.");
			} else {
				System.out.println("자리 번호 변경 중 문제가 발생했습니다.");
				System.out.println("다시 시도해주세요.");
			}
		}
	}

	public boolean chkPw(StudentVO stdVo) {
		String sql;
		PreparedStatement pstmt;
		ResultSet rs;
		String scanStdPw;
		String dbStdPw;

		while (true) {
			System.out.println("비밀번호를 입력해주세요.");
			scanStdPw = ConnectController.scanData();

			sql = "select std_pw from " + stdVo.getClassName() + " where std_no = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setInt(1, stdVo.getStd_no());
			} catch (Exception e) {
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}

			rs = ConnectController.executePstmtQuery(pstmt);

			if (UtilController.isNull(rs)) {
				System.out.println("pstmt 실행 중 문제 발생");
				return false;
			}

			try {
				rs.next();
				dbStdPw = rs.getString("std_pw");

			} catch (Exception e) {
				System.out.println("DB에서 받아온 값 처리 중 문제 발생");
				return false;
			}

			if (!scanStdPw.equals(dbStdPw)) {
				System.out.println("비밀번호가 틀렸습니다. 다시 입력해주세요.");
				continue;
			}
			return true;
		}

	}

	public boolean chkPw(AdminVO admVo) {
		String sql;
		PreparedStatement pstmt;
		ResultSet rs;
		String scanStdPw;
		String dbStdPw;

		while (true) {
			System.out.println("비밀번호를 입력해주세요.");
			scanStdPw = ConnectController.scanData();

			sql = "select adm_pw from " + admVo.getClassName() + " where adm_no = ?";

			pstmt = ConnectController.getPstmt(sql);

			try {
				pstmt.setInt(1, admVo.getAdm_no());
			} catch (Exception e) {
				System.out.println("pstmt set데이터 중 문제 발생");
				continue;
			}

			rs = ConnectController.executePstmtQuery(pstmt);

			if (UtilController.isNull(rs)) {
				System.out.println("pstmt 실행 중 문제 발생");
				return false;
			}

			try {
				rs.next();
				dbStdPw = rs.getString("adm_pw");

			} catch (Exception e) {
				System.out.println("DB에서 받아온 값 처리 중 문제 발생");
				return false;
			}

			if (!scanStdPw.equals(dbStdPw)) {
				System.out.println("비밀번호가 틀렸습니다. 다시 입력해주세요.");
				continue;
			}
			return true;
		}

	}
}