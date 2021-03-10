import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

// 자동으로 육목을 자동으로 두도록 하는 인공지능 파트
// 의사결정에 필요한 요소들을 사고과정에 따라 적절하게 다루는 파트

public class Alpha6
{
	public static int[] result = new int[2];
	// 자신의 색상정보를 정수형 코드로 전달받음
	// 다른 곳에서 해당 method를 실행하는 구조에서 1과 2 이외에 다른 값은 받을 수 없도록 설계 해줘야함.
	// 해당 method내에서 입력값 오류를 처리해주는 예외는 존재하지 않음
	
	public static void launch(int stoneCode)
	{	
		Weight_Selection ws = new Weight_Selection(stoneCode); // 가중치를 선정하는 객체를 선언
		System.out.println("알파6 착수정보 로딩시작");
		System.out.println(stoneCode + "인 경우의 실행입니다.");
		
		ws.setWeight(); // 가중치값을 연산하여 할당함
		ws.showTable(); // 산정한 가중치 값을 출력함(사용자 화면이 아닌 콘솔상 -> 디버깅, 오류 확인을 위해서 수행하는 구문)
		result = ws.transmit(); // 가장 가중치가 높은 좌표를 선정하여 좌표값을 반환
	    
	    // 판단한 결과를 바탕으로 착수를 진행하는 부분
	    // 인공지능이 흑돌이고 최초로 착수를 진행해야하는 경우
	    if(stoneCode == 1 && FE_Board.counter == 1)
	    {
	    	FE_Board.filepath = "./img/Black.png"; // 이미지 할당 시 활용해야 하는 이미지를 흑돌로 설정
	    	
	    	BE_Board.put(1, result[0], result[1]); // 백엔드 단에 착수관련 정보 전달
	    	// 착수한 정보를 화면에 업데이트할 수 있도록 해당 위치에 대응하는 버튼의 이미지를 변경함
	    	FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(FE_Board.filepath)); 
			FE_Board.counter++;    // 착수순서를 통제하기 위한 counter 변수의 값을 1증가
			FE_Board.timerCount++; // 타이머카운트의 값 1증가
			
			// 타이머 카운트의 값이 1가 된 경우 -> 할당된 1번의 착수를 모두 마친경우
			// -> 흑돌은 첫 착수에서 오직 1개의 수만을 둘 수 있기 때문에 다음과 같이 할당함
			if(FE_Board.timerCount == 1) {
				FE_Board.resetTime(FE_Board.t);    // 기존에 실행되고 있던 타이머를 종료함
				FE_Board.t = FE_Board.startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
				FE_Board.timerCount = 0;           // 타이머 카운트 값 초기화 -> 다음상대의 착수시 활용을 위해서
			}
	    	
	    }
	    // 인공지능이 흑돌이고, 첫번째 이후의 착수를 진행해야하는 경우
	    else if(stoneCode == 1 && FE_Board.counter > 1)
	    {
	    	FE_Board.filepath = "./img/Black.png"; // 이미지 할당 시 활용해야 하는 이미지를 흑돌로 설정
	    	
	    	BE_Board.put(1, result[0], result[1]); // 백엔드 단에 착수관련 정보 전달
	    	// 착수한 정보를 화면에 업데이트할 수 있도록 해당 위치에 대응하는 버튼의 이미지를 변경함
	    	FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(FE_Board.filepath)); 
			FE_Board.counter++;    // 착수순서를 통제하기 위한 counter 변수의 값을 1증가
			FE_Board.timerCount++; // 타이머카운트의 값 1증가
			
			ws.setWeight(); // 두번째 돌을 착수하기 위해 두번 째 가중치값을 연산하여 할당함
			result = ws.transmit(); // 가장 가중치가 높은 좌표를 선정하여 좌표값을 반환함
			BE_Board.put(1, result[0], result[1]); // 백엔드 단에 착수관련 정보 전달
			FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(FE_Board.filepath)); 
			FE_Board.trouble = false; // 정상적인 처리가 되었으므로 trouble 값을 false로 변경
			FE_Board.counter++;    // 착수순서를 통제하기 위한 counter 변수의 값을 1증가
			FE_Board.timerCount++; // 타이머카운트의 값 1증가
			
			// 타이머 카운트의 값이 2가 된 경우 -> 할당된 2번의 착수를 모두 마친경우
			if(FE_Board.timerCount == 2) {
				FE_Board.resetTime(FE_Board.t);    // 기존에 실행되고 있던 타이머를 종료함
				FE_Board.t = FE_Board.startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
				FE_Board.timerCount = 0;           // 타이머 카운트 값 초기화 -> 다음상대의 착수시 활용을 위해서
			}
			
			// 착수를 마쳤으므로 이에 대한 승패판정을 수행하는 부분
			if(BE_Board.judgement(1, result[0], result[1]) == 1) {
				FE_Board.resetTime(FE_Board.t); // 시간값을 초기화함
				// -> 경기가 종료 되었으므로 더 이상의 카운트는 의미를 가지지 않음
				JOptionPane.showMessageDialog(FE_Board.mainFrame, "흑돌 승리!");
			}
	    }
	    // 인공지능이 백돌이고, 첫번째 이후의 착수를 진행해야하는 경우
	    else if(stoneCode == 2 && FE_Board.counter > 1)
	    {
	    	FE_Board.filepath = "./img/White.png"; // 이미지 할당 시 활용해야 하는 이미지를 백돌로 설정
	    	
	    	BE_Board.put(2, result[0], result[1]); // 백엔드 단에 착수관련 정보 전달
	    	// 착수한 정보를 화면에 업데이트할 수 있도록 해당 위치에 대응하는 버튼의 이미지를 변경함
	    	FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(FE_Board.filepath)); 
			FE_Board.counter++;    // 착수순서를 통제하기 위한 counter 변수의 값을 1증가
			FE_Board.timerCount++; // 타이머카운트의 값 1증가
			
			ws.setWeight(); // 두번째 돌을 착수하기 위해 두번 째 가중치값을 연산하여 할당함
			result = ws.transmit(); // 가장 가중치가 높은 좌표를 선정하여 좌표값을 반환함
			BE_Board.put(2, result[0], result[1]); // 백엔드 단에 착수관련 정보 전달
			FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(FE_Board.filepath)); 
			FE_Board.trouble = false; // 정상적인 처리가 되었으므로 trouble 값을 false로 변경
			FE_Board.counter++;    // 착수순서를 통제하기 위한 counter 변수의 값을 1증가
			FE_Board.timerCount++; // 타이머카운트의 값 1증가
			
			// 타이머 카운트의 값이 2가 된 경우 -> 할당된 2번의 착수를 모두 마친경우
			if(FE_Board.timerCount == 2) {
				FE_Board.resetTime(FE_Board.t);    // 기존에 실행되고 있던 타이머를 종료함
				FE_Board.t = FE_Board.startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
				FE_Board.timerCount = 0;           // 타이머 카운트 값 초기화 -> 다음상대의 착수시 활용을 위해서
			}
			
			// 착수를 마쳤으므로 이에 대한 승패판정을 수행하는 부분
			if(BE_Board.judgement(2, result[0], result[1]) == 2) {
				FE_Board.resetTime(FE_Board.t); // 시간값을 초기화함
				// -> 경기가 종료 되었으므로 더 이상의 카운트는 의미를 가지지 않음
				JOptionPane.showMessageDialog(FE_Board.mainFrame, "백돌 승리!");
			}
	    }
	}
}
		
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//		//흑돌로서 최초로 착수를 진행하는 경우
//		if(FE_Board.counter == 1)
//		{
//			result = ws.transmit(); // 산출된 결과 좌표를 반환함
//			BE_Board.put(stoneCode, result[0], result[1]); // 결과값 전송
//			FE_Board.counter++;
//			FE_Board.timerCount++;
//			FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(filepath));
//			
//			// 흑돌의 경우 처음에 한개만 착수하면 되므로 다음과 같이 조건문이 할당됨
//			if(FE_Board.timerCount == 1) {
//				FE_Board.resetTime(FE_Board.t);
//				FE_Board.t =FE_Board. startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
//				FE_Board.timerCount = 0; // 타이머 카운트 값 초기화
//			}
//			
//			// 첫번째 수로는 경기의 승부를 결정짓는 것이 불가능 하므로 해당 경우에는 승부판정에 대한 구문이 삽입되지 않음
//		}
//		// 그 이외의 경우
//		else
//		{
//			result = ws.transmit(); // 산출된 결과 좌표를 반환함
//			BE_Board.put(stoneCode, result[0], result[1]); // 첫번째 산출좌표값 전송
//			FE_Board.counter++;
//			FE_Board.timerCount++;
//			FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(filepath));
//			
//			// 착수 후 승리여부 판단
//			result = ws.transmit(); // 두번째 좌표값을 가져옴
//			BE_Board.put(stoneCode, result[0], result[1]); // 첫번째 산출좌표값 전송
//			FE_Board.counter++;
//			FE_Board.timerCount++;
//			FE_Board.putStone[result[0]][result[1]].setIcon(new ImageIcon(filepath));
//			// 타이머 종료
//			if(FE_Board.timerCount == 2) {
//				FE_Board.t= FE_Board.startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
//				FE_Board.timerCount = 0; // 타이머 카운트 값 초기화
//			}
//			// 착수 후 승리여부 판단
//			if(BE_Board.judgement(stoneCode, result[0], result[1]) == 1) {
//				FE_Board.resetTime(FE_Board.t); // 시간값을 초기화함
//				// -> 경기가 종료되었으므로 더 이상의 카운트는 의미를 가지지 않음
//				JOptionPane.showMessageDialog(FE_Board.mainFrame, "Alpha6 승리!");
//			}
//			
//			
//			System.out.println(FE_Board.counter + " : " + FE_Board.timerCount);
//			FE_Board.counter++;
//		}