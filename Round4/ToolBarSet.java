import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// 영상에 대한 작업을 처리할 때 필요한 툴바를 제공하는 class

public class ToolBarSet
{
	private JToolBar bcTool; // 밝기, 대비 조절에 사용되는 툴바
	
	// 밝기, 대비 조절에 사용되는 툴바를 세팅함
	public JToolBar setBCTB()
	{
		bcTool = new JToolBar(); // 툴바를 생성
		bcTool.setBackground(Color.LIGHT_GRAY); // 툴바의 색상을 지정
		JLabel bSliderName = new JLabel(); // 밝기조절 슬라이더임을 보여주는 레이블
		JLabel cSliderName = new JLabel(); // 대비조절 슬라이더임을 보여주는 레이블
		bSliderName.setText("밝기 (brightness)"); // 레이블의 내용설정 - 1
		cSliderName.setText("대비 (contrast)"); // 레이블의 내용설정 - 2
		JSlider bright = new JSlider(-200, 200);   // 밝기 조절에 사용되는 슬라이더
		// 슬라이더바의 값이 바뀐 경우 다음의 동작을 수행한다. - 밝기조절 관련 동작 수행
		bright.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent event)
			{
				JSlider source = (JSlider)event.getSource(); // 슬라이더 바로부터 값을 읽어옴
				int b_value = (int)source.getValue(); // 사용자가 지정한 밝기값을 읽어옴
				// 이미지와 밝기값을 전달하여 해당 이미지의 밝기를 조정함
				Bright_and_Contrast.adjustBright(Current_Image.load(),b_value);
			}
		});
		// 해당 함수의 경우 음수가 들어가면 값이 튀어버려서 이미지 처리에 문제가 생긴다. 주의해서 잘 사용하자
		// -> 음수값이 들어가면 픽셀의 컬러값이 튀어서 완전히 검은색으로 처리되는 문제점이 있음
		JSlider contrast = new JSlider(100, 1000, 100); // 대비 조절에 사용되는 슬라이더
		// 슬라이더바의 값이 바뀐 경우 다음의 동작을 수행한다. - 대비조절 관련 동작 수행
		contrast.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent event)
			{
				JSlider source = (JSlider)event.getSource(); // 슬라이더 바로부터 값을 읽어옴
				int b_value = (int)source.getValue(); // 사용자가 지정한 밝기값을 읽어옴
				// 이미지와 밝기값을 전달하여 해당 이미지의 밝기를 조정함
				Bright_and_Contrast.adjustContrast(Current_Image.load(),b_value);
			}
		});
		bcTool.add(bSliderName); // 슬라이더를 설명하는 레이블 추가 - 1
		bcTool.add(bright);   // 밝기조절에 사용되는 슬라이더를 툴바에 추가
		bcTool.add(cSliderName); // 슬라이더를 설명하는 레이블 추가 - 2
		bcTool.add(contrast); // 대비조절에 사용되는 슬라이더를 툴바에 추가
		
		return bcTool; // 생성한 툴바를 반환
	}
}
