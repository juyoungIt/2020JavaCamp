// 프로그램의 전체적인 흐름을 통제하는 main class
// Frame이 위치하게 되는 공간에 대한 정의도 함께 들어감
// 전체적인 배치, 모양을 수정하고 싶다면 해당 class에서 수행 가능
// 화면에 무언가를 그리는 코드, method를 모두 Main class에서 구성할 수 있도록 제작함

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class Main
{
	public static JFrame window; // 메인 프레임
	public static JPanel panel; // 메인 패널
	public static BackUp backup; // 생성하는 이미지들에 대한 백업공간
	
	// main method -> 프로그램의 전체적 흐름을 통제하는 method
	public static void main(String[] args)
	{
		window = new JFrame(); // 그래픽적 요소를 출력할 프레임 생성
		panel = new JPanel(); // 여러 컴포넌트를 추가할 패널 생성
		backup = new BackUp(); // 백업공간 생성
		window.add(panel); // 프레임에 스크롤 패널을 추가 - 스크롤 기능은 차후에 다시 시도하는 걸로
		
		// 메뉴바관련 코드
		MenuBar mBar = new MenuBar();
		window.setJMenuBar(mBar.setMenuBar()); // 생성한 메뉴바를 추가(frame에 붙임)
		
		// 프레임 출력관련 코드
		window.setTitle("Image Processing Program"); // 프레임 헤더 설정
		window.setSize(1300, 850); // 생성할 프레임의 크기 설정
		// window.setResizable(false); // 프레임의 사이즈를 사용자 임의로 변경하지 못하도록 설정
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true); // frame이 출력되도록 설정
	}
	
	
	// 인자로 전달받은 이미지를 프레임에 업데이트 시켜주는 method
	// 프레임에 이미지를 출력하기 위해서는 반드시 해당 method를 호출해야함
	// -> 해당 메소드 내에 백업에 대한 구문이 삽입되어 있음
	public static void updateToFrameImg(BufferedImage loading_img)
	{
		// 프레임으로 출력하는 이미지를 백업함.
		BackUp.push(loading_img);
		panel.removeAll(); // 패널에 기록되어 있던 모든 내용을 삭제함 - 이전내용이 현재의 이미지에 영향을 미치지 못하도록
		panel.repaint(); // 기존에 있던 내용을 확실하게 지우기 위해 해당 구문을 반복적으로 사용
		JLabel imgLabel = new JLabel(); // 이미지를 가져올 레이블을 생성함
		panel.add(imgLabel, BorderLayout.CENTER); // 이미지 레이블을 패널에 추가 및 레이아웃 지정
		imgLabel.setIcon(new ImageIcon(loading_img)); // 불러온 이미지를 레이블에 삽입
		window.add(panel, BorderLayout.CENTER); // 프레임에 패널을 추가, 레이아웃 지정
		window.pack(); // 창의 크기를 유동적으로 조절하기 위함
	}
	
	// 인자로 전달받은 이미지를 프레임이 그대로 업데이트 시켜주되 백업을 진행하지 않음
	// 이미지를 업데이트 하되 백업이 필요없는 경우 해당 method를 이용하여 백업을 진행함
	public static void updateToFrameImgDoNotBackUp(BufferedImage loading_img)
	{
		panel.removeAll(); // 패널에 기록되어 있던 모든 내용을 삭제함 - 이전내용이 현재의 이미지에 영향을 미치지 못하도록
		panel.repaint(); // 기존에 있던 내용을 확실하게 지우기 위해 해당 구문을 반복적으로 사용
		JLabel imgLabel = new JLabel(); // 이미지를 가져올 레이블을 생성함
		panel.add(imgLabel, BorderLayout.CENTER); // 이미지 레이블을 패널에 추가 및 레이아웃 지정
		imgLabel.setIcon(new ImageIcon(loading_img)); // 불러온 이미지를 레이블에 삽입
		window.add(panel, BorderLayout.CENTER); // 프레임에 패널을 추가, 레이아웃 지정
		window.pack(); // 창의 크기를 유동적으로 조절하기 위함
	}
	
	// 인자로 전달받은 툴바를 메인 프레임에 업데이트 시켜주는 method
	public static void updateToFrameTB(JToolBar tb)
	{
		window.add(tb, BorderLayout.NORTH); // 인자전달 툴바 프레임 추가 및 레이아웃 지정
		window.pack(); // 창의 크기를 유동적으로 조절하기 위함
		window.revalidate(); // 추가내용을 보이기 위해서 프레임을 업데이트 시킴
	}
	
	// 예외가 발생한 경우 전달한 문자열을 포함한 경고창을 만들어서 출력함
	public static void showWarnning(String message)
	{
		JOptionPane.showMessageDialog(window, message); // 경고창을 출력
	}
	
	// 사용자의 선택이 필요한 옵션의 경우 이에 대한 확인을 위해 확인창을 만들어서 출력
	public static void showConfirm(String message)
	{
		JOptionPane.showConfirmDialog(window, message); // 확인창을 출력
	}
}
