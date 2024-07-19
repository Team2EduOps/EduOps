package com.team2.eduops.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

		if (ConnectController.isNull(rs)) {
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
			System.out.println("문제발생");
			e.getMessage();
		}

		return stdVo;
	}
}
