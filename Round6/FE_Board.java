import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.SystemColor;

public class FE_Board
{
	public static final int black = 1; // 흑색을 의미하는 정수형 코드 상수
	public static final int white = 2; // 백색을 의미하는 정수형 코드 상수
	public static final int red = 3; // 심판이 임의로 착수하는 적돌을 의미 상수
	public static final int empty = 0; // 아무것도 착수되지 않은 공간을 의미하는 상수
	
	// 화면을 구성하는 프레임, 패널, 버튼들과 같은 구성요소들
	public static JFrame mainFrame;         // 대국화면이 출력되는 메인 프레임
	public static JPanel BoardPanel;        // 바둑판 이미지를 담는 패널 -> 화면에 바둑판 이미지를 출력해줌
	public static JPanel rightPanel;        // 대국과 관련된 정보를 출력하는 패널
	public static JLabel timeLabel;         // 매 착수에 대한 제한시간 정보를 출력해주는 레이블
	public static JLabel stoneImage;        // 바둑돌의 이미지를 출력해주는 레이블
	public static JLabel stoneText;         // 바둑돌에 대한 Text를 출력해주는 테이블
	public static JButton confirm;          // 심판에 의해 착수되는 적색돌에 대한 버튼
	public static JButton restart;          // 게임 재시작 버튼
	public static JButton autoMode;         // 자동착수기능 수행 버튼
	public static JButton alphaColorButton; // 인공지능의 돌의 색을 설정하는 버튼  
	public static JButton[][] putStone;     // 바둑판을 구성하는 버튼들을 저장하는 이차원 배열
	
	// 반복문, 카운트에 필요한 정수형 변수들
	public static int remainTime = 60; // 남은 시간을 저장하는 공간
	public static int redStone = 0; // 심판이 착수한 적돌의 수
	public static int i, j; // 반복문 변수
	
	// 경기진행의 순서를 통제하는 매우 중요한 변수
	public static int counter = -1; // 착수한 횟수값을 저장
	public static int timerCount = 0; // 타이머 제어를 위한 카운트에 사용되는 정수형 변수
	
	// 반복문 또는 조건문에 사용되는 boolean 변수
	public static boolean trouble; // 경기자가 착수한 돌이 없음을 의미하는 변수 -> 나중에 돌의 색상 결정에 활용되는 변수에 해당함
	
	// 착수하는 돌을 표현하기 위해서 사용하는 돌의 이미지 파일의 경로를 기술
	// -> 나중에 프로젝트 파일을 공유하여 다른환경에서도 바로 사용할 수 있도록 절대경로가 아닌 상대경로를 사용하였음
	public static String filepath = "./img/Red.png"; // 착수하는 돌의 이미지 파일의 경로를 저장
	
	// 타이머 사용을 위한 타이머 변수 -> 시간을 측정하여 정해진 시간내로 사용자가 착수하도록 행동을 제한시킴
	public static Timer t; // 타이머값 전달을 위한 매개변수
	
	// 인공지능이 사용할 돌의 색을 지정하는 정수형 변수 -> 아주 중요한 변수
	public static int alphaStoneColor = 1; // 인공지능이 사용할 돌의 색상

	
	
	// main 메소드 -> 프로그램의 전체적인 흐름을 기술
	public static void main(String[] args)
	{
		// 그래픽적 요소를 담아주고 윈도우를 생성하는 역할을 하는 메인 프레임 선언
		mainFrame = new JFrame();  // 메인 프레임 선언
		mainFrame.getContentPane().setLayout(null); // 별도의 레이아웃을 사용하지 않음 -> 사용자가 원하는 곳에 컴포넌트들을 배치하도록 함
		
		// 바둑판 이미지가 들어가는 BoardPanel 생성 및 이미지 추가
		BoardPanel = new BoardPanel(new ImageIcon("./img/BoardImage.png").getImage()); // 판 이미지가 들어가는 패널
		BoardPanel.setLayout(new GridLayout(19,19,0,0)); // 버튼들의 배치를 위해서 해당 패널내부는 19X19 형식의 그리드 레이아웃을 사용함
		
		// 바둑판 이미지 우측에 위치하는 대국정보를 출력하는 패널을 생성
		rightPanel = new JPanel(); // 대국정보를 출력하는 패널생성
		rightPanel.setLayout(null);
		rightPanel.setBackground(SystemColor.textHighlight); // 패널의 배경색상 설정
		rightPanel.setBounds(801, 0, 400, 820); // 좌표 및 크기를 지정하여 해당 패널을 메인프레임에서 BoardPanel의 우측에 배치
		
		// 남은 착수제한 시간 정보를 출력하는 레이블 생성
		timeLabel = new JLabel(); // 남은시간정보를 출력하는 레이블 생성
		timeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 60)); // 해당레이블에 사용되는 텍스트의 폰트 및 사이즈 설정
		timeLabel.setText("00:00:" + remainTime); // 남은 시간을 출력하는 레이블 세팅
		timeLabel.setLayout(null); // 별도의 레이아웃을 지정하지 않음 -> 사용자가 의도한 데로 배치할 수 있도록
		timeLabel.setBounds(55, 146, 283, 73); // 좌표 및 크기를 지정하여 right panel의 내부에 오도록 배치
		
		// 대국을 재시작하도록 해주는 버튼 생성
		restart = new JButton("대국 재시작"); // 대국을 재시작 해주는 버튼
		restart.setBounds(237, 729, 123, 48); // 별도의 값을 전달하여 세부좌표, 사이즈 설정
		
		// 적돌 배치를 마친다는 명령을 전달하는 버튼생성
		confirm = new JButton("적돌 배치완료"); // 적돌배치를 마침을 의미하는 버튼
		confirm.setBounds(45, 729, 123, 48); // 별도의 값을 전달하여 세부좌표, 사이즈 설정
		
		// 생성하는 361개의 버튼을 저장하는 2차원 배열
		putStone = new JButton[19][19]; // 바둑판과 연계하여 배치되는 버튼들을 해당 배열에 저장함
		
		// 현재순서인 돌의 이미지를 표시해주는 레이블
		stoneImage = new JLabel("");
		stoneImage.setIcon(new ImageIcon(filepath));
		stoneImage.setBounds(45, 52, 100, 100);
		rightPanel.add(stoneImage);
		
		// 현재순서에 대한 간략한 설명 텍스트를 출력해주는 역할을 하는 레이블
		stoneText = new JLabel("심판이 적돌을 착수합니다.");
		stoneText.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		stoneText.setBounds(99, 52, 242, 93);
		rightPanel.add(stoneText);
	
		// 생성과 설정을 마친 버튼들을 패널에 추가
		rightPanel.add(restart); // 대국 재시작 버튼 추가
		rightPanel.add(confirm); // 확인버튼 추가
		rightPanel.add(timeLabel); // 레이블 추가

		SetOk set = new SetOk();
		confirm.addActionListener(set);
		
		// 버튼을 생성하고 각각에 해당하는 액션리스너를 할당해줌
		PutStones put; // 버튼에 대한 액션리스너 선언
		for(i=0 ; i<19 ; i++) {
			for(j=0 ; j<19 ; j++) {
				putStone[i][j] = new JButton(); // 각 배열의 요소에 Label을 넣어서 초기화
				put = new PutStones(i, j);
				mainFrame.getContentPane().add(putStone[i][j]);
				putStone[i][j].setBorderPainted(false);
				putStone[i][j].addActionListener(put);
			}
		}
		
		// 패널에 바둑판에 대응하는 버튼들을 추가
        for(i=0 ; i<19 ; i++) {
        	for(j=0 ; j<19 ; j++) {
        		BoardPanel.add(putStone[i][j]); // 361개의 버튼을 추가함
        	}
        }
		
		// 메인프레임에 대한 부가적인 설정
		mainFrame.setSize(1200, 820); // 메인프레임 크기설정
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기버튼을 통해서 프레임을 완전히 종료할 수 있도록 설정
		mainFrame.getContentPane().add(BoardPanel);  // 메인 프레임에 바둑판 이미지가 있는 패널추가
		mainFrame.getContentPane().add(rightPanel); // 메인 프레임에 rightPanel 추가(대국정보를 표시해주는 패널)
		
		// autoMode -> AI모드에 대한 부가적인 설정을 수행하는 부분
		autoMode = new JButton("Alpha6 착수");
		autoMode.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		autoMode.setBounds(45, 275, 315, 133);
		AutoModeSet ai = new AutoModeSet(); // 액션리스너 객체 생성
		autoMode.addActionListener(ai); // 해당 버튼에 대한 액션리스너 지정
		rightPanel.add(autoMode); // 해당버튼을 우측패널에 추가
		
		// 인공지능이 사용할 돌의 색을 지정하는 버튼
		AlphaColorDecisicon acd = new AlphaColorDecisicon();
		alphaColorButton = new JButton("Alpha6 흑돌"); // 기본값을 흑돌로 할당함
		alphaColorButton.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		alphaColorButton.setBounds(45, 451, 315, 133);
		alphaColorButton.addActionListener(acd);
		rightPanel.add(alphaColorButton);
	
        
        mainFrame.setVisible(true); // 프레임이 창에 출력되도록 함
	}
	
	// 바둑판 이미지를 담고 있는 패널 -> 바둑판 이미지를 출력함
    public static class BoardPanel extends JPanel
    {
        private Image board; // 불러온 바둑판의 이미지를 저장함
        public BoardPanel(Image img) {
            this.board = img;
            setSize(new Dimension(img.getWidth(null), img.getHeight(null)));
        }
        public void paintComponent(Graphics g) {
            g.drawImage(board, 0, 0, null);
        }
    }
    
    // 인공지능이 사용할 돌의 색을 설정하는 부분
    // -> 해당 버튼을 클릭한 경우 수행하는 액션리스너 정의
    public static class AlphaColorDecisicon implements ActionListener
    {
    	@Override
    	public void actionPerformed(ActionEvent ae)
    	{
    		// 버튼을 클릭한 경우 현재 설정한 돌의 색상을 바꿔주는 방식으로 구현
    		
    		// 현재 설정된 값이 흑돌인 경우 -> 백돌로 변경
    		if(alphaStoneColor == black)
    		{
    			alphaStoneColor = white; // 백돌로 변경
    			alphaColorButton.setText("Alpha6 백돌");
    		}
    		// 현재 설정된 값이 백돌인 경우 -> 흑돌로 변경
    		else if(alphaStoneColor == white)
    		{
    			alphaStoneColor = black; // 흑돌로 변경
    			alphaColorButton.setText("Alpha6 흑돌");
    		}
    		// 정의된 범위 밖의 값으로 초기값을 가진 경우
    		else
    		{
    			System.out.println("Error! : Invalid value(alphaStoneColor)");
    			System.exit(-1); // 오류메시지 출력 후 프로그램 강제종료
    		}
    	}
    }
    
    // 인공지능으로 인해 자동으로 착수가 이루어지도록 하는 부분
    // -> 인터페이스에서 인공지능의 색을 선택하여 지정할 수 있는 방향으로 생각해보도록 하자
    public static class AutoModeSet implements ActionListener
    {
    	@Override
    	public void actionPerformed(ActionEvent ae)
    	{
    		// 현재 설정되어 있는 인공지능의 돌 색상값을 인자로 전달함
    		Alpha6.launch(alphaStoneColor); // 해당부분의 수를 바꿔서 인공지능이 가질 돌 색상을 전달
    	}
    }
   
    // 바둑판 위에 위치하는 버튼들의 입력에 대한 리스너를 정의
    public static class PutStones implements ActionListener
    {
    	// 내부적으로 사용하는 반복문 변수
    	// 버튼을 2차원 배열의 인덱스로 접근하는 데 필요한 2개의 정수형 변수
    	public int i;
    	public int j;
    	
    	// 기본생성자 -> 2개의 정수형 변수를 전달받은 인자로 초기화함
    	public PutStones(int i, int j)
    	{
    		this.i = i;
    		this.j = j;
    	}
    	
    	// 돌을 착수하는 버튼을 클릭한 경우에 대한 액션 리스너 정의
    	// 제한시간을 통한 통제를 위한 관련 구문들이 추가되어 있음
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// 착수하려는 지점이 비어있는 공간인지 확인
			if(!BE_Board.isEmpty(i, j)) {
				JOptionPane.showMessageDialog(mainFrame, "이미 착수된 지점입니다. 착수할 수 없습니다.");
				return;
			}
			
			// 심판이 임의의 적돌을 배치하는 경우
			if(counter == -1) {
				// 판에 놓인 적돌의 수가 6개 이하인 경우
				putStone[i][j].setIcon(new ImageIcon(filepath)); // 아이콘 설정
				BE_Board.put(3, i, j); // 백엔드 단에 착수관련 정보 전달 - 심판이 놓는 임의의 적돌
				redStone++; // 착수한 적돌의 수를 1증가 -> 나중에 적돌 입력 확인 시켜주는 부분에서 활용할 수 있도록 하자
			}
			
			// 처음인 경우 -> 흑돌이 1개만 착수하도록 한다.
			else if(counter == 1) {
				// 대국정보를 표시해주는 패널에 현재 순서에 대한 정보를 업데이트
//				stoneImage.setIcon(new ImageIcon(filepath));
//				stoneImage.repaint(); // 변경된 내용이 나타나도록 업데이트
//				stoneText.setText("흑돌 순서 입니다.");
				
				// 착수하는 위치의 버튼의 배경이미지를 현재 순서의 돌 색깔로 변경함
				// -> 처음인 경우는 무조건 흑돌만이 수행할 수 있으므로 색깔을 흑돌로 지정하여서 사용하여도 무방!
				putStone[i][j].setIcon(new ImageIcon(filepath));
				
				
				BE_Board.put(1, i, j); // 백엔드 단에 착수관련 정보 전달
				counter++;             // 순서를 제어하기 위한 카운터값 1증가
				timerCount++;          // 타이머를 통제하기 위한 카운터값 1증가
				
				// 흑돌의 경우 처음에 한개만 착수하면 되므로 다음과 같이 조건문이 할당됨
				// 1개의 돌 착수가 완료된 경우 -> 타이머를 종료 후 재실행하고(초기화), timerCount 변수도 다시 0으로 초기화함
				// -> 그 이후에 착수에서도 다시 활용할 수 있도록 구성하기 위함
				if(timerCount == 1) {
					resetTime(t);
					t = startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
					timerCount = 0; // 타이머 카운트 값 초기화
				}
			}
			
			// 그 이후 -> 각 돌별로 2개씩 번갈아가면서 둔다.
			// -> counter 값이 짝수인 경우 -> 착수권이 다른 경기자에게 넘어갔음을 의미함
			else if(counter%2 == 0) {
				// 돌의 색을 변경하는 부분 -> 돌의 색에 따라서 백엔드 단에서 나중에 착수자를 인식하게 됨
				// -> 순서가 바뀌어야하므로 현재 유지되고 있던 돌의 색상을 반전시킴
				if(trouble == false)
				{
					if(filepath.equals("./img/Black.png"))
						filepath = "./img/White.png";
					else if(filepath.equals("./img/White.png"))
						filepath = "./img/Black.png";
				}
				
				
				// 현재순서의 돌을 보여주는 부분 업데이트
				stoneImage.setIcon(new ImageIcon(filepath));
				stoneImage.repaint(); // 변경된 내용이 나타나도록 업데이트
				
				// 현재순서의 돌을 설명해주는 텍스트를 업데이트 해주는 부분
				if(filepath.equals("./img/Black.png"))
					stoneText.setText("흑돌 순서 입니다.");
				else
					stoneText.setText("백돌 순서 입니다.");
				stoneText.repaint(); // 변경한 내용이 반영되어 나타나도록 업데이트
				
				// 착수하게 되는 위치 버튼의 배경이미지를 해당하는 순서의 돌 이미지로 변경
				putStone[i][j].setIcon(new ImageIcon(filepath));
				
				// 생성된 정보를 백엔드 단에 전송하여 반영하도록 함
				// 각 조건문은 돌의 색에 대한 구문으로만 나눈 것이고 그 이외의 로직은 모두 동일하게 적용함.
				// -> 현재 순서가 흑돌인 경우
				if(filepath.equals("./img/Black.png")) {
					BE_Board.put(1, i, j); // 백엔드 단에 착수관련 정보 전달
					counter++;
					timerCount++; // 타이머 카운트의 값 1증가
					if(BE_Board.judgement(1, i, j) == 1) {
						resetTime(t); // 시간값을 초기화함
						// -> 경기가 종료되었으므로 더 이상의 카운트는 의미를 가지지 않음
						JOptionPane.showMessageDialog(mainFrame, "흑돌 승리!");
					}
				}
				// 현재 순서가 백돌인 경우
				else if(filepath.equals("./img/White.png")) {
					BE_Board.put(2, i, j); // 백엔드 단에 착수관련 정보 전
					counter++;
					timerCount++; // 타이머 카운트의 값 1증가
					if(BE_Board.judgement(2, i, j) == 2) {
						resetTime(t); // 시간값을 초기화함
						// -> 경기가 종료 되었으므로 더 이상의 카운트는 의미를 가지지 않음
						JOptionPane.showMessageDialog(mainFrame, "백돌 승리!");
					}
				}
			}
			// 그 이외의 경우 -> 현재의 색을 유지하면서 착수를 진행한다.
			// 두 개의 돌 중 두번째 돌을 착수하는 경우
			else
			{
//				// 현재 순서에 대한 정보를 출력해주는 부분 내용 업데이트
//				stoneImage.setIcon(new ImageIcon(filepath));
//				stoneImage.repaint(); // 변경된 내용이 나타나도록 업데이트
//				if(filepath.equals("./img/Black.png"))
//					stoneText.setText("흑돌 순서 입니다.");
//				else
//					stoneText.setText("백돌 순서 입니다.");
//				stoneText.repaint(); // 변경한 내용이 반영되어 나타나도록 업데이트
				
				// 착수하려는 위치 버튼의 배경이미지를 해당하는 순서의 돌의 색 이미지로 변경함
				putStone[i][j].setIcon(new ImageIcon(filepath));
				
				// 착수하는 위치정보를 백엔드 단으로 전달하는 부분
				// 두 조건문은 돌의 색깔에 따라서 처리를 구분한 것일뿐
				// -> 논리적으로는 모두 서로 동일한 부분에 해당함
				
				// 흑돌인 경우
				if(filepath.equals("./img/Black.png")) {
					BE_Board.put(1, i, j); // 백엔드 단에 착수관련 정보 전달
					resetTime(t);    // 해당순서에 대한 착수가 완료되었으므로 타이머를 종료함
					trouble = false; // 정상적인 처리가 되었으므로 trouble 값을 false로 변경
					counter++;       // 순서진행에 대한 정보반영을 위해서 counter이 값을 1증가
					timerCount++;    // 타이머 카운트의 값을 1증가
					
					if(timerCount == 2) {
						resetTime(t); // 착수를 마쳤으므로 기존에 실행되고 있던 타이머를 종료함
						t = startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
						timerCount = 0; // 타이머 카운트 값 초기화
					}
					
					// 착수 후에 승패에 대한 여부를 판정함
					if(BE_Board.judgement(1, i, j) == 1) {
						resetTime(t); // 승패가 결정되었으므로 더 이상 타이머를 작동시킬 필요가 없음 시간값을 초기화함
						// -> 경기가 종료 되었으므로 더 이상의 카운트는 의미를 가지지 않음
						JOptionPane.showMessageDialog(mainFrame, "흑돌 승리!");
					}
				}
				// 순서가 백돌인 경우
				else if(filepath.equals("./img/White.png")) {
					BE_Board.put(2, i, j); // 백엔드 단에 착수관련 정보 전달
					resetTime(t); // 해당순서에 대한 착수가 완료되었으므로 타이머를 종료함
					trouble = false; // 정상적인 처리가 되었으므로 trouble 값을 false로 변경
					counter++;
					timerCount++; // 타이머카운트의 값 1증가
					
					if(timerCount == 2) {
						resetTime(t);
						t = startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
						timerCount = 0; // 타이머 카운트 값 초기화
					}
					
					// 착수를 마쳤으므로 이에 대한 승패판정을 수행하는 부분
					if(BE_Board.judgement(2, i, j) == 2) {
						resetTime(t); // 시간값을 초기화함
						// -> 경기가 종료 되었으므로 더 이상의 카운트는 의미를 가지지 않음
						JOptionPane.showMessageDialog(mainFrame, "백돌 승리!");
					}
				}
			}
			//be_board.showStatus(); // 백엔드에 저장되는 착수정보를 확인하기 위한 부분 -> 디버깅용 코드
			//System.out.println("\n\n");
		}
    }
    
    // 착수가 시작된 경우 제한시간 카운트를 시작함
    public static Timer startTime()
    {
    	Timer timer = new Timer(); // 타이머를 사용하기 위한 타이머 객체 생성
    	TimerTask task = new TimerTask() {
    	
    		@Override
    		public void run() {
    			//System.out.println("restTime : " + remainTime);
    			remainTime--;
    			// 보기좋은 형식으로 맞춰서 출력을 진행해주기 위한 부분
    			if(remainTime >= 10) {
    				timeLabel.setForeground(Color.BLACK); // 출력되는 서체의 색상을 결정 -> 남은시간이 없음을 강조하기 위함
    				timeLabel.setText("00:00:" + remainTime); // 타임보드 최신화
    			}
    			// 한자리 수로 시간이 남은경우 -> 업데이트되는 텍스트의 형식을 일관되게 유지하도록 하기 위함
    			else if(remainTime > 5)
    				timeLabel.setText("00:00:0" + remainTime); // 타임보드 최신화
    			// 시간이 5초 미만으로 남은 경우 -> 강조를 위해서 업데이트 되는 서체의 색상을 적색으로 변경함
    			else {
    				timeLabel.setForeground(Color.RED); // 출력되는 서체의 색상을 변경 -> 남은 시간이 없음을 강조하기 위함
    				timeLabel.setText("00:00:0" + remainTime); // 타임보드 최신화
    			}
    			rightPanel.repaint(); // 변경내용을 업데이트 시키기 위한 부분
    			
    			
    			// 제한시간을 모두 소진한 경우
    			if(remainTime == 0) {
    				resetTime(t); // 타임값을 초기화함
    				// 맨 처음 흑돌이 착수하지 않은 경우
    				// -> 제한시간이 초과되었음을 알리는 경고창을 띄움
    				if(counter == 1) {
    					Object[] options = {"확인"};
    	    			int cf = JOptionPane.showOptionDialog(mainFrame, "제한시간 초과!", "주의", 
    	    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    	    					options,
    	    					options[0]);
    	    			if(cf == 0 || cf == 1) {
    	    				t = startTime(); // 타이머 재시작
        	    			counter++; // 순서가 백돌의 순서로 넘어갈 수 있도록 counter의 값을 1증가시킴
    	    			}
    				}
    				// 그 이후에 다른 돌들이 착수하는 경우에 대한 case
    				// 상대편 착수 이후 어떠한 돌도 착수하지 않은 경우
    				else if(counter%2 == 0){
    					trouble = true; // 정상적인 착수가 이뤄지지 않았음을 의미하는 정보를 저장함
    					Object[] options = {"확인"};
    	    			int cf = JOptionPane.showOptionDialog(mainFrame, "제한시간 초과!", "주의", 
    	    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    	    					options,
    	    					options[0]);
    	    			if(cf == 0 || cf == 1) {
    	    				t = startTime();
        					counter += 2; // 착수권이 상대방에게 넘어가도록 count 값 조절
    	    			}
    				}
    				// 상대편 착수 이후 오직 1개의 돌만을 착수한 경우
    				else if(counter%2 == 1) {
    					Object[] options = {"확인"};
    	    			int cf = JOptionPane.showOptionDialog(mainFrame, "제한시간 초과!", "주의", 
    	    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    	    					options,
    	    					options[0]);
    	    			if(cf == 0 || cf == 1) {
    	    				t = startTime();
        					counter++; // 착수권이 상대방에게 넘어가도록 count 값 조절
    	    			}
    				}
    				// -> Counter 값이 짝수가 되면 다음순서로 전환된다는 규칙을 활용한 것
    			}
    		}
    	};
    	timer.schedule(task, 0, 1000);
    	return timer; // 생성한 timer 값을 반환함
    }
    
    
    
    
    
    
    
    
    
    
    
    
    // 특정 조건을 만족한 경우 남은 시간을 초기화함
    // -> 해당 메소드가 호출되면 기존에 생성하여 시작한 타이머를 종료시키고
    // -> 남은 시간정보를 출력하는 레이블의 정보를 업데이트 함
    public static void resetTime(Timer timer)
    {
    	timer.cancel(); // 해당 타이머를 종료함.
    	remainTime = 60; // 남은 시간값을 다시 15로 초기화
    	timeLabel.setText("00:00:" + remainTime); // 타임보드 최신화
    	rightPanel.repaint(); // 변경내용을 업데이트 시키기 위한 부분
    }
    
    // 심판이 적돌의 배치를 마치고 누르는 버튼에 대한 액션리스너
    static class SetOk implements ActionListener
    {
    	@Override
    	public void actionPerformed(ActionEvent e)
    	{
    		// 놓은 적돌의 수가 6개 이상인 경우 -> 다음으로 넘어갈 수 있음
    		// 6개 이상이 아닌 경우 -> 메시지를 출력하고 심판이 적돌을 추가배치 할 수 있도록 유도함
    		if(redStone >= 6)
    		{
    			Object[] options = {"취소", "확인"};
    			int cf = JOptionPane.showOptionDialog(mainFrame, "적돌 배치를 확정하시겠습니까?", "확인", 
    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    					options,
    					options[0]);
    			// 확인버튼을 누른 경우 -> 대국을 시작할 지에 대한 여부를 사용자에게 질의함
    			// -> 확인버튼을 누를 경우 -> 순서를 흑돌의 순서로 넘겨주면서 대국을 시작함
    			// -> 취소버튼을 누른 경우 -> 순서가 넘어가지 않고 적돌 배치단계에 남아있음
    			if(cf == 1) {
        			int cf2 = JOptionPane.showOptionDialog(mainFrame, "대국을 시작합니다. 승인하시겠습니까?", "대국시작", 
        					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        					options,
        					options[0]);
        			// 확인버튼을 누른 경우
        			if(cf2 == 1) {
        				confirm.setEnabled(false); // 확정 후에는 사용자가 해당 버튼을 다시 클릭할 수 없도록 해당 버튼을 비활성화 함
        				filepath = "./img/Black.png"; // 적돌 배치 후에는 흑돌이 배치를 시작하므로 filepath의 값을 흑돌에 해당하는 값으로 변경함
        				
        				// -> 카운터 값을 0이 아닌 1로 변경한 이유는 순서에 대한 정보로 받아들이기 용이하게 하도록 하기 위함
        				counter = 1; // 흑돌배치로 흐름이 넘어가도록 counter값을 1로 변경
        				t = startTime(); // 제한시간 측정을 시작함 -> 사전에 설정한 시간에서 차감되기 시작함
        				// -> 해당 class 내에서 공유하는 t라는 타이머 변수에 새로운 타이머를 생성하여 할당함
        				// -> 이렇게 함으로서 다른 메소드에서 활성화한 타이머를 다른 메소드에서 접근하여 종료시키는 것이 가능함
        				
        				// 대국 정보를 출력하는 패널에 순서 정보가 업데이트 되도록 함
        				// -> 심판이 임의의 적돌 배치를 마친 후 해당 패널 정보를 업데이트 함으로서 순서가 넘어갔음을 시각적으로 보여줌
        				stoneImage.setIcon(new ImageIcon(filepath));
        				stoneImage.repaint(); // 변경된 내용이 나타나도록 업데이트
        				stoneText.setText("흑돌 순서 입니다.");
        				stoneText.repaint(); // 변경된 내용이 나타나도록 업데이트
        			}
    			}
    		}
    		// 놓은 적돌의 수가 6개 이하인 경우 -> 다음으로 넘어갈 수 없음, 경고메시지 출력 후 추가입력 유도
    		else
    		{
    			// -> 경고창을 출력하여 적돌의 배치가 완료되지 않았음을 알리고 적돌배치를 계속할 수 있도록 유도함
    			// -> 이 경우에는 대국을 시작하는 구간으로 넘어가지 않음
    			JOptionPane.showMessageDialog(mainFrame, "적돌을 6개 이상 배치하지 않았습니다.");
    		}
    	}
    }
}





