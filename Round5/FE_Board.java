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
	public static int i, j; // 반복문 변수
	public static JFrame mainFrame;
	public static JPanel BoardPanel;
	public static JPanel rightPanel; // 대국과 관련된 정보를 출력하는 패널
	public static JLabel timeLabel;  // 매 착수에 대한 제한시간 정보를 출력해주는 레이블
	public static JLabel stoneImage; // 바둑돌의 이미지를 출력해주는 레이블
	public static JLabel stoneText;  // 바둑돌에 대한 Text를 출력해주는 테이블
	public static JButton[][] putStone;
	public static JButton confirm; // 확인버튼
	public static JButton restart; // 게임 재시작 버튼
	public static int counter = -1; // 착수한 횟수값을 저장
	public static String filepath = "./img/Red.png"; // 착수하는 돌의 이미지 파일의 경로를 저장
	public static BE_Board be_board = new BE_Board(); // 판에 대한 백엔드 부분객체 선언
	public static int remainTime = 60; // 남은 시간을 저장하는 공간
	public static int redStone = 0; // 심판이 착수한 적돌의 수
	public static Timer t; // 타이머값 전달을 위한 매개변수
	public static int timerCount; // 타이머 제어를 위한 카운트에 사용되는 정수형 변수
	public static boolean trouble; // 경기자가 착수한 돌이 없음을 의미하는 변수 -> 나중에 돌의 색상 결정에 활용되는 변수에 해당함
	private static JLabel lblNewLabel;
	
	public static void main(String[] args)
	{	
		mainFrame = new JFrame();  // 메인 프레임 선언
		mainFrame.getContentPane().setLayout(null);
		PutStones put;
		BoardPanel = new BoardPanel(new ImageIcon("./img/BoardImage.png").getImage()); // 판 이미지가 들어가는 패널
		BoardPanel.setLayout(new GridLayout(19,19,0,0));
		rightPanel = new JPanel(); // 대국정보를 출력하는 패널생성
		rightPanel.setBackground(SystemColor.textHighlight);
		rightPanel.setForeground(SystemColor.activeCaptionText);
		rightPanel.setBounds(801, 0, 400, 820);
		timeLabel = new JLabel(); // 남은시간정보를 출력하는 레이블 생성
		timeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 60));
		timeLabel.setLayout(null); // 별도의 레이아웃을 지정하지 않음 -> 사용자가 의도한 데로 배치할 수 있도록
		timeLabel.setBounds(55, 146, 283, 73); // 추가하는 레이블의 배치좌표 및 사이즈 결정
		
		restart = new JButton("대국 재시작"); // 대국을 재시작 해주는 버튼
		restart.setBounds(237, 729, 123, 48);
		
		confirm = new JButton("적돌 배치완료");
		confirm.setBounds(45, 729, 123, 48);
		SetOk set = new SetOk();
		confirm.addActionListener(set);
		rightPanel.setLayout(null);
		
		rightPanel.add(restart); // 대국 재시작 버튼 추가
		rightPanel.add(confirm); // 확인버튼 추가
		rightPanel.add(timeLabel); // 레이블 추가
		timeLabel.setText("00:00:00");
		putStone = new JButton[19][19]; // 버튼의 배열설정
		
		for(i=0 ; i<19 ; i++) {
			for(j=0 ; j<19 ; j++) {
				putStone[i][j] = new JButton(); // 각 배열의 요소에 Label을 넣어서 초기화
				put = new PutStones(i, j);
				mainFrame.getContentPane().add(putStone[i][j]);
				putStone[i][j].setBorderPainted(false);
				putStone[i][j].addActionListener(put);
			}
		}
		
		mainFrame.setSize(1200, 820); // 메인프레임 크기설정
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기버튼 활성화
		
		mainFrame.getContentPane().add(rightPanel);
		
		stoneImage = new JLabel("");
		stoneImage.setIcon(new ImageIcon(filepath));
		stoneImage.setBounds(45, 52, 100, 100);
		rightPanel.add(stoneImage);
		
		stoneText = new JLabel("심판이 적돌을 착수합니다.");
		stoneText.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		stoneText.setBounds(99, 52, 242, 93);
		rightPanel.add(stoneText);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("/Users/juyoungha/Desktop/측면 배경화면.jpg"));
		lblNewLabel.setBounds(0, -23, 440, 822);
		rightPanel.add(lblNewLabel);
		
        mainFrame.getContentPane().add(BoardPanel);  // 메인 프레임에 바둑판 이미지가 있는 패널추가
        // 패널에 바둑판에 대응하는 버튼들을 추가
        for(i=0 ; i<19 ; i++) {
        	for(j=0 ; j<19 ; j++) {
        		BoardPanel.add(putStone[i][j]);
        	}
        }
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
    
    // 버튼의 입력에 대한 리스너를 정의
    static class PutStones implements ActionListener
    {
    	// 내부적으로 사용하는 반복문 변수
    	public int i;
    	public int j;
    	
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
			if(!be_board.isEmpty(i, j)) {
				JOptionPane.showMessageDialog(mainFrame, "이미 착수된 지점입니다. 착수할 수 없습니다.");
				return;
			}
			// 심판이 임의의 적돌을 배치하는 경우
			if(counter == -1) {
				// 판에 놓인 적돌의 수가 6개 이하인 경우
				putStone[i][j].setIcon(new ImageIcon(filepath)); // 아이콘 설정
				be_board.put(3, i, j); // 백엔드 단에 착수관련 정보 전달 - 심판이 놓는 임의의 적돌
				redStone++; // 착수한 적돌의 수를 1증가
			}
			// 처음인 경우 -> 흑돌이 1개만 착수하도록 한다.
			else if(counter == 1) {
				stoneImage.setIcon(new ImageIcon(filepath));
				stoneImage.repaint(); // 변경된 내용이 나타나도록 업데이트
				stoneText.setText("흑돌 순서 입니다.");
				putStone[i][j].setIcon(new ImageIcon(filepath));
				be_board.put(1, i, j); // 백엔드 단에 착수관련 정보 전달
				System.out.println("좌표 : " + i + " : " + j);
				counter++;
				timerCount++;
				// 흑돌의 경우 처음에 한개만 착수하면 되므로 다음과 같이 조건문이 할당됨
				if(timerCount == 1) {
					resetTime(t);
					t = startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
					timerCount = 0; // 타이머 카운트 값 초기화
				}
			}
			// 그 이후 -> 각 돌별로 2개씩 번갈아가면서 둔다.
			else if(counter%2 == 0) {
				// 돌의 색을 변경하는 부분 -> 돌의 색에 따라서 백엔드 단에서 나중에 착수자를 인식하게 됨
				if(trouble == false)
				{
					if(filepath.equals("./img/Black.png"))
						filepath = "./img/White.png";
					else if(filepath.equals("./img/White.png"))
						filepath = "./img/Black.png";
				}
				// 현재 순서에 대한 정보를 출력해주는 부분 내용 업데이트
				stoneImage.setIcon(new ImageIcon(filepath));
				stoneImage.repaint(); // 변경된 내용이 나타나도록 업데이트
				if(filepath.equals("./img/Black.png"))
					stoneText.setText("흑돌 순서 입니다.");
				else
					stoneText.setText("백돌 순서 입니다.");
				stoneText.repaint(); // 변경한 내용이 반영되어 나타나도록 업데이트
				
				putStone[i][j].setIcon(new ImageIcon(filepath));
				if(filepath.equals("./img/Black.png")) {
					be_board.put(1, i, j); // 백엔드 단에 착수관련 정보 전달
					counter++;
					timerCount++; // 타이머 카운트의 값 1증가
					if(be_board.judgement(1, i, j) == 1) {
						resetTime(t); // 시간값을 초기화함
						// -> 경기가 종료되었으므로 더 이상의 카운트는 의미를 가지지 않음
						JOptionPane.showMessageDialog(mainFrame, "흑돌 승리!");
					}
				}
				else if(filepath.equals("./img/White.png")) {
					be_board.put(2, i, j); // 백엔드 단에 착수관련 정보 전
					counter++;
					timerCount++; // 타이머 카운트의 값 1증가
					if(be_board.judgement(2, i, j) == 2) {
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
				// 현재 순서에 대한 정보를 출력해주는 부분 내용 업데이트
				stoneImage.setIcon(new ImageIcon(filepath));
				stoneImage.repaint(); // 변경된 내용이 나타나도록 업데이트
				if(filepath.equals("./img/Black.png"))
					stoneText.setText("흑돌 순서 입니다.");
				else
					stoneText.setText("백돌 순서 입니다.");
				stoneText.repaint(); // 변경한 내용이 반영되어 나타나도록 업데이트
				
				System.out.println("trouble : " + trouble);
				putStone[i][j].setIcon(new ImageIcon(filepath));
				if(filepath.equals("./img/Black.png")) {
					be_board.put(1, i, j); // 백엔드 단에 착수관련 정보 전달
					resetTime(t); // 해당순서에 대한 착수가 완료되었으므로 타이머를 종료함
					trouble = false; // 정상적인 처리가 되었으므로 trouble 값을 false로 변경
					counter++;
					timerCount++; // 타이머 카운트의 값을 1증가
					if(timerCount == 2) {
						t= startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
						timerCount = 0; // 타이머 카운트 값 초기화
					}
					if(be_board.judgement(1, i, j) == 1) {
						resetTime(t); // 시간값을 초기화함
						// -> 경기가 종료 되었으므로 더 이상의 카운트는 의미를 가지지 않음
						JOptionPane.showMessageDialog(mainFrame, "흑돌 승리!");
					}
				}
				else if(filepath.equals("./img/White.png")) {
					be_board.put(2, i, j); // 백엔드 단에 착수관련 정보 전달
					resetTime(t); // 해당순서에 대한 착수가 완료되었으므로 타이머를 종료함
					trouble = false; // 정상적인 처리가 되었으므로 trouble 값을 false로 변경
					counter++;
					timerCount++; // 타이머카운트의 값 1증가
					if(timerCount == 2) {
						resetTime(t);
						t= startTime(); // 다음번 상대의 착수를 위해 타이머 초기화
						timerCount = 0; // 타이머 카운트 값 초기화
					}
					if(be_board.judgement(2, i, j) == 2) {
						resetTime(t); // 시간값을 초기화함
						// -> 경기가 종료 되었으므로 더 이상의 카운트는 의미를 가지지 않음
						JOptionPane.showMessageDialog(mainFrame, "백돌 승리!");
					}
				}
			}
			// be_board.showStatus(); // 백엔드에 저장되는 착수정보를 확인하기 위한 부분 -> 디버깅용 코드
			// System.out.println("\n\n");
		}
    }
    
    // 15초 제한시간 카운트를 시작함
    public static Timer startTime()
    {
    	System.out.println("타이머 시작"); // 타이머 시작을 모니터링 하기 위해서 삽입한 구문
    	Timer timer = new Timer();
    	TimerTask task = new TimerTask() {
    	
    		@Override
    		public void run() {
    			System.out.println("restTime : " + remainTime);
    			remainTime--;
    			// 보기좋은 형식으로 맞춰서 출력을 진행해주기 위한 부분
    			if(remainTime >= 10) {
    				timeLabel.setForeground(Color.BLACK); // 출력되는 서체의 색상을 결정 -> 남은시간이 없음을 강조하기 위함
    				timeLabel.setText("00:00:" + remainTime); // 타임보드 최신화
    			}
    			else if(remainTime >5)
    				timeLabel.setText("00:00:0" + remainTime); // 타임보드 최신화
    			else {
    				timeLabel.setForeground(Color.RED); // 출력되는 서체의 색상을 변경 -> 남은 시간이 없음을 강조하기 위함
    				timeLabel.setText("00:00:0" + remainTime); // 타임보드 최신화
    			}
    			rightPanel.repaint(); // 변경내용을 업데이트 시키기 위한 부분
    			// 제한시간을 모두 소진한 경우
    			if(remainTime == 0) {
    				resetTime(t); // 타임값을 초기화함
    				// 맨 처음 흑돌이 착수하지 않은 경우
    				if(counter == 1) {
    					Object[] options = {"확인"};
    	    			int cf = JOptionPane.showOptionDialog(mainFrame, "제한시간 15초를 모두 소진하였습니다. 착수권이 상대편에게 넘어갑니다.", "주의", 
    	    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    	    					options,
    	    					options[0]);
    	    			if(cf == 0 || cf == 1) {
    	    				t = startTime(); // 타이머 재시작
        	    			counter++; // 착수권이 상대편 돌로 넘어감
        	    			System.out.println("counter : " + counter); // 카운터 값을 모니터링 하기 위한 부분
    	    			}
    				}
    				// 그 이후에 다른 돌들이 착수하는 경우에 대한 case
    				// 상대편 착수 이후 어떠한 돌도 착수하지 않은 경우
    				else if(counter%2 == 0){
    					trouble = true; // 정상적인 착수가 이뤄지지 않았음을 의미하는 정보를 저장함
    					Object[] options = {"확인"};
    	    			int cf = JOptionPane.showOptionDialog(mainFrame, "제한시간 15초를 모두 소진하였습니다. 착수권이 상대편에게 넘어갑니다.", "주의", 
    	    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    	    					options,
    	    					options[0]);
    	    			if(cf == 0 || cf == 1) {
    	    				t = startTime();
        					counter += 2; // 착수권이 상대방에게 넘어가도록 count 값 조절
        					System.out.println("counter : " + counter); // 카운터 값을 모니터링 하기 위한 부분
    	    			}
    				}
    				// 상대편 착수 이후 오직 1개의 돌만을 착수한 경우
    				else if(counter%2 == 1) {
    					Object[] options = {"확인"};
    	    			int cf = JOptionPane.showOptionDialog(mainFrame, "제한시간 15초를 모두 소진하였습니다. 착수권이 상대편에게 넘어갑니다.", "주의", 
    	    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    	    					options,
    	    					options[0]);
    	    			if(cf == 0 || cf == 1) {
    	    				t = startTime();
        					counter++; // 착수권이 상대방에게 넘어가도록 count 값 조절
        					System.out.println("counter : " + counter); // 카운터 값을 모니터링 하기 위한 부분
    	    			}
    				}
    			}
    		}
    	};
    	timer.schedule(task, 0, 1000); 
    	return timer; // 생성한 timer 값을 반환함
    }
    
    // 특정 조건을 만족한 경우 남은 시간을 초기화함
    public static void resetTime(Timer timer)
    {
    	timer.cancel(); // 해당 타이머를 종료함.
    	remainTime = 16; // 남은 시간값을 다시 15로 초기화
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
    		if(redStone >= 6)
    		{
    			Object[] options = {"취소", "확인"};
    			int cf = JOptionPane.showOptionDialog(mainFrame, "적돌 배치를 확정하시겠습니까?", "확인", 
    					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
    					options,
    					options[0]);
    			if(cf == 1) {
        			int cf2 = JOptionPane.showOptionDialog(mainFrame, "대국을 시작합니다. 승인하시겠습니까?", "대국시작", 
        					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        					options,
        					options[0]);
        			if(cf2 == 1) {
        				confirm.setEnabled(false); // 확정 후에는 사용자가 해당 버튼을 다시 클릭할 수 없도록 해당 버튼을 비활성화 함
        				filepath = "./img/Black.png";
        				counter = 1; // 흑돌배치로 흐름이 넘어가도록 counter값을 1로 변경
        				t = startTime(); // 제한시간 측정을 시작함
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
    			JOptionPane.showMessageDialog(mainFrame, "적돌을 6개 이상 배치하지 않았습니다.");
    		}
    	}
    }
}