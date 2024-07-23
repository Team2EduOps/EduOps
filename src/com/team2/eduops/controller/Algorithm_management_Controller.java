package com.team2.eduops.controller;


public class Algorithm_management_Controller {                     //알고리즘 문제 선정 및 제출

	public static void almanagement() {
		
		
		while(true) {
			try {
			System.out.println("=-=-=-=-=-  알고리즘 관리 -=-=-=-=-=");
			System.out.println("\t 1. 알고리즘 문제 선정 ");
			System.out.println("\t 2. 알고리즘 제출 ");
			System.out.println("\t 3. 종료 ");
			int menuNo = ConnectController.scanIntData();
			
			switch(menuNo) {
			
			case 1: 
				Algorithm_name_Controller.menu();  // 알고리즘 문제 선정
				break;
			case 2:
				Algorithm_Controller.menu();    //알고리즘 제출 
				break;
			case 3:
                ConnectController.close();
                System.out.println("프로그램 종료합니다. ! ! ! ");
                System.exit(0);
                break;
            default:
                System.out.println("없는 번호 선택하였습니다. 1~3번 중에서 선택하세요.");
				
			}
			
			}
			catch(Algorithm_name_Controller.ReturnToMainMenuException | Algorithm_Controller.ReturnToMainMenuException e){
				//메인 메뉴로 돌아가기
				System.out.println("메인 메뉴로 돌아갑니다. ");
			}
		}
        
	}
        
	}

