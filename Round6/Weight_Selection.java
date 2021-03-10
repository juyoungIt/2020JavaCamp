import java.util.Random;

// 가중치를 설정하고 이 가중치를 반영하여 착수를 진행함

// 적용하는 원리
// -> 경기자들에 의해서 착수되는 착수정보를 백엔드로부터 읽어와서 여러가지 가중치 산정 method를 적용
// -> 이를 바탕으로 가중치 테이블을 형성함
// -> 산술적인 비교를 통해서 가중치가 가장 큰 빈공간에 착수를 하는 것으로 결정함
// -> 한 빈공간에서의 중복되는 가중치는 산술연산을 통해서 해당 연산을 수행할 수 있도록 한다.
// -> 그렇기 때문에 승리나 패배를 결정짓는 중요한 공간에 대한 가중치는 나머지 가중치에 대한 모든 합으로도 그 값을 넘을 수 없는 것으로 한다.

/*
 <해당 class에서 사용하는 가중치 테이블 값 매핑 리스트>
 0 - 아무런 가중치도 할당되지 않은 상황(garbage value)
 
 -> 승리할 수 있는 수나, 패배를 막기위한 수의 경우 나머지 가중치들을 더해서 그 값을 넘을 수 없도록 설정
 */

// 적돌 근처는 가중치를 -1 차감할 수 있도록 할 것

public class Weight_Selection
{
	public static final int empty = 0; // 착수된 돌 없이 완전히 빈 공간을 의미하는 상수
	public static final int black = 1; // 경기자에 의해서 착수된 흑돌을 의미하는 상수
	public static final int white = 2; // 경기자에 의해서 착수된 백돌을 의미하는 상수
	public static final int referee = 3; // 심판이 착수한 돌을 의미하는 상수
	public static int myStoneCode = black; // 나의 돌 색상(기본값을 흑돌로 초기화)
	public static int[][] weightTable = new int[19][19];; // 가중치를 저장하는 테이블
	public final int weightA=500; // 할당하기 위해서 설정한 가중치 값 - 1
	public final int weightB=400; // 할당하기 위해서 설정한 가중치 값 - 2
	public final int weightC=300; // 할당하기 위해서 설정한 가중치 값 - 3
	public final int weightD=200; // 할당하기 위해서 설정한 가중치 값 - 4
	
	// 기본생성자 -> 가중치 테이블 값을 초기화 -> 가중치가 할당되지 않은 상태로 유지
	public Weight_Selection(int stoneCode)
	{
		// 모든 가중치 테이블의 요소를 0으로 초기화
		for(int i=0 ; i<19 ; i++) {
			for(int j=0 ; j<19 ; j++) {
				weightTable[i][j] = 0;
			}
		}
		myStoneCode = stoneCode;
	}
	
	// 현 시점에서 생성한 showTable의 상태를 출력해줌
	// Table 정보를 시각화 해서 보여줌
	public void showTable()
	{
		System.out.println("현 시점에 대하여 생성된 가중치 테이블 입니다.");
		for(int i=0 ; i<19 ; i++) {
			for(int j=0 ; j<19 ; j++) {
				System.out.printf("%4d ", weightTable[i][j]);
			}
			System.out.println("\n"); // 개행을 위해 삽입한 구문
		}
	}
	
	// 현재까지 착수된 정보들을 바탕으로 가중치 테이블의 값을 최신화 함
	public void setWeight()
	{
		if(FE_Board.counter == 1)
			initCase();
		for(int i=0 ; i<19 ; i++) {
			for(int j=0 ; j<19 ; j++) {
				weightTable[i][j] += defender(i, j); // 수비의 입장에서 가중치를 산정
				weightTable[i][j] += attacker(i, j); // 공격의 입장에서 가중치를 산정
				//weightTable[i][j] += criticalEvent(i, j); // 경기의 승패를 결정지을 수 있는 핵심부의 가중치를 산정
				weightTable[i][j] += criticalEvent_1(i, j);
				weightTable[i][j] += Math.max(criticalEvent_2(i, j), weightTable[i][j]) ;
				weightTable[i][j] += Math.max(criticalEvent_3(i, j), weightTable[i][j]) ;
				weightTable[i][j] += Math.max(criticalEvent_4(i, j), weightTable[i][j]) ;
				weightTable[i][j] += normalEvent_1(i, j);
				weightTable[i][j] += normalEvent_2(i, j);
				weightTable[i][j] += normalEvent_3(i, j);
				weightTable[i][j] += normalEvent_4(i, j);
				weightTable[i][j] += makeLoad(i, j, myStoneCode);
				weightTable[i][j] += sevenDetection_1(i, j);
				weightTable[i][j] += sevenDetection_2(i, j);
				weightTable[i][j] += sevenDetection_3(i, j);
				weightTable[i][j] += sevenDetection_4(i, j);
				//weightTable[i][j] += longCaseDetection(i, j); // 칠목이상의 경우를 감지하여 잘못할당된 가중치를 조정함
			}
		}
	}
	
	// 흑돌로서 초기에 착수를 수행하는 경우
	public void initCase()
	{
		for(int i=6 ; i<12 ; i++) {
			for(int j=6 ; j<12 ; j++) {
				weightTable[i][j] = 20; // 중앙부로 착수할 수 있도록 가중치 값을 더하여 유도
			}
		}
	}
	
	// 수비적 입장으로 가중치를 산정함
	// 모든 가중치 산정은 빈공간을 전제로 한다.
	public int defender(int i, int j)
	{
		int criticalCount = 0; // 해당 빈공간을 기점으로 8개 방향에 존재하는 돌의 수를 합산하여 저장
		int[][] board = BE_Board.getBoard(); // 현재 저장되어 있는 착수정보를 가져옴
		
		// 해당공간이 빈공간이 아닐 경우 -> -1을 반환하며 종료
		// -> 돌이 착수된 공간에 대해서는 음수를 넣어서 이를 가중치 테이블에서 식별하기 위함이 주 목적
		if(!isEmpty(BE_Board.storage[i][j]))
		{
			return -1000;
		}

		// 해당 빈공간을 기점으로 5개의 공간을 탐색
		// 5개의 공간내에 존재하는 상대편의 돌의 수를 카운트
		
		// 3시방향
		for(int k=1 ; k<5 ; k++) {
			if(j+k>18) // 배열 범위에 대한 예외처리
				break;
			int point = board[i][j+k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 5시방향
		for(int k=1 ; k<5 ; k++) {
			if(i+k>18 || j+k>18) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i+k][j+k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 6시방향
		for(int k=1 ; k<5 ; k++) {
			if(i+k>18) // 배열범위에 대한 예외처리
				break; 
			int point = board[i+k][j]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 7시 방향
		for(int k=1 ; k<5 ; k++) {
			if(i+k > 18 || j-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i+k][j-k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 9시 방향
		for(int k=1 ; k<5 ; k++) {
			if(j-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i][j-k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 11시 방향
		for(int k=1 ; k<5 ; k++) {
			if(i-k < 0 || j-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i-k][j-k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 12시 방향
		for(int k=1 ; k<5 ; k++) {
			if(i-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i-k][j]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 1시방향
		for(int k=1 ; k<5 ; k++) {
			if(i-k < 0 || j+k > 18) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i-k][j+k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && !isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		return criticalCount; // 산정한 가중치 값을 반환
	}
	
	// 공격적인 입장에서 해당 돌의 가중치를 산정
	public int attacker(int i, int j)
	{
		int criticalCount=0; // 해당 빈공간을 기점으로 8개 방향에 존재하는 돌의 수를 합산하여 저장
		int[][] board = BE_Board.getBoard(); // 현재 저장되어 있는 착수정보를 가져옴
		
		if(!isEmpty(BE_Board.storage[i][j]))
		{
			return -1000;
		}
		
		// 해당 빈공간을 기점으로 5개의 공간을 탐색
		// 5개의 공간내에 존재하는 우리 돌의 수를 카운트
		// 3시방향
		for(int k=1 ; k<5 ; k++) {
			if(j+k>18) // 배열 범위에 대한 예외처리
				break;
			int point = board[i][j+k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 5시방향
		for(int k=1 ; k<5 ; k++) {
			if(i+k>18 || j+k>18) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i+k][j+k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 6시방향
		for(int k=1 ; k<5 ; k++) {
			if(i+k>18) // 배열범위에 대한 예외처리
				break; 
			int point = board[i+k][j]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 7시 방향
		for(int k=1 ; k<5 ; k++) {
			if(i+k > 18 || j-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i+k][j-k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 9시 방향
		for(int k=1 ; k<5 ; k++) {
			if(j-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i][j-k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 11시 방향
		for(int k=1 ; k<5 ; k++) {
			if(i-k < 0 || j-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i-k][j-k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				//criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		// 12시 방향
		for(int k=1 ; k<5 ; k++) {
			if(i-k < 0) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i-k][j]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		
		// 1시방향
		for(int k=1 ; k<5 ; k++) {
			if(i-k < 0 || j+k > 18) // 배열의 범위에 대한 예외처리
				break;
			int point = board[i-k][j+k]; // 해당위치의 값을 읽어옴
			if(isPlayerStone(point) && isMyStone(point)) {
				criticalCount++; // 카운트를 1증가
			}
//			// 탐색하는 위치의 돌이 적돌인 경우
//			if(isReferee(point))
//				criticalCount--; // 가중치의 값을 1차감함 -> 주변에 적돌이 있다면 가중치를 낮은 값으로 반영하도록 하게하기 위함
		}
		return criticalCount; // 산정한 가중치 값을 반환
	}
	
	// 해당 빈공간이 경기를 마칠 수 있는 중요한 부분인지 검출
	// -> 가장 중요한 부분이므로 가장 높은 가중치를 부여함.
	public int criticalEvent(int i, int j)
	{
		int myColor = 0; // 나의 돌 색상값
		int enemyColor = 0; // 상대편 돌의 색상값을 저장
		
		int myCount=0;    // 해당영역에서 발견된 나의 돌의 수
		int enemyCount=0; // 해당영역에서 발견된 적의 돌의 수
		int emptyCount=0; // 해당영역에서 발견된 빈공간의 수
		int redCount=0;   // 해당영역에서 발견된 적돌의 수(심판에 의하여 배치된)
		
		int criticalWeight = 0; // 식별시 할당하게 되는 가중치 값, 초기에는 0으로 초기화
		// -> 이후에 해당값에 특정조건에 부합하는 빈공간에 대해서 정해진 가중치를 더함
		
		int[][] board = BE_Board.getBoard(); // 백엔드 단에 저장되는 착수정보를 불러옴
		// -> 값의 변경이 아닌 참조가 목적이므로 다음과 같이 복사해와도 무방
		
		// 빈공간이 아닌 경우는 탐색의 대상에서 제외시킴
		// -> 이미 착수가 진행된 공간에 대한 행위 정의
		if(!isEmpty(BE_Board.storage[i][j]))
		{
			return -1000;
		}
		
		// 수행되는 상황에 따라서 적절한 색상값을 할당함
		if(myStoneCode == black) {
			myColor = black;
			enemyColor = white;
		}
		else if(myStoneCode == white) {
			myColor = white;
			enemyColor = black;
		}
		else
			System.out.println("Error! : Color Code input error");
		// -> 나의 색상에 따라서 적의 돌의 색상을 반대로 선정하여 처리함
		
		
		// 각 요소들을 6개씩 그룹화 하여서 다음을 반복함
		// 3시방향으로 탐색
		for(int x=j-5 ; x<=j ; x++)
		{
			if(x>18 || x<0)
				break;
			for(int y=x ; y<x+6 ; y++)
			{
				if(y>18 || y<0) { //범위 넘었을 때
					break;	
				}
				else if(board[i][y] == myColor) { //우리돌일때
					myCount++;
				}
				else if(board[i][y] == empty) { //빈곳을 만났을때 
					emptyCount++;
				}
				else if(board[i][y] == enemyColor){ // 상대방 돌일때
					enemyCount++;
				}
				else {
					redCount++;
				}
			}
			
			// 특정 경우에 대해서 판정을 내려서 가중치 값을 더해주는 부분
			if(myCount==5 && emptyCount==1) {
				criticalWeight += 500;
			}
			else if(myCount==4 && emptyCount==2) {
				criticalWeight += 500;
			}
			else if(enemyCount==4 && emptyCount==2) {
				criticalWeight += 100;
			}
			else if(enemyCount==5 && emptyCount==1) {
				criticalWeight += 100;
			}
			
			// 다음번 판정을 위한 초기화 파트
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
		}
		
		// 6시 방향판단
		for(int y=i-5 ; y<=i ; y++)
		{
			for(int y1=y ; y1<y+6 ; y1++) 
			{ 
				if(y1>18||y1<0) { //범위 넘었을 때
					break;
				}
				else if(board[y1][j]==myColor) { //우리돌일때
					myCount++;
				}
				else if(board[y1][j]==0) { //빈곳을 만났을때 
					emptyCount++;
				}
				else if(board[y1][j]==enemyColor){ // 상대방 돌일때
					enemyCount++;
				}
				else {
					redCount++;
				}
			}
			if(myCount==5 && emptyCount==1) {
				criticalWeight += 500;
			}
			else if(myCount==4 && emptyCount==2) { // 바로 끝낼 수 있는 공격의 수를 x축에서 발견
				criticalWeight += 500;
			}
			else if(enemyCount==4 && emptyCount==2) { //바로 수비해야하는 수를 x축에서 발견
				criticalWeight += 100;
			}
			else if(enemyCount==5 && emptyCount==1) {
				criticalWeight += 100;
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
		}
		
		
		// 5시 방향으로 탐색
		for(int x=j-5,y=i-5 ; y<=i || x<=j ; y++, x++)
		{
			for(int x1=x,y1=y ; x1<x+6 || y1<y+6 ; x1++,y1++)
			{ //6개씩
				if(y1>18 || y1<0 || x1>18 || x1<0) { //범위 넘었을 때
					break;
				}
				else if(board[x1][y1]==myColor) { //우리돌일때
					myCount++;
				}
				else if(board[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
				}
				else if(board[x1][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
				}
				else {
					redCount++;
				}
			}

			if(myCount==5&& emptyCount==1) {
				criticalWeight += 500;
			}else if(myCount==4&&emptyCount==2) { // 바로 끝낼 수 있는 공격의 수를 x축에서 발견
				criticalWeight += 500;
			}else if(enemyCount==4&&emptyCount==2) { //바로 수비해야하는 수를 x축에서 발견
				criticalWeight += 100;
			}else if(enemyCount==5&& emptyCount==1) {
				criticalWeight += 100;
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
		}
		// 오른쪽 위로 판단 

		for(int x=j-5,y=i+5 ; x<=j ||y>=i; x++,y--)
		{
			for(int x1=x , y1=y ; x1<x+6||y1>y-6 ; x1++,y1--)
			{ //6개씩
				if(y1>18||y1<0||x1>18||x1<0) { //범위 넘었을 때
					break;
				}else if(board[x1][y1]==myColor) { //우리돌일때
					myCount++;
				}else if(board[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
				}else if(board[x1][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
				}else {
					redCount++;
				}
			}
			if(myCount==5&& emptyCount==1) {
				criticalWeight += 500;
			}
			else if(myCount==4&&emptyCount==2) {
				criticalWeight += 500;
			}
			else if(enemyCount==4&&emptyCount==2) { 
				criticalWeight += 100;
			}
			else if(enemyCount==5&& emptyCount==1) {
				criticalWeight += 100;
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;		
		}
		return criticalWeight; // 산정한 가중치 값을 반환
	}
	
	
	// 육목이 아닌 칠목이상의 경우 가중치를 두지 않도록 조정해주는 부분
	// 그 이외에 다른 부분들도 커버하기 위한 기술을 하는 부분
	public static int longCaseDetection(int i, int j)
	{	
		int[][] board = new int[19][19]; // 백엔드 단에 저장된 착수정보를 불러와서 저장하는 2차원 배열
		int enemyColor = 0; // 상대방측의 돌의 색상값을 저장
		int resultValue = 0; // 결과적으로 반환해야할 값을 저장

		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		board = BE_Board.getBoard(); // 현재 백엔드 단에 저장되어 있는 착수정보를 로딩함
		
		String[] width = new String[19];      // 가로방향으로 탐색한 19개의 문자열을 저장
		String[] height = new String[19];     // 세로방향을 탐색한 19개의 문자열을 저장
		String[] leftCross = new String[38];  // 왼쪽 대각선으로 탐색한 37개의 문자열을 저장
		String[] rightCross = new String[38]; // 오른쪽 대각선으로 탐색한 37개의 문자열을 저장
		
		
		
		// 1. 가로방향으로 탐색 -> 각 라인별로 문자열 정보를 생성함
		for(int k=0 ; k<19 ; k++) {
			for(int l=0 ; l<19 ; l++) {
				if(l == 0) // 0번 인덱스부터 문자열이 들어가도록 하기 위한 부분
					width[k] = Integer.toString(board[k][l]); 
				else
					width[k] += Integer.toString(board[k][l]); // 해당위치의 착수정보를 문자열로 변환하여 저장
			}
		}
		
		// 1. 공격하는 입장
		// 생성한 String 배열을 바탕으로 탐색
		for(int k=0 ; k<19 ; k++)
		{
			// 인공지능이 흑돌을 사용하는 경우
			if(myStoneCode == black)
			{
				// 패턴1
				if(width[k].contains("1111101"))
				{
					int space = width[k].indexOf("1111101");
					weightTable[k][space+5] -= 100;
				}
				// 패턴2
				else if(width[k].contains("1111011"))
				{
					int space = width[k].indexOf("1111011");
					weightTable[k][space+4] -= 100;
				}
				// 패턴3
				else if(width[k].contains("1110111"))
				{
					int space = width[k].indexOf("1110111");
					weightTable[k][space+3] -= 100;
				}
				// 패턴4
				else if(width[k].contains("1101111"))
				{
					int space = width[k].indexOf("1101111");
					weightTable[k][space+2] -= 100;
				}
				// 패턴5
				else if(width[k].contains("1011111"))
				{
					int space = width[k].indexOf("1011111");
					weightTable[k][space+1] -= 100;
				}
				// 패턴6
				else if(width[k].contains("1111110"))
				{
					int space = width[k].indexOf("1111110");
					weightTable[k][space+6] -= 100;
				}
				// 패턴7
				else if(width[k].contains("0111111"))
				{
					int space = width[k].indexOf("0111111");
					weightTable[k][space] -= 100;
				}
			}
			// 인공지능이 백돌을 사용하는 경우
			else if(myStoneCode == white)
			{
				// 패턴1
				if(width[k].contains("2222202"))
				{
					int space = width[k].indexOf("2222202");
					weightTable[k][space+5] -= 100;
				}
				// 패턴2
				else if(width[k].contains("2222022"))
				{
					int space = width[k].indexOf("2222022");
					weightTable[k][space+4] -= 100;
				}
				// 패턴3
				else if(width[k].contains("2220222"))
				{
					int space = width[k].indexOf("2220222");
					weightTable[k][space+3] -= 100;
				}
				// 패턴4
				else if(width[k].contains("2202222"))
				{
					int space = width[k].indexOf("2202222");
					weightTable[k][space+2] -= 100;
				}
				// 패턴5
				else if(width[k].contains("2022222"))
				{
					int space = width[k].indexOf("2022222");
					weightTable[k][space+1] -= 100;
				}
				// 패턴6
				else if(width[k].contains("2222220"))
				{
					int space = width[k].indexOf("2222220");
					weightTable[k][space+6] -= 100;
				}
				// 패턴7
				else if(width[k].contains("022222"))
				{
					int space = width[k].indexOf("022222");
					weightTable[k][space] -= 100;
				}
			}
		}
		
		
		// 2. 방어하는 입장
		// 생성한 String 배열을 바탕으로 검색
		for(int k=0 ; k<19 ; k++)
		{
			// 인공지능이 흑돌을 사용하는 경우 -> 7목 이상으로 승부와 관련없는 요소를 발견 시 주변의 가중치 산정에 반영함
			if(myStoneCode == black)
			{
				// 패턴1
				if(width[k].contains("2222202"))
				{
					int space = width[k].indexOf("2222202");
					weightTable[k][space+5] -= 100;
				}
				// 패턴2
				else if(width[k].contains("2222022"))
				{
					int space = width[k].indexOf("2222022");
					weightTable[k][space+4] -= 100;
				}
				// 패턴3
				else if(width[k].contains("2220222"))
				{
					int space = width[k].indexOf("2220222");
					weightTable[k][space+3] -= 100;
				}
				// 패턴4
				else if(width[k].contains("2202222"))
				{
					int space = width[k].indexOf("2202222");
					weightTable[k][space+2] -= 100;
				}
				// 패턴5
				else if(width[k].contains("2022222"))
				{
					int space = width[k].indexOf("2022222");
					weightTable[k][space+1] -= 100;
				}
				// 패턴6
				else if(width[k].contains("2222220"))
				{
					int space = width[k].indexOf("2222220");
					weightTable[k][space+6] -= 100;
				}
				// 패턴7
				else if(width[k].contains("022222"))
				{
					int space = width[k].indexOf("022222");
					weightTable[k][space] -= 100;
				}
			}
			// 인공지능이 백돌을 사용하는 경우
			else if(myStoneCode == white)
			{
				// 패턴1
				if(width[k].contains("1111101"))
				{
					int space = width[k].indexOf("1111101");
					weightTable[k][space+5] -= 100;
				}
				// 패턴2
				else if(width[k].contains("1111011"))
				{
					int space = width[k].indexOf("1111011");
					weightTable[k][space+4] -= 100;
				}
				// 패턴3
				else if(width[k].contains("1110111"))
				{
					int space = width[k].indexOf("1110111");
					weightTable[k][space+3] -= 100;
				}
				// 패턴4
				else if(width[k].contains("1101111"))
				{
					int space = width[k].indexOf("1101111");
					weightTable[k][space+2] -= 100;
				}
				// 패턴5
				else if(width[k].contains("1011111"))
				{
					int space = width[k].indexOf("1011111");
					weightTable[k][space+1] -= 100;
				}
				// 패턴6
				else if(width[k].contains("1111110"))
				{
					int space = width[k].indexOf("1111110");
					weightTable[k][space+6] -= 100;
				}
				// 패턴7
				else if(width[k].contains("0111111"))
				{
					int space = width[k].indexOf("0111111");
					weightTable[k][space] -= 100;
				}
			}
		}
		
		
		// 2. 세로방향으로 탐색 -> 각 라인별로 문자열 정보를 생성함
		for(int k=0 ; k<19 ; k++) {
			for(int l=0 ; l<19 ; l++) {
				if(l == 0) // 0번 인덱스부터 문자열이 들어가도록 하기 위한 부분
					height[k] = Integer.toString(board[l][k]); 
				else
					height[k] += Integer.toString(board[l][k]); // 해당위치의 착수정보를 문자열로 변환하여 저장
			}
		}
		// 1. 공격하는 입장
		// 생성한 String 배열을 바탕으로 탐색
		for(int k=0 ; k<19 ; k++)
		{
			// 인공지능이 흑돌을 사용하는 경우
			if(myStoneCode == black)
			{
				// 패턴1
				if(height[k].contains("1111101"))
				{
					int space = height[k].indexOf("1111101");
					weightTable[space+5][k] -= 500;
				}
				// 패턴2
				else if(height[k].contains("1111011"))
				{
					int space = height[k].indexOf("1111011");
					weightTable[space+4][k] -= 500;
				}
				// 패턴3
				else if(height[k].contains("1110111"))
				{
					int space = height[k].indexOf("1110111");
					weightTable[space+3][k] -= 500;
				}
				// 패턴4
				else if(height[k].contains("1101111"))
				{
					int space = height[k].indexOf("1101111");
					weightTable[space+2][k] -= 500;
				}
				// 패턴5
				else if(height[k].contains("1011111"))
				{
					int space = height[k].indexOf("1011111");
					weightTable[space+1][k] -= 500;
				}
				// 패턴6
				else if(height[k].contains("1111110"))
				{
					int space = height[k].indexOf("1111110");
					weightTable[space+6][k] -= 500;
				}
				// 패턴7
				else if(height[k].contains("0111111"))
				{
					int space = height[k].indexOf("0111111");
					weightTable[space][k] -= 500;
				}
			}
			// 인공지능이 백돌을 사용하는 경우
			else if(myStoneCode == white)
			{
				// 패턴1
				if(height[k].contains("2222202"))
				{
					int space = height[k].indexOf("2222202");
					weightTable[space+5][k] -= 500;
				}
				// 패턴2
				else if(height[k].contains("2222022"))
				{
					int space = height[k].indexOf("2222022");
					weightTable[space+4][k] -= 500;
				}
				// 패턴3
				else if(height[k].contains("2220222"))
				{
					int space = height[k].indexOf("2220222");
					weightTable[space+3][k] -= 500;
				}
				// 패턴4
				else if(height[k].contains("2202222"))
				{
					int space = height[k].indexOf("2202222");
					weightTable[space+2][k] -= 500;
				}
				// 패턴5
				else if(height[k].contains("2022222"))
				{
					int space = height[k].indexOf("2022222");
					weightTable[space+1][k] -= 500;
				}
				// 패턴6
				else if(height[k].contains("2222220"))
				{
					int space = height[k].indexOf("2222220");
					weightTable[space+6][k] -= 500;
				}
				// 패턴7
				else if(height[k].contains("022222"))
				{
					int space = height[k].indexOf("022222");
					weightTable[space][k] -= 500;
				}
			}
		}		
		// 2. 방어하는 입장
		// 생성한 String 배열을 바탕으로 검색
		for(int k=0 ; k<19 ; k++)
		{
			// 인공지능이 흑돌을 사용하는 경우
			if(myStoneCode == black)
			{
				// 패턴1
				if(height[k].contains("2222202"))
				{
					int space = height[k].indexOf("2222202");
					weightTable[space+5][k] -= 500;
				}
				// 패턴2
				else if(height[k].contains("2222022"))
				{
					int space = height[k].indexOf("2222022");
					weightTable[space+4][k] -= 500;
				}
				// 패턴3
				else if(height[k].contains("2220222"))
				{
					int space = height[k].indexOf("2220222");
					weightTable[space+3][k] -= 500;
				}
				// 패턴4
				else if(height[k].contains("2202222"))
				{
					int space = height[k].indexOf("2202222");
					weightTable[space+2][k] -= 500;
				}
				// 패턴5
				else if(height[k].contains("2022222"))
				{
					int space = height[k].indexOf("2022222");
					weightTable[space+1][k] -= 500;
				}
				// 패턴6
				else if(height[k].contains("2222220"))
				{
					int space = height[k].indexOf("2222220");
					weightTable[space+6][k] -= 500;
				}
				// 패턴7
				else if(height[k].contains("022222"))
				{
					int space = height[k].indexOf("022222");
					weightTable[space][k] -= 500;
				}
			}
			// 인공지능이 백돌을 사용하는 경우
			else if(myStoneCode == white)
			{
				// 패턴1
				if(height[k].contains("1111101"))
				{
					int space = height[k].indexOf("1111101");
					weightTable[space+5][k] -= 500;
				}
				// 패턴2
				else if(height[k].contains("1111011"))
				{
					int space = height[k].indexOf("1111011");
					weightTable[space+4][k] -= 500;
				}
				// 패턴3
				else if(height[k].contains("1110111"))
				{
					int space = height[k].indexOf("1110111");
					weightTable[space+3][k] -= 500;
				}
				// 패턴4
				else if(height[k].contains("1101111"))
				{
					int space = height[k].indexOf("1101111");
					weightTable[space+2][k] -= 500;
				}
				// 패턴5
				else if(height[k].contains("1011111"))
				{
					int space = height[k].indexOf("1011111");
					weightTable[space+1][k] -= 500;
				}
				// 패턴6
				else if(height[k].contains("1111110"))
				{
					int space = height[k].indexOf("1111110");
					weightTable[space+6][k] -= 500;
				}
				// 패턴7
				else if(height[k].contains("0111111"))
				{
					int space = height[k].indexOf("0111111");
					weightTable[space][k] -= 500;
				}
			}
		}
		return resultValue;
	}
	
	public int criticalEvent_1(int curX, int curY) { // x 축 검색 -> 6목 수비, 공격 찾기
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// x 축 판단
		for(int x=curX-5;x<=curX;x++) { //6번 
			for(int x1=x ; x1 <= x+5 ; x1++) { //6개씩
				if(x1>18||x1<0) { //범위 넘었을 때
					set6+=0;
				}
				else if(board[x1][curY] == myStoneCode) { //우리돌일때
					myCount++;
					set6+=1;
				}
				else if(board[x1][curY] == 0) { //빈곳을 만났을때 
					emptyCount++;
					set6+=0;
				}
				else if(board[x1][curY] == enemyColor){ // 상대방 돌일때
					enemyCount++;
					set6+=2;
				}
				else
				{
					set6+=3;
					redCount++;
				}
				length++;
			}
			
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 55;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set6);
				//System.out.println("바로 공격할 것을 찾음");
				weight += 50;

			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return 45;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if(curX==x||curX==x+5) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set6);
					//System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight += 40;
				}
			}else if(set6=="122220"||set6=="022221") { //우리가 이미 한 쪽을 막고 있는 경우
				
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set6=""; 	
		}
		return weight;
	}
	
	public int criticalEvent_2(int curX , int curY) { // y 축 검색 -> 6목 공격, 수비 잡기
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// x 축 판단
		for(int y=curY-5;y<=curY;y++) { //6번 
			for(int y1=y;y1<=y+5;y1++) { //6개씩
				if(y1>18||y1<0) { //범위 넘었을 때
					set6+=0;
				}else if(board[curX][y1] == myStoneCode) { //우리돌일때
					myCount++;
					set6+=1;
				}else if(board[curX][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set6+=0;
				}else if(board[curX][y1] == enemyColor){ // 상대방 돌일때
					enemyCount++;
					set6+=2;
				}else {
					set6+=3;
					redCount++;
				}
				length++;
			}
			
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 55;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set6);
				//System.out.println("바로 공격할 것을 찾음");
				weight += 50;

			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return 45;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if(curY==y||curY==y+5) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set6);
					//System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight += 40;
				}
			}else if(set6=="122220"||set6=="022221") { //우리가 이미 한 쪽을 막고 있는 경우
				
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set6=""; 
		}		
		return weight;
	}
	
	public int criticalEvent_3(int curX , int curY) { // 오른쪽 아래로 검색 -> 6목 공격, 수비 잡기
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// x 축 판단
		for(int x=curX-5,y=curY-5;x<=curX&&y<=curY;x++,y++) { //6번 
			for(int x1=x,y1=y;x1<=x+5&&y1<=y+5;x1++,y1++) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
					set6+=0;
				}else if(board[x1][y1] == myStoneCode) { //우리돌일때
					myCount++;
					set6+=1;
				}else if(board[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set6+=0;
				}else if(board[x1][y1] == enemyColor){ // 상대방 돌일때
					enemyCount++;
					set6+=2;
				}else {
					set6+=3;
					redCount++;
				}
				length++;
			}
			
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 55;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set6);
				//System.out.println("바로 공격할 것을 찾음");
				weight += 50;

			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return 45;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if(curX==x&&curY==y||curX==x+5&&curY==y+5) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set6);
					//System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight += 40;
				}
			}else if(set6=="122220"||set6=="022221") { //우리가 이미 한 쪽을 막고 있는 경우
				
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set6=""; 
		}		
		return weight;
	}
	
	public int criticalEvent_4(int curX , int curY) { // 오른쪽 위로 검색 -> 6목 공격, 수비 잡기
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// x 축 판단
		for(int x=curX-5,y=curY+5;x<=curX&&y>=curY;x++,y--) { //6번 
			for(int x1=x,y1=y;x1<=x+5&&y1>=y-5;x1++,y1--) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
					set6+=0;
				}else if(board[x1][y1] == myStoneCode) { //우리돌일때
					myCount++;
					set6+=1;
				}else if(board[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set6+=0;
				}else if(board[x1][y1] == enemyColor){ // 상대방 돌일때
					enemyCount++;
					set6+=2;
				}else {
					set6+=3;
					redCount++;
				}
				length++;
			}
			
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 55;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set6);
				//System.out.println("바로 공격할 것을 찾음");
				weight += 50;

			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return 45;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if(curX==x&&curY==y||curX==x+5&&curY==y-5) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set6);
					//System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight += 40;
				}
			}else if(set6=="122220"||set6=="022221") { //우리가 이미 한 쪽을 막고 있는 경우
				
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set6=""; 
		}		
		return weight;
	}
	
	// 3개짜리 대비 ...
	public int normalEvent_1(int curX, int curY) {
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set5 = ""; // 돌 5개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// x 축 판단
		for(int x=curX-4;x<=curX;x++) { //6번 
			for(int x1=x;x1<=x+4;x1++) { //6개씩
				if(x1>18||x1<0) { //범위 넘었을 때
					set5+=0;
				}else if(board[x1][curY] == myStoneCode) { //우리돌일때
					myCount++;
					set5+=1;
				}else if(board [x1][curY] == 0) { //빈곳을 만났을때 
					emptyCount++;
					set5+=0;
				}else if(board[x1][curY] == enemyColor){ // 상대방 돌일때
					enemyCount++;
					set5+=2;
				}else {
					set5+=3;
					redCount++;
				}
				length++;
			}

			
			if(set5.equals("01110")||set5.equals("01111")||set5.equals("11110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 4;
			}else if(myCount==4&&emptyCount==1 || myCount==3 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set5);
				//System.out.println("공격 위험 감지");
				weight += 5;

			}else if(set5.equals("02220")||set5.equals("02222")||set5.equals("22220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("수비하기 " +curX+" , "+curY);
				return 7;
			}else if(enemyCount==4&&emptyCount==1||enemyCount==3 && emptyCount==2){
				if(curX==x||curX==x+4) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set5);
					//System.out.println("공격 위험 감지 "+curX + " , "+curY);
					weight += 6;
				}
			}else if(set5=="12220"||set5=="02221") { //우리가 이미 한 쪽을 막고 있는 경우
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set5=""; 
		}
		return weight;
	}
	
	public int normalEvent_2(int curX, int curY){
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		
		String set5 = ""; // 돌 5개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		

		// y 축 판단
		for(int y=curY-4;y<=curY;y++) { //6번 
			for(int y1=y;y1<=y+4;y1++) { //6개씩
				if(y1>18||y1<0) { //범위 넘었을 때
					set5+=0;
				}else if(board[curX][y1] == myStoneCode) { //우리돌일때
					myCount++;
					set5+=1;
				}else if(board[curX][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set5+=0;
				}else if(board[curX][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
					set5+=2;
				}else {
					set5+=3;
					redCount++;
				}
				length++;
			}
			
			if(set5.equals("01110")||set5.equals("01111")||set5.equals("11110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 4;
			}else if(myCount==4&&emptyCount==1 || myCount==3 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set5);
				//System.out.println("공격 위험 감지");
				weight += 5;

			}else if(set5.equals("02220")||set5.equals("02222")||set5.equals("22220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("수비하기 " +curX+" , "+curY);
				return 7;
			}else if(enemyCount==4&&emptyCount==1||enemyCount==3 && emptyCount==2){
				if(curY==y||curY==y+4) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set5);
					//System.out.println("공격 위험 감지 "+curX + " , "+curY);
					weight += 6;
				}
			}else if(set5=="12220"||set5=="02221") { //우리가 이미 한 쪽을 막고 있는 경우
				
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set5=""; 
		}
		return weight;
	}
	
	public int normalEvent_3(int curX, int curY){
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set5 = ""; // 돌 5개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// 오른쪽 아래로 판단
		for(int x=curX-4,y=curY-4;x<=curX&&y<=curY;x++,y++) { //6번 
			for(int x1=x,y1=y;x1<=x+4&&y1<=y+4;x1++,y1++) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
					set5+=0;
				}else if(board[curX][y1] == myStoneCode) { //우리돌일때
					myCount++;
					set5+=1;
				}else if(board[curX][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set5+=0;
				}else if(board[curX][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
					set5+=2;
				}else {
					set5+=3;
					redCount++;
				}
				length++;
			}
			
			if(set5.equals("01110")||set5.equals("01111")||set5.equals("11110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 4;
			}else if(myCount==4&&emptyCount==1 || myCount==3 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set5);
				//System.out.println("공격 위험 감지");
				weight += 5;

			}else if(set5.equals("02220")||set5.equals("02222")||set5.equals("22220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("수비하기 " +curX+" , "+curY);
				return 7;
			}else if(enemyCount==4&&emptyCount==1||enemyCount==3 && emptyCount==2){
				if(curX==x&&curY==y||curX==x+4&&curY==y+4) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set5);
					//System.out.println("공격 위험 감지 "+curX + " , "+curY);
					weight += 6;
				}
			}else if(set5=="12220"||set5=="02221") { //우리가 이미 한 쪽을 막고 있는 경우
				
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set5=""; 
		}
		return weight;
	}
	
	public int normalEvent_4(int curX, int curY){
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;

		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set5 = ""; // 돌 5개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;

		// 오른쪽 아래로 판단
		for(int x=curX-4,y=curY+4;x<=curX&&y>=curY;x++,y--) { //6번 
			for(int x1=x,y1=y;x1<=x+4&&y1>=y-4;x1++,y1--) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
					set5+=0;
				}else if(board[curX][y1] == myStoneCode) { //우리돌일때
					myCount++;
					set5+=1;
				}else if(board[curX][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set5+=0;
				}else if(board[curX][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
					set5+=2;
				}else {
					set5+=3;
					redCount++;
				}
				length++;
			}

			
			if(set5.equals("01110")||set5.equals("01111")||set5.equals("11110")) { //공격도 붙어서 하는게 더 좋음
				//System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return 4;
			}else if(myCount==4&&emptyCount==1 || myCount==3 && emptyCount==2) { //아무데나 공격 ~
				//System.out.print(set5);
				//System.out.println("공격 위험 감지");
				weight += 5;

			}else if(set5.equals("02220")||set5.equals("02222")||set5.equals("22220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				//System.out.println("수비하기 " +curX+" , "+curY);
				return 7;
			}else if(enemyCount==4&&emptyCount==1||enemyCount==3 && emptyCount==2){
				if(curX==x&&curY==y||curX==x+4&&curY==y-4) weight=0; // 가운데 막는데만 써도 된다. 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					//System.out.print(set5);
					//System.out.println("공격 위험 감지 "+curX + " , "+curY);
					weight += 6;
				}
			}else if(set5=="12220"||set5=="02221") { //우리가 이미 한 쪽을 막고 있는 경우
				
				//System.out.println("한쪽을 이미 우리가 막고 있는 수비");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set5=""; 
		}
		return weight;
	}
	
	public int makeLoad(int curX, int curY, int findColor){ // 공격 길목을 넓힐 수 있는 자리들을 먹기 위함
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int findCount=0;
		
		int count=0;
		
		//아래쪽 판단
		for(int y=curY+1;y<=curY+4;y++) {
			if(y>18) { //범위 넘었을 때
				break;
			}else if(board[curX][y]==findColor) { //찾았을때 
				//System.out.println("아래쪽에서 똑같은 것을 찾음!");
				findCount++;

			}else break; // 색깔 다르거나 빈곳을 만났을때 
			//System.out.println(findCount);
		}

		//System.out.println(findCount);
		
		//윗쪽 판단 
		for(int y=curY-1;curY-4<=y;y--) {
			if(y<0) { //범위 넘었을 때
				break;
			}else if(board[curX][y]==findColor) { //찾았을때 
				//System.out.println("위쪽에서 똑같은 것을 찾음!");
				findCount++;
			}else break; // 색깔 다르거나 빈곳을 만났을때 
		}
		
		//오른쪽 판단
		for(int x=curX+1;x<=curX+4;x++) {
			if(x>18) { //범위 넘었을 때
				break;
			}else if(board[x][curY]==findColor) { //찾았을때 
				//System.out.println("오른쪽에서 똑같은 것을 찾음!");
				findCount++;
			}else break; // 색깔 다르거나 빈곳을 만났을때 
		}
		
		//왼쪽 판단 
		for(int x=curX-1;curX-4<=x;x--) {
			if(x<0) { //범위 넘었을 때
				break;
			}else if(board[x][curY]==findColor) { //찾았을때 
				//System.out.println("왼쪽에서 똑같은 것을 찾음!");
				findCount++;
			}else break; // 색깔 다르거나 빈곳을 만났을때 
		}

		
		// 오른쪽 아래 대각선
		for(int x=curX+1, y=curY+1 ;x<=curX+4 && y<=curY+4 ;x++, y++) {
			if(x>18||y>18) { //범위 넘었을 때
				break;
			}else if(board[x][y]==findColor) { //찾았을때 
				findCount++;
			}else break; // 색깔 다르거나 빈곳을 만났을때 
		}
		
		//왼쪽 위 대각선
		for(int x=curX-1, y=curY-1 ; curX-4<=x && curY-4<=y ;x--, y--) {
			if(x<0||y<0) { //범위 넘었을 때
				break;
			}else if(board[x][y]==findColor) { //찾았을때 
				findCount++;
			}else break; // 색깔 다르거나 빈곳을 만났을때 
		}

		// 오른쪽 위 대각선
		for(int x=curX+1, y=curY-1 ;x<=curX+4 && y>=curY-4 ;x++, y--) {
			if(x>18||y<0) { //범위 넘었을 때
				break;
			}else if(board[x][y]==findColor) { //찾았을때 
				findCount++;
			}else break; // 색깔 다르거나 빈곳을 만났을때 
		}
		
		//왼쪽 아래 대각선
		for(int x=curX-1, y=curY+1 ; curX-4>=x && curY+4<=y ;x--, y++) {
			if(x<0||y<0) { //범위 넘었을 때
				break;
			}else if(board[x][y]==findColor) { //찾았을때 
				findCount++;
			}else break; // 색깔 다르거나 빈곳을 만났을때 
		}
		return findCount;
	}
	
	
	// 칠목 이상의 경우를 감지하여 가중치를 깍아내는 부분 -> 둘 필요가 없는 곳에 높은 가중치를 둬서
	// -> 수를 낭비하는 부분을 없애기 위한 부분
	public int sevenDetection_1(int curX, int curY) {// x 축 검색 -> 7목 감지
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set7 = ""; // 돌 7개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		
		// x 축 판단
		for(int x=curX-6;x<=curX;x++) { //6번 
			for(int x1=x;x1<=x+6;x1++) { //6개씩
				if(x1>18||x1<0) { //범위 넘었을 때
//					System.out.println("범위를 넘어감 ! ");
					set7+=0;
//					myCount++;
				}else if(board[x1][curY] == myStoneCode) { //우리돌일때
//					System.out.println("내 돌 찾음 "+ myCount +"개 ");
					myCount++;
					set7+=1;
				}else if(board[x1][curY] == 0) { //빈곳을 만났을때 
					emptyCount++;
					set7+=0;
//					System.out.println("빈 공간 찾음 ! " + emptyCount);
				}else if(board[x1][curY] == enemyColor){ // 상대방 돌일때
//					System.out.println("상대방 돌 찾음 "+ enemyCount +"개 ");
					enemyCount++;
					set7+=2;
//					System.out.println("상대방돌 찾음 ! "+ enemyCount);
				}else {
					set7+=3;
					redCount++;
				}
			}
			
			
			
			if(myCount==6&&emptyCount==1) { //칠목인 자리의 가중치를 확 떨어뜨린다. 
				weight=-weightA;
				System.out.println("우리꺼 칠목 ");
			} 
			if(enemyCount==6&&emptyCount==1) { //상대방의 칠목도 막지 않는다. 
				weight=-weightC;
				System.out.println("상대편 칠목");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set7="";
		}
		return weight;
	}
	
	public int sevenDetection_2(int curX , int curY) { // y 축 검색 -> 7목 감지
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		
		String set7 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		
		// x 축 판단
		for(int y=curY-6;y<=curY;y++) { //6번 
			for(int y1=y;y1<=y+6;y1++) { //6개씩
				if(y1>18||y1<0) { //범위 넘었을 때
//					System.out.println("범위를 넘어감 ! ");
					set7+=0;
				}else if(board[curX][y1] == myStoneCode) { //우리돌일때
//					System.out.println("내 돌 찾음 "+ myCount +"개 ");
					myCount++;
					set7+=1;
				}else if(board[curX][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set7+=0;
//					System.out.println("빈 공간 찾음 ! " + emptyCount);
				}else if(board[curX][y1]==enemyColor){ // 상대방 돌일때
//					System.out.println("상대방 돌 찾음 "+ enemyCount +"개 ");
					enemyCount++;
					set7+=2;
//					System.out.println("상대방돌 찾음 ! "+ enemyCount);
				}else {
					set7+=3;
					redCount++;
				}
			}
			if(myCount==6&&emptyCount==1) { //칠목인 자리의 가중치를 확 떨어뜨린다. 
				weight=-70;
				System.out.println("우리꺼 칠목 ");
			} 
			if(enemyCount==6&&emptyCount==1) { //상대방의 칠목도 막지 않는다. 
				weight=-70;
				System.out.println("상대편 칠목");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set7=""; 
		}		
		return weight;
	}
	
	public int sevenDetection_3(int curX , int curY) { // 오른쪽 아래로 검색 -> 7목 감지
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		
		String set7 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		

		// x 축 판단
		for(int x=curX-6,y=curY-6;x<=curX&&y<=curY;x++,y++) { //6번 
			for(int x1=x,y1=y;x1<=x+6&&y1<=y+6;x1++,y1++) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
//					System.out.println("범위를 넘어감 ! ");
					set7+=0;
//					myCount++;
				}else if(board[x1][y1] == myStoneCode) { //우리돌일때
//					System.out.println("내 돌 찾음 "+ myCount +"개 ");
					myCount++;
					set7+=1;
				}else if(board[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set7+=0;
//					System.out.println("빈 공간 찾음 ! " + emptyCount);
				}else if(board[x1][y1] == enemyColor){ // 상대방 돌일때
//					System.out.println("상대방 돌 찾음 "+ enemyCount +"개 ");
					enemyCount++;
					set7+=2;
//					System.out.println("상대방돌 찾음 ! "+ enemyCount);
				}else {
					set7+=3;
					redCount++;
				}
				length++;
			}
			if(myCount==6&&emptyCount==1) { //칠목인 자리의 가중치를 확 떨어뜨린다. 
				weight=-70;
				System.out.println("우리꺼 칠목 ");
			} 
			if(enemyCount==6&&emptyCount==1) { //상대방의 칠목도 막지 않는다. 
				weight=-70;
				System.out.println("상대편 칠목");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set7=""; 
		}		
		return weight;
	}
	
	public int sevenDetection_4(int curX , int curY) { // 오른쪽 위로 검색 -> 7목 감지
		
		int[][] board = new int[19][19];
		int enemyColor = 0; // 상대방이 가지는 돌의 색상값
		board = BE_Board.getBoard(); // 저장되어 있는 착수정보를 가져옴
		
		// 상대방의 돌의 색을 산정하는 경우
		// 우리쪽이 흑돌인 경우
		if(myStoneCode == black)
			enemyColor = white;
		// 우리쪽이 백돌인 경우
		else if(myStoneCode == white)
			enemyColor = black;
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		String set7 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// x 축 판단
		for(int x=curX-6,y=curY+6;x<=curX&&y>=curY;x++,y--) { //6번 
			for(int x1=x,y1=y;x1<=x+6&&y1>=y-6;x1++,y1--) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
//					System.out.println("범위를 넘어감 ! ");
					set7+=0;
//					myCount++;
				}else if(board[x1][y1] == myStoneCode) { //우리돌일때
//					System.out.println("내 돌 찾음 "+ myCount +"개 ");
					myCount++;
					set7+=1;
				}else if(board[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					set7+=0;
//					System.out.println("빈 공간 찾음 ! " + emptyCount);
				}else if(board[x1][y1]==enemyColor){ // 상대방 돌일때
//					System.out.println("상대방 돌 찾음 "+ enemyCount +"개 ");
					enemyCount++;
					set7+=2;
//					System.out.println("상대방돌 찾음 ! "+ enemyCount);
				}else {
					set7+=3;
					redCount++;
				}
				length++;
			}
			if(myCount==6&&emptyCount==1) { //칠목인 자리의 가중치를 확 떨어뜨린다. 
				weight=-70;
				System.out.println("우리꺼 칠목 ");
			} 
			if(enemyCount==6&&emptyCount==1) { //상대방의 칠목도 막지 않는다. 
				weight=-70;
				System.out.println("상대편 칠목");
			}
			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			set7=""; 	
		}		
		return weight;
	}
	
	
	// 해당위치의 돌이 나의 돌인가?
	public boolean isMyStone(int stone)
	{
		// 나의 돌의 색을 지정함
		if(stone == myStoneCode)
			return true;
		else
			return false;
	}
	
	// 해당 위치의 돌이 경기자의 돌인지 판정
	// -> 심판이 임의로 둔 적돌을 구분하기 위한 부분
	public boolean isPlayerStone(int stone)
	{
		// 해당위치의 돌이 흑돌 또는 백돌인 경우
		if(stone == black || stone == white)
			return true;
		else
			return false;
	}
	
	// 해당 위치가 빈공간인지 판정
	public boolean isEmpty(int stone)
	{
		if(stone == empty)
			return true;
		else
			return false;
	}
	
	// 해당 위치의 돌이 심판에 의해 착수된 적돌인지를 판정
	public boolean isReferee(int stone)
	{
		if(stone == referee)
			return true;
		else
			return false;
	}
	
	// 생성한 가중 치 값을 바탕으로 착수해야할 점의 좌표를 전송
	// 가중치가 같은 경우가 2개 이상인 경우 여기서 적절한 처리를 해줄 필요성이 있음
	public int[] transmit()
	{
		int i, j; // 반복문 변수
		int maxWeight = 0; // 가장 큰 가중치의 값을 저장함
		int sameWeight = 0; // 같은 가중치를 가지는 요소의 수를 저장
		
		int result[] = new int[2]; // 결과적으로 산출한 인덱스를 저장하는 배열
		int[][] max = new int[361][2]; // 최대 가중치를 가지는 요소의 인덱스를 저장하는 배열 
		for(i=0 ; i<19 ; i++) {
			for(j=0 ; j<19 ; j++) {
				if(maxWeight < weightTable[i][j]) {
					maxWeight = weightTable[i][j];
					max[0][0] = i;
					max[0][1] = j;
					sameWeight = 0; // 더 큰 가중치가 등장한 것이므로 weight 값을 초기화
				}
				else if(maxWeight == weightTable[i][j]) {
					sameWeight++; // 카운트를 1 증가
					max[j][0] = i;
					max[j][1] = j;
				}
			}
		}
		//같은 가중치를 가진 요소가 2개 초과인 경우
		if(sameWeight > 2 && FE_Board.counter == 1)
		{
			Random random = new Random(); // 랜덤을 사용하기 위한요소
			random.setSeed(System.currentTimeMillis()); // 랜덤시드값 설정 -> 현재시간값을 기준으로 난수를 발생시킴
			int idx = random.nextInt(sameWeight); // 저장된 인덱스 범위 내에서 랜덤수를 발생
			System.out.println("해당과정에서 발생한 난수 값 : " + idx + ", sameWeight : " + sameWeight);
			result[0] = max[idx%19][0];
			result[1] = max[idx%19][1];
			System.out.println("반환한 좌표값 : " + result[0] + " : " + result[1]);
			// 해당 인덱스 값을 전달하여 반환하도록 함 -> 같은 가중치를 가진 경우 랜덤요소를 발생시켜 착수지점을 랜덤으로 선정함
		}
		// 그 이외의 경우
		else
		{
			result[0] = max[0][0];
			result[1] = max[0][1];
		}
		result[0] = max[0][0];
		result[1] = max[0][1];
		weightTable[result[0]][result[1]] = 0; // 두번째 점의 착수를 위해 가중치가 가장 높은 점에 대한 정보를 초기화
		// 어차피 두번째 착수 후에는 가중치 정보값이 의미가 없어지기 때문에 완전히 초기화 해주거나 값을 덮어써도 문제가 되지 않는다.
		sameWeight = 0; // 좌표의 산정을 마무리 하기 전에 sameWeight 값을 초기화함
		return result; // 착수할 좌표값을 저장한 정수형 배열을 반환
	}
}
