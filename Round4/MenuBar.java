// 메뉴바를 구성하는 내용들을 다루는 Class

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBar
{
	public JMenuBar mBar; // 모든 메뉴들이 한곳으로 모이는 메뉴바
	public JMenu menu1;   // 메뉴버튼 1 -> 파일과 관련된 옵션들을 포함함
	public JMenu menu2;   // 메뉴버튼 2 -> 편집과 관련된 옵션들을 포함함
	public JMenu menu3;   // 메뉴버튼 3 -> undo, redo와 관련된 옵션들을 포함
	public JMenu menu4;   // 메뉴버튼 4 -> 기타옵션들과 관련된 옵션들을 포함함.
	public JMenuItem open; // 파일을 열 때 사용하는 옵션
	public JMenuItem save; // 파일을 저장할 때 사용하는 옵션
	public JMenuItem exit; // 프로그램을 종료함
	public JMenuItem gscale; // 로딩한 이미지를 흑백으로 처리하여 출력해줌
	public JMenuItem reverse; // 색상을 반전시켜서 출력해줌
	public JMenuItem achromatic; // 전달한 이미지의 색상을 무채색화함 -> 사진보다는 만화캐릭터에 효과적
	public JMenuItem bcScale; // 로딩한 이미지의 밝기, 대비값을 사용자의 의도에 따라서 조절하여 보여줌
	public JMenuItem merge; // 로딩한 2개 이상의 이미지를 합성해서 그 결과를 출력함
	public JMenuItem undo; // 이전에 작업한 내용을 복원함.
	public JMenuItem redo; // 취소한 현재 작업을 다시 복원함 -> undo 명령을 수행한 경우에만 사용가능
	public JMenuItem dark;
	public JMenuItem light;
	
	// 메뉴바에 대한 세팅을 진행해주는 method
	public JMenuBar setMenuBar()
	{
		mBar = new JMenuBar(); // 메뉴바를 생성함
		menu1 = new JMenu("파일"); // 메뉴버튼 생성
		menu2 = new JMenu("편집");
		menu3 = new JMenu("복원"); // undo, redo
		menu4 = new JMenu("기타");
		
		open = new JMenuItem("열기 (Open)");
		open.addActionListener(new basicListener()); // 액션 리스너 등록
		save = new JMenuItem("저장 (Save)");
		save.addActionListener(new basicListener()); // 액션 리스너 등록
		exit = new JMenuItem("종료 (Exit)");
		exit.addActionListener(new basicListener()); // 액션 리스너 등록
		
		gscale = new JMenuItem("Gray-Scale");
		gscale.addActionListener(new editListener()); // 액션 리스너 등록
		reverse = new JMenuItem("색상반전 (reverse)");
		reverse.addActionListener(new editListener()); // 액션 리스너 등록
		achromatic = new JMenuItem("카툰화");
		achromatic.addActionListener(new editListener()); // 액션리스너 등
		bcScale = new JMenuItem("밝기 및 대비");
		bcScale.addActionListener(new editListener()); // 액션 리스너 등록
		merge = new JMenuItem("병합 (Merge)");
		merge.addActionListener(new editListener()); // 액션 리스너 등록
		
		undo = new JMenuItem("Undo");
		undo.addActionListener(new urdoListener());
		redo = new JMenuItem("Redo");
		redo.addActionListener(new urdoListener());
		
		// 생성한 요소들을 추가
		menu1.add(open);    // open 추가
		menu1.add(save);    // save 추가
		
		menu2.add(gscale);  // gscale 추가
		menu2.add(reverse); // 색상반전 추가
		menu2.add(achromatic); // 무채색화 추가
		menu2.add(bcScale); // bcScale 추가
		menu2.add(merge);   // merge 추가
		
		menu3.add(undo);    // undo 기능 추가
		menu3.add(redo);    // redo 기능 추가
		
		menu4.add(exit);    // exit 추가
		
		// 메뉴바에 생성한 메뉴를 모두 추가
		mBar.add(menu1);
		mBar.add(menu2);
		mBar.add(menu3);
		mBar.add(menu4);
		
		return mBar; // 세팅을 마친 메뉴바를 반환
	}
	
	// 이벤트에 대한 액션 정의를 위한 내부 클래스 정의
	// 메뉴에서 열기, 저장, 종료와 같은 기본적인 기능에 대한 액션을 정의한 내부 클래스
	class basicListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			String str = ae.getActionCommand();
			// 열기버튼을 클릭한 경우에 대한 동작
			if(str.equals("열기 (Open)"))
			{
				Bright_and_Contrast.runCode = 0; // runCode를 0으로 초기화
				JFileChooser fileChooser = new JFileChooser();
				//기본 Path의 경로 설정 -> 최초로 창을 띄웠을 때 기본적으로 접근하는 경 
		    	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));
				
				// 파일을 오픈하는 창을 띄움
				int result = fileChooser.showOpenDialog(Main.window);
				// 확인창을 클릭하여 파일을 선택한 경우
				if(result == JFileChooser.APPROVE_OPTION)
				{
					// 선택한 파일의 경로를 반환한다.
					File selectedFile = fileChooser.getSelectedFile();
					Loading_Image.load(selectedFile); // 해당파일을 로딩하여 창에 띄움
				}
				else
				{
					Main.showWarnning("선택한 파일이 없습니다. 파일을 선택해주세요.");
				}
			}
			// 저장 버튼을 클릭한 경우에 대한 동작
			else if(str.equals("저장 (Save)"))
			{
				Save_Image.save(); // 현재 사용중인 내용을 저장함
				Bright_and_Contrast.runCode = -1; // runCode를 -1로 변경
			}
			// 종료버튼을 누른 경우에 대한 동
			else if(str.equals("종료 (Exit)"))
			{
				// 최근에 수행한 작업에 대하여 저장을 수행하지 않은 경우
				if(Bright_and_Contrast.runCode != -1)
				{
					Object[] options = {"취소", "확인"};
					int cf = JOptionPane.showOptionDialog(Main.window, "저장하지 않은 작업내용이 있습니다.\n정말로 종료하시겠습니까?\n저장하지 않은 이미지는 복원할 수 없습니다.", "주의!", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
							options,
							options[0]);
					if(cf == 1)
						System.exit(0); // 사용자가 확인을 선택한 경우 저장하지 않은 내용이 있더라도 프로그램을 종료함
				}
				// 최근에 수행한 작업들을 모두 저장했거나 수행한 작업이 없는 경우
				else
					System.exit(0); // 프로세스 종료
			}
		}
	}
	
	// 다양한 편집기능에 대한 액션을 정의한 내부 클래스
	class editListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			String str = ae.getActionCommand();
			// Gray-Scale을 선택한 경우
			if(str.equals("Gray-Scale"))
			{
				Bright_and_Contrast.runCode = 0;
				Gray_Scale.gray_scale();
			}
			// 색상반전을 선택한 경우
			else if(str.equals("색상반전 (reverse)"))
			{
				Bright_and_Contrast.runCode = 0;
				Color_Reverse.reverse();
			}
			// 무채색화를 선택한 경
			else if(str.equals("카툰화"))
			{
				Bright_and_Contrast.runCode = 0;
				Achromatic.achromaicImage();
			}
			// 밝기 및 대비를 선택한 경우
			else if(str.equals("밝기 및 대비"))
			{
				ToolBarSet tb = new ToolBarSet(); // 해당 옵션을 위한 툴바를 생성
				Main.updateToFrameTB(tb.setBCTB()); // 생성한 툴바를 프레임에 추가 및 업데이트
			}
			// 병합을 선택한 경우
			else if(str.equals("병합 (Merge)"))
			{
				Bright_and_Contrast.runCode = 0;
				Merge.imageMerge(); // 이미지를 병합함
			}
		}
	}
	
	// Undo, Redo 옵션에 대한 동작을 기술한 내부 class
	class urdoListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			String str = ae.getActionCommand();
			
			// Undo를 선택한 경우
			if(str.equals("Undo"))
			{
				UnReDo.undo();
			}
			// Redo를 선택한 경우
			else if(str.equals("Redo"))
			{
				UnReDo.redo();
			}
		}
	}
	
	
}
