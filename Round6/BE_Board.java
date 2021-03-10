public class BE_Board
{
    public static int[][] storage =  new int[19][19]; // 돌의 착수정보를 저장하는 부분

/*
    <착수정보에 대한 정수화 코드>
    0 : 판에 착수된 돌이 존재하지 않음
    1 : 경기자1
    2 : 경기자2
    3 : 경기 시작 전 심판이 임의로 배치한 적돌
*/

    // 기본생성자 -> 착수정보 저장공간에 대한 값 초기화
    public BE_Board()
    {
        for(int i=0 ; i<19 ; i++) {
            for (int j = 0; j < 19; j++)
                storage[i][j] = 0;
        }
        // -> 모든 착수정보를 0로 초기화(착수된 돌이 없음을 의미하는 정수화 코드)
    }

    // 전달받은 착수정보를 바탕으로 해당좌표에 착수를 진행함
    // -> 좌표에 이해를 쉽게하기 위해서 고의적으로 좌표에 할당하는 변수명을 역으로 할당
    public static void put(int code, int y, int x)
    {
        if(code == 1) // 경기자1에 의한 착수
            storage[y][x] = 1;
        else if(code == 2) // 경기자2에 의한 착수
            storage[y][x] = 2;
        else if(code == 3) // 심판에 의한 착수(심판이 임의로 착수하는 6개 이상의 적돌)
            storage[y][x] = 3;
        else
        	System.out.println("input Error!");
    }

    // 현재까지 진행된 착수상황을 출력해줌
    public static void showStatus()
    {
        for(int i=0 ; i<19 ; i++){
            for(int j=0 ; j<19 ; j++)
                System.out.print(storage[i][j] + " ");
            System.out.println(""); // 다음줄을 표현하기 위해서 개행
        }
        // -> 다음과 같이 착수상황을 출력하여 디버깅 간에 이를 참고할 수 있도록 유도함
    }
  
    // 백엔드 단에서 해당 부분이 비었는 지를 확인하여 결과를 반환함
    public static boolean isEmpty(int i, int j)
    {
    	// 해당 백엔드 단에 저장된 정수화 코드가 0이 아니다? = 착수된 돌이 있음을 의미함
    	if(storage[i][j] != 0)
    		return false;
    	else
    		return true;
    }
    
    // 현재 백엔드 단의 정보를 참고하여 현재 승패의 여부를 판단
    // -> 인자로 착수한 경기자의 정보와 가장 최근에 착수한 좌표를 전달받음
    public static int judgement(int pcode, int y, int x)
    {
    	// System.out.println("전달 값 : " + pcode + " : " + y + " : " + x);
    	int[] dir = {0,0,0,0,0,0,0,0}; // 8개의 방향값을 저장하는 정수형 배열
    	
    	// 12시 방향
		for(int k=0 ; (y-k >= 0) && (storage[y-k][x] == pcode) ; k++)
			if(k>0) dir[0]++;
		// 1시 방향
		for(int k=0 ; (x+k < 19 && y-k >= 0) && (storage[y-k][x+k] == pcode) ; k++)
			if(k>0) dir[1]++;
		// 3시방향
		for(int k=0 ; (x+k < 19) && (storage[y][x+k] == pcode) ; k++)
			if(k>0) dir[2]++;
		// 5시 방향
		for(int k=0 ; (y+k < 19 && x+k < 19) && (storage[y+k][x+k] == pcode) ; k++)
			if(k>0) dir[3]++;
		// 6시 방향 
		for(int k=0 ; (y+k < 19) && (storage[y+k][x] == pcode) ; k++)
			if(k>0) dir[4]++;
		// 7시 방향
		for(int k=0 ; (x-k >= 0 && y+k < 19) && (storage[y+k][x-k] == pcode) ; k++)
			if(k>0) dir[5]++;
		// 9시 방향
		for(int k=0 ; (x-k >= 0) && (storage[y][x-k] == pcode) ; k++)
			if(k>0) dir[6]++;
		// 11시 방향
		for(int k=0 ; (y-k >= 0 && x-k >= 0) && (storage[y-k][x-k] == pcode)  ; k++)
			if(k>0) dir[7]++;
		

		// 산출값 모니터링 -> 각 방향별로 카운트 값 모니터링
		//System.out.println(""); // 개행 - 가독성을 위한
		//System.out.printf("%d %d %d\n", dir[7], dir[0], dir[1]);
		//System.out.printf("%d C %d\n",  dir[6],         dir[2]);
		//System.out.printf("%d %d %d\n", dir[5], dir[4], dir[3]);
		//System.out.println(""); // 개행 - 가독성을 위한


		// 승패가 결정된 경우 승리한 경기자의 돌의 색상코드값을 반환하고
		// 승패가 결정되지 않은 경우 -1을 반환하도록 함
		if(dir[0]+dir[4] == 5 || dir[1]+dir[5] == 5 || dir[2]+dir[6] == 5 || dir[3]+dir[7] == 5)
		{
			if(pcode == 1)
				return 1; // 경기자 1 승리
			else if(pcode == 2)
				return 2; // 경기자 2 승리
		}
		return -1; // 승부가 나지 않은 경우
    } 
    
    // 현재 까지 입력된 모든 착수정보를 초기화
    public void reset()
    {
    	for(int i=0 ; i<19 ; i++) {
            for (int j = 0; j < 19; j++)
                storage[i][j] = 0;
        }
    }
    
    // 현재까지 저장된 착수정보를 저장한 배열을 반환
    public static int[][] getBoard()
    {
    	return storage;
    }
}