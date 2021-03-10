// Canvas에 대한 설정을 수행하는 Class

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class CV_Set
{	
	// 그림을 그리는 공간인 canvas 생성
    public Canvas tLayer = new Canvas(1800, 1000);                  // 가장 위쪽에 위치하게 되는 canvas(top)
    public Canvas btLayer = new Canvas(1800, 1000);                 // 가장 아래쪽에 위치하게 되는 canvas(bottom)
    public GraphicsContext tlgc = tLayer.getGraphicsContext2D();    // top Layer의 access에 사용하는 Graphics Context
    public GraphicsContext btlgc = btLayer.getGraphicsContext2D();  // bottom Layer의 access에 사용하는 Graphics Context
    public StackPane cvBox = new StackPane(btLayer, tLayer);        // canvas를 담는 Layout
    
    // 생성한 canvas에 대한 정보를 반환
    public StackPane load_CV()
    {
    	return cvBox;
    }
}
