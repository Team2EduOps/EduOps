package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.team2.eduops.model.AdminVO;
import com.team2.eduops.model.StudentVO;

public class JoinController {
	public void studentJoin() {
		StudentVO stdVo = new StudentVO();
		String sql = "";
		PreparedStatement pstmt;
		ResultSet rs;
		int result = -1;

		while (true) {
			System.out.println("===학생 회원가입 페이지===");

			// ID 값 중복 체크
			while (stdVo.getStd_id() == null) {
				System.out.println("ID를 입력해주세요(최대 15자): ");
				stdVo.setStd_id(ConnectController.scanData());

				sql = "Select std_name from " + stdVo.getClassName() + " where std_id = ?";

				pstmt = ConnectController.getPstmt(sql);

				if (UtilController.isNull(pstmt)) {
					System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
					continue;
				}

				try {
					pstmt.setString(1, stdVo.getStd_id());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("문제 발생");
					continue;
				}

				rs = ConnectController.executePstmtQuery(pstmt);

				if (UtilController.isNull(rs)) {
					System.out.println("문제 발생!!");
					continue;
				}

				try {
					rs.next();
					if (rs.getString("std_name") != null) {
						stdVo.setStd_id(null);
						System.out.println("이미 있는 ID입니다.");
						System.out.println("다시 입력해주세요.");
					}
				} catch (Exception e) {
					System.out.println("아직 사용하지 않은 아이디입니다.");
					continue;
				}
			}

			System.out.println("PW를 입력해주세요(최대 20자): ");
			stdVo.setStd_pw(ConnectController.scanData());

			System.out.println("성함을 입력해주세요: ");
			stdVo.setStd_name(ConnectController.scanData());

			// 자리 번호 중복값 체크
			int tmp;
			while (stdVo.getSeat_no() == 0) {
				System.out.println("자리 번호를 입력해주세요: ");
				tmp = ConnectController.scanIntData();
				if (!UtilController.isNegative(tmp)) {
					stdVo.setSeat_no(tmp);
				} else {
					System.out.println("자리 번호를 올바르게 입력해주세요.");
					continue;
				}

				sql = "select std_name from " + stdVo.getClassName() + " where seat_no = ?";

				pstmt = ConnectController.getPstmt(sql);
				if (UtilController.isNull(pstmt)) {
					System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
					continue;
				}

				try {
					pstmt.setInt(1, stdVo.getSeat_no());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("문제 발생");
					continue;
				}

				rs = ConnectController.executePstmtQuery(pstmt);

				if (UtilController.isNull(rs)) {
					System.out.println("문제 발생!!");
					continue;
				}

				try {
					rs.next();
					if (rs.getString("std_name") != null) {
						stdVo.setSeat_no(0);
						System.out.println("다른 사람의 자리입니다.");
						System.out.println("다시 입력해주세요.");
					}
				} catch (Exception e) {
					System.out.println("아직 비어있는 자리입니다.");
				}
			}

			System.out.println("팀을 입력해주세요: ");
			stdVo.setTeam_name(ConnectController.scanData());

			// INSERT INTO STUDENT (STD_ID, STD_PW, STD_NAME, TEAM_NAME, SEAT_NO) VALUES
			// ('student1', 'password1', 'Alice', 'TeamA', 1);
			sql = "insert into " + stdVo.getClassName()
					+ " (std_id, std_pw, std_name, team_name, seat_no) values(?, ?, ?, ?, ?)";

			pstmt = ConnectController.getPstmt(sql);
			if (UtilController.isNull(pstmt)) {
				System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
				continue;
			}

			try {
				pstmt.setString(1, stdVo.getStd_id());
				pstmt.setString(2, stdVo.getStd_pw());
				pstmt.setString(3, stdVo.getStd_name());
				pstmt.setString(4, stdVo.getTeam_name());
				pstmt.setInt(5, stdVo.getSeat_no());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("문제 발생");
				continue;
			}

			result = ConnectController.executePstmtUpdate(pstmt);

			if (result == 1) {
				ConnectController.commit();
				System.out.println("회원가입이 완료되었습니다. 로그인해주세요.");
				return;
			}

			System.out.println("쿼리문 실행 중 문제가 발생했습니다.");
			System.out.println("회원가입을 다시 시도해주세요.");
			return;
		}
	}

	public void adminJoin() {
		AdminVO admVo = new AdminVO();
		String sql = "";
		PreparedStatement pstmt;
		ResultSet rs;
		int result = -1;

		while (true) {
			System.out.println("===관리자 회원가입 페이지===");

			// ID 값 중복 체크
			while (admVo.getAdm_id() == null) {
				System.out.println("ID를 입력해주세요: ");
				admVo.setAdm_id(ConnectController.scanData());

				sql = "Select adm_name from " + admVo.getClassName() + " where adm_id = ?";

				pstmt = ConnectController.getPstmt(sql);

				if (UtilController.isNull(pstmt)) {
					System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
					continue;
				}

				try {
					pstmt.setString(1, admVo.getAdm_id());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("문제 발생");
					continue;
				}

				rs = ConnectController.executePstmtQuery(pstmt);

				if (UtilController.isNull(rs)) {
					System.out.println("문제 발생!!");
					continue;
				}

				try {
					rs.next();
					if (rs.getString("adm_name") != null) {
						admVo.setAdm_id(null);
						System.out.println("이미 있는 ID입니다.");
						System.out.println("다시 입력해주세요.");
					}
				} catch (Exception e) {
					System.out.println("아직 사용하지 않은 아이디입니다.");
					continue;
				}
			}

			System.out.println("PW를 입력해주세요: ");
			admVo.setAdm_pw(ConnectController.scanData());

			System.out.println("성함을 입력해주세요: ");
			admVo.setAdm_name(ConnectController.scanData());

			// INSERT INTO ADMIN (ADM_ID, ADM_PW, ADM_NAME) VALUES ('admin1', 'adminpass1',
			// 'AdminA');
			sql = "insert into " + admVo.getClassName() + " (adm_id, adm_pw, adm_name) values(?, ?, ?)";

			pstmt = ConnectController.getPstmt(sql);
			if (UtilController.isNull(pstmt)) {
				System.out.println("제대로 동작하지 않았습니다. 다시 입력해주세요.");
				continue;
			}

			try {
				pstmt.setString(1, admVo.getAdm_id());
				pstmt.setString(2, admVo.getAdm_pw());
				pstmt.setString(3, admVo.getAdm_name());

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("문제 발생");
				continue;
			}

			result = ConnectController.executePstmtUpdate(pstmt);

			if (result == 1) {
				ConnectController.commit();
				System.out.println("회원가입이 완료되었습니다. 로그인해주세요.");
				return;
			}

			System.out.println("쿼리문 실행 중 문제가 발생했습니다.");
			System.out.println("회원가입을 다시 시도해주세요.");
			return;
		}

	}

}
