import javafx.scene.canvas.GraphicsContext;

public class reDrawer
{
	public static Shapes tmp; // 로딩하는 Shapes 정보를 일시저장
	public static int scode;
	
	// 백업한 내용을 바탕으로 해당 회차까지 그림을 다시 그림
	public static void redrawing(int idx, GraphicsContext gc1, GraphicsContext gc2)
	{
		// 기존에 기록되어 있던 내용을 모두 지움(백업본은 살아있고 캔버스 상에서만 지우는 것)
		gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
		gc2.clearRect(0, 0, gc2.getCanvas().getWidth(), gc2.getCanvas().getHeight());

		// 사용자가 지정한 index값 까지 다음을 반복함
		for(int i=0 ; i<idx+1 ; i++)
		{
			tmp = Drawer.ht.load(i);
			scode = detector(tmp); // Shapes의 형태를 판별받아 정수 scode로 판정값을 로드
			gc1.beginPath();
			gc2.beginPath();
			
			// Line인 경우
			if(scode == 1) {
				System.out.println("직선 : " + tmp.line_startX + " : " + tmp.line_startY + " : " + tmp.line_endX + " : " + tmp.line_endY);
				gc1.strokeLine(tmp.line_startX, tmp.line_startY, tmp.line_endX, tmp.line_endY);
				gc2.strokeLine(tmp.line_startX, tmp.line_startY, tmp.line_endX, tmp.line_endY);
			}
			// Rectangle인 경우
			else if(scode == 2) {
				System.out.println("사각형 : " + tmp.rect_startX + " : " + tmp.rect_startY + " : " + tmp.rectWidth + " : " + tmp.rectHeight);
				gc1.strokeRect(tmp.rect_startX, tmp.rect_startY, tmp.rectWidth, tmp.rectHeight);
				gc2.strokeRect(tmp.rect_startX, tmp.rect_startY, tmp.rectWidth, tmp.rectHeight);
			}
			// Circle인 경우
			else if(scode == 3) {
				System.out.println("원 : " + tmp.circle_startX + " : " + tmp.circle_startY + " : " + tmp.circleWidth + " : " + tmp.circleHeight);
				gc1.strokeOval(tmp.circle_startX, tmp.circle_startY, tmp.circleWidth, tmp.circleHeight);
				gc2.strokeOval(tmp.circle_startX, tmp.circle_startY, tmp.circleWidth, tmp.circleHeight);
			}
			// PolyLine인 경우 - 같은 Line이지만 다르게 구분하여서 구성
			else if(scode == 4) {
				System.out.println("PolyLine : " + tmp.pLine_startX + " : " + tmp.pLine_startY + " : " + tmp.pLine_endX + " : " + tmp.pLine_endY);
				gc1.strokeLine(tmp.pLine_startX, tmp.pLine_startY, tmp.pLine_endX, tmp.pLine_endY);
				gc2.strokeLine(tmp.pLine_startX, tmp.pLine_startY, tmp.pLine_endX, tmp.pLine_endY);
			}
			// 사용자 정의인 경우
			else if(scode == 5) {
				gc1.lineTo(tmp.udLineX, tmp.udLineY);
				gc1.stroke();
				gc2.lineTo(tmp.udLineX, tmp.udLineY);
				gc2.stroke();
			}
		}
	}
	
	// 입력된 Shapes 값이 어떤 도형인지 판별하여 그 결과를 정수로 출력
	/*
	 <value mapping list>
	 1. Line
	 2. Rectangle
	 3. Circle
	 4. PolyLine
	 5. User define
	 */
	public static int detector(Shapes sp)
	{
		if((sp.rect_startX == -1) && (sp.circle_startX == -1) && (sp.pLine_startX == -1) && (sp.udLineX == -1))
			return 1; // 일반직선(Line)
		else if((sp.line_startX == -1) && (sp.circle_startX == -1) && (sp.pLine_startX == -1) && (sp.udLineX == -1))
			return 2; // 사각형(Rectangle)
		else if((sp.line_startX == -1) && (sp.rect_startX == -1) && (sp.pLine_startX == -1) && (sp.udLineX == -1))
			return 3; // 원(Circle)
		else if((sp.line_startX == -1) && (sp.rect_startX == -1) && (sp.circle_startX == -1) && (sp.udLineX == -1))
			return 4; // PolyLine
		else if((sp.line_startX == -1) && (sp.rect_startX == -1) && (sp.circle_startX == -1) && (sp.pLine_startX == -1))
			return 5; // 사용자 정의곡선(User define)
		else {
			System.out.println("Error! : Can not identify shapes");
			return -1; // 비정상적 입력의 의미하는 -1 출력
		}
	}
}
