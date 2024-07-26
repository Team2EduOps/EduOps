package com.team2.eduops.view;

import com.team2.eduops.controller.ConnectController;
import com.team2.eduops.controller.PageController;
import com.team2.eduops.controller.AttendanceUpdater;
// 프로젝트 전체 돌리는 위치 <- 다 만들어진 메소드들 실행만!
public class MainEntry {

	public static void main(String[] args) {
		// 프로그램 실행
		PageController pc = new PageController();
		AttendanceUpdater au = new AttendanceUpdater();

		ConnectController.connect();
		au.attendUpdate();
		pc.runPage();
	}

}
