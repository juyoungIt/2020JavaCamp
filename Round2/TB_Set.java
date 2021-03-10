// 툴바에 대한 설정을 담당하는 class
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;

public class TB_Set{
	
	public ToolBar tb; // 툴바생성
    public ColorPicker cp; // Color_Picker 추가
    public Slider sd; // Slider 추가(크기조절에 사용)
    
    // default constructor
    public TB_Set()
    {
    	tb = new ToolBar();
    	cp = new ColorPicker();
    	sd = new Slider();
    	
    	// sliderBar에 대한 설정
    	sd.setMin(1);   // sliderBar의 최소값
		sd.setMax(50);  // sliderBar의 최대값
		sd.setShowTickLabels(true); // sliderBar의 간격설정
		
		// colorPicker에 대한 설정
	    cp.setValue(Color.BLACK); // ColorPicker의 색상 기본값 지정
	    
	    // toolBar에 생성한 요소 추가
	    tb.getItems().add(cp); // color picker 추가
	    tb.getItems().add(sd); // slider 추가
    }
    
	// 생성한 ToolBar 반환
	public ToolBar load_TB()
	{
		return tb;
	}
}