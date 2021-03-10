import javafx.scene.paint.Color;

// Canvas 상에서 그리는 shape들에 대한 전반적인 요소들을 다루는 class

public class Shapes
{
	// 선의 요소를 그리는 데 필요한 정보
	public double line_startX; // 시작점 x좌표
	public double line_startY; // 시작점 y좌표
	public double line_endX;   // 종착점 x좌표
	public double line_endY;   // 종착점 y좌표
	public double lineThick;   // 그려지는 직선의 두깨
	public Color lineColor;   // 그려지는 라인의 색상
	
	// 사각형을 그리는 데 필요한 정보
	public double rect_startX;  // 시작점 x좌표
	public double rect_startY;  // 시작점 y좌표
	public double rectWidth;    // 사각형 가로
	public double rectHeight;   // 사각형 세로
	public double rectThick;    // 사각형 윤곽선 두깨
	public Color rectColor;    // 윤곽선 색상
	
	// 원을 그리는 데 필요한 정보
	public double circle_startX; // 시작점 x좌표
	public double circle_startY; // 시작점 y좌표
	public double circleWidth;   // 원 가로
	public double circleHeight;  // 원 세로
	public double circleThick;    // 사각형 윤곽선 두깨
	public Color circleColor;    // 윤곽선 색상
	
	// PolyLine을 구성하는 데 필요한 정보
	public double pLine_startX; // 시작점 X좌표
	public double pLine_startY; // 시작점 Y좌표
	public double pLine_endX;   // 종착점 X좌표
	public double pLine_endY;   // 종착점 Y좌표
	public double pLineThick;   // 라인 두깨
	public Color pLineColor;    // 라인 색상  
	
	// UserDefine 그림을 구성하는 데 필요한 정보
	public double udLineX;        // 종착점 X좌표
	public double udLineY;        // 종착점 Y좌표
	public double udLineThick;    // 라인 두깨
	public Color udLineColor;     // 라인 색상
	
	// default constructor -> Shapes class가 가진 원소 정보를 최신화
	// canvas 좌표상에서 존재할 수 없는 값인 -1로 초기화
	public Shapes() {
		// 직선관련 요소 초기화
		line_startX = -1;
		line_startY = -1;
		line_endX = -1;
		line_endX = -1;
		lineThick = -1;   // 그려지는 직선의 두깨
		lineColor = null;   // 그려지는 라인의 색상
		// 사각형 관련요소 초기화
		rect_startX = -1;
		rect_startY = -1;
		rectWidth = -1;
		rectHeight = -1;
		rectThick = -1;    // 사각형 윤곽선 두깨
		rectColor = null;    // 윤곽선 색상
		// 원 관련요소 초기화
		circle_startX = -1;
		circle_startY = -1;
		circleWidth = -1;
		circleHeight = -1;
		circleThick = -1;    // 사각형 윤곽선 두깨
		circleColor = null;    // 윤곽선 색상
		// PolyLine에 쓰이는 요소 초기화
		pLine_startX = -1;
		pLine_startY = -1;
		pLine_endX = -1;
		pLine_endY = -1;
		// UserDefine 그림을 구성하는 데 필요한 정보
		udLineX = -1; // 시작점 X좌표
		udLineY = -1; // 시작점 Y좌표
		udLineThick = -1;    // 라인 두깨
		udLineColor = null;     // 라인 색상
	}
}
