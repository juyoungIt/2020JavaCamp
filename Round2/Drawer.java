import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

// 전달받은 요소를 바탕으로 Canvas에 그림을 그리는 class

public class Drawer
{
	public static int counter; // PolyLine을 그려낸 횟수저장
	public static double thickness; // 그리는 stroke의 두깨값 저장
	public static double tmp_line_startX; // Line의 시작X좌표를 임시저장 - polygon drawing 시 활용
	public static double tmp_line_startY; // Line의 시작Y좌표를 임시저장 - polygon drawing 시 활용
	public static boolean use[] = new boolean[10]; // drawing method의 사용여부를 저장
	public static History ht = new History(); // 그린 내용을 백업하는 저장공간
	public static Color cc; // 색상코드 값 저장
	public static int udCounter; // 저장된 사용자 정의곡선 요소의 수를 저장
	public static int latest_udc; // 가장 최근에 저장된 사용자 정의곡선 요소의 수를 저장
	
	public Drawer()
	{
		counter = 0; // 카운터 초기화
		thickness = 0;
		udCounter = 0;
		latest_udc = 0;
	}
	
	// 선을 그리는 method - 일반직선
	public static void drawLine(GraphicsContext gc1, GraphicsContext gc2, TB_Set tb)
	{		
		// 마우스의 버튼이 눌러졌을 때
		gc1.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
        	public void handle(MouseEvent e) {
				Shapes sp = new Shapes(); // 생성할 그림객체 생성
				System.out.println("새로운 drawLine 생성완료");
				sp.line_startX = e.getX(); // 생성한 sp의 line_startX 값 세팅
	    		sp.line_startY = e.getY(); // 생성한 sp의 line_startY 값 세팅
	    		// 툴바의 Color Picker에서 색상 선택하는 경우에 대한 액션
	    		tb.cp.setOnAction(event-> {
	    			cc = tb.cp.getValue();
	    			gc1.setStroke(cc); // 입력으로 들어온 값으로 색상을 변경함
	    			gc2.setStroke(cc);
	    			sp.lineColor = cc; // 색상백업
	    		});
	    					
	    		// 슬라이더 바에서 상태값을 가져와서 이에 리스너를 추가함
	    		tb.sd.valueProperty().addListener(event-> {
	    			double value = tb.sd.getValue(); // 슬라이더로부터 해당 값을 가져옴
	    			gc1.setLineWidth(value); // 이에 따라 선의 굵기를 변경
	    			gc2.setLineWidth(value);
	    			sp.lineThick = value; // 두깨값 백업
	    		});
	    		// 마우스가 드래그 되었을 때
	            gc1.getCanvas().setOnMouseDragged(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent e) {
	            		if( use[0] == true) {
	            			gc1.beginPath(); // 선을 그리기 시작함을 선언함
	                		gc1.lineTo(sp.line_endX, sp.line_endY);
	                		gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                		gc1.strokeLine(sp.line_startX, sp.line_startY, e.getX(), e.getY());
	            		}
	            	}
	            });
	    		
	    		// 마우스의 버튼이 때어졌을 때
	    		gc1.getCanvas().setOnMouseReleased(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent e) {
	            		if(use[0] == true) {
	            			sp.line_endX = e.getX();
	                		sp.line_endY = e.getY();
	                		gc1.strokeLine(sp.line_startX, sp.line_startY, sp.line_endX, sp.line_endY);
	                		gc2.strokeLine(sp.line_startX, sp.line_startY, sp.line_endX, sp.line_endY);
	                		ht.save(sp); // 생성한 그리기 정보를 백업
	                		System.out.println("저장된 도형의 수 : " + Drawer.count());
	                		System.out.println("저장내용 : " + ht.load().line_startX + " : " + ht.load().line_startY + " : "
	                				+ ht.load().line_endX + " : " + ht.load().line_endY);
	            		}
	            	}
	            });
	    	}
        });
	}
	
	// 사각형을 그리는 method
	public static void drawRect(GraphicsContext gc1, GraphicsContext gc2, TB_Set tb)
	{	
		// 마우스의 버튼이 눌러졌을 때
		gc1.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
        	public void handle(MouseEvent e) {
				Shapes sp = new Shapes(); // 생성할 그림객체 생성
        		System.out.println("새로운 drawRect 생성완료");
				sp.rect_startX = e.getX();
	    		sp.rect_startY = e.getY();
	    		
	    		// 마우스의 버튼이 때어졌을 때
	    		gc1.getCanvas().setOnMouseReleased(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent e) {
	            		// 툴바의 Color Picker에서 색상 선택하는 경우에 대한 액션
	            		tb.cp.setOnAction(event-> {
	            			cc = tb.cp.getValue();
	            			gc1.setStroke(cc);
	            			gc2.setStroke(cc);
	            			sp.rectColor = cc; // color 값 백업
	            		});
	            					
	            		// 슬라이더 바에서 상태값을 가져와서 이에 리스너를 추가함
	            		tb.sd.valueProperty().addListener(event-> {
	            			double value = tb.sd.getValue(); // 슬라이더로부터 해당 값을 가져옴
	            			gc1.setLineWidth(value); // 이에 따라 선의 굵기를 변경
	            			gc2.setLineWidth(value);
	            			sp.rectThick = value; // 두께값 백업
	            		});
	            		if(use[1] == true) {
	            			if(sp.rect_startX < e.getX() && sp.rect_startY < e.getY()) {
	            				sp.rectWidth = Math.abs(e.getX() - sp.rect_startX);
	            				sp.rectHeight =  Math.abs(e.getY() - sp.rect_startY);
	                			gc1.strokeRect(sp.rect_startX, sp.rect_startY, sp.rectWidth, sp.rectHeight);
	                			gc2.strokeRect(sp.rect_startX, sp.rect_startY, sp.rectWidth, sp.rectHeight);
	                		}
	                		else if(sp.rect_startX < e.getX() && sp.rect_startY > e.getY()) {
	                			sp.rectWidth = Math.abs(e.getX() - sp.rect_startX);
	            				sp.rectHeight =  Math.abs(e.getY() - sp.rect_startY);
	                			gc1.strokeRect(sp.rect_startX, e.getY(), sp.rectWidth, sp.rectHeight);
	                			gc2.strokeRect(sp.rect_startX, e.getY(), sp.rectWidth, sp.rectHeight);
	                		}
	                		else if(sp.rect_startX > e.getX() && sp.rect_startY < e.getY()) {
	                			sp.rectWidth = Math.abs(e.getX() - sp.rect_startX);
	            				sp.rectHeight =  Math.abs(e.getY() - sp.rect_startY);
	                			gc1.strokeRect(e.getX(), sp.rect_startY, sp.rectWidth, sp.rectHeight);
	                			gc2.strokeRect(e.getX(), sp.rect_startY, sp.rectWidth, sp.rectHeight);
	                		}
	                		else {
	                			sp.rectWidth = Math.abs(e.getX() - sp.rect_startX);
	            				sp.rectHeight =  Math.abs(e.getY() - sp.rect_startY);
	                			gc1.strokeRect(e.getX(), e.getY(), sp.rectWidth, sp.rectHeight);
	                			gc2.strokeRect(e.getX(), e.getY(), sp.rectWidth, sp.rectHeight);
	                		}
	            			ht.save(sp); // 생성한 그리기 정보를 백업
	            			System.out.println("저장된 도형의 수 : " + Drawer.count());
	            			System.out.println("저장내용 : " + ht.load().rect_startX + " : " + ht.load().rect_startY + " : " +
	            					+ ht.load().rectWidth + " : " + ht.load().rectHeight);
	            		}
	            	}
	            });
	            
	    		// 마우스가 드래그 되었을 때
	            gc1.getCanvas().setOnMouseDragged(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent e) {
	            		if(use[1] == true) {
	            			if(sp.rect_startX < e.getX() && sp.rect_startY < e.getY()) {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeRect(sp.rect_startX, sp.rect_startY, Math.abs(e.getX() - sp.rect_startX), Math.abs(e.getY() - sp.rect_startY));
	                		}
	                		else if(sp.rect_startX < e.getX() && sp.rect_startY > e.getY()) {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeRect(sp.rect_startX, e.getY(), Math.abs(e.getX() - sp.rect_startX), Math.abs(e.getY() - sp.rect_startY));
	                		}
	                		else if(sp.rect_startX > e.getX() && sp.rect_startY < e.getY()) {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeRect(e.getX(), sp.rect_startY, Math.abs(e.getX() - sp.rect_startX), Math.abs(e.getY() - sp.rect_startY));
	                		}
	                		else {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeRect(e.getX(), e.getY(), Math.abs(e.getX() - sp.rect_startX), Math.abs(e.getY() - sp.rect_startY));
	                		}
	            		}
	            	}
	            });
	    	}
        });
	}
	
	// 원을 그리는 method
	public static void drawCircle(GraphicsContext gc1, GraphicsContext gc2, TB_Set tb)
	{	
		// 마우스의 버튼이 눌러졌을 때
		gc1.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
        	public void handle(MouseEvent e) {
				Shapes sp = new Shapes(); // 생성할 그림객체 생성
				System.out.println("새로운 drawCircle 생성완료");
				sp.circle_startX = e.getX();
	    		sp.circle_startY = e.getY();
	    		
	    		// 마우스의 버튼이 때어졌을 때
	    		gc1.getCanvas().setOnMouseReleased(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent e) {
	            		if(use[2] == true) {
	            			// 툴바의 Color Picker에서 색상 선택하는 경우에 대한 액션
		            		tb.cp.setOnAction(event-> {
		            			cc = tb.cp.getValue();
		            			gc1.setStroke(cc);
		            			gc2.setStroke(cc);
		            			sp.circleColor = cc; // color 값 백업
		            		});
		            					
		            		// 슬라이더 바에서 상태값을 가져와서 이에 리스너를 추가함
		            		tb.sd.valueProperty().addListener(event-> {
		            			double value = tb.sd.getValue(); // 슬라이더로부터 해당 값을 가져옴
		            			gc1.setLineWidth(value); // 이에 따라 선의 굵기를 변경
		            			gc2.setLineWidth(value);
		            			sp.circleThick = value; // 두께값 백업
		            		});
	            			if(sp.circle_startX < e.getX() && sp.circle_startY < e.getY()) {
	            				sp.circleWidth = Math.abs(e.getX() - sp.circle_startX);
	            				sp.circleHeight = Math.abs(e.getY() - sp.circle_startY);
	                			gc1.strokeOval(sp.circle_startX, sp.circle_startY, sp.circleWidth, sp.circleHeight);
	                			gc2.strokeOval(sp.circle_startX, sp.circle_startY, sp.circleWidth, sp.circleHeight);
	                		}
	                		else if(sp.circle_startX < e.getX() && sp.circle_startY > e.getY()) {
	                			sp.circleWidth = Math.abs(e.getX() - sp.circle_startX);
	            				sp.circleHeight = Math.abs(e.getY() - sp.circle_startY);
	                			gc1.strokeOval(sp.circle_startX, e.getY(), sp.circleWidth, sp.circleHeight);
	                			gc2.strokeOval(sp.circle_startX, e.getY(), sp.circleWidth, sp.circleHeight);
	                		}
	                		else if(sp.circle_startX > e.getX() && sp.circle_startY < e.getY()) {
	                			sp.circleWidth = Math.abs(e.getX() - sp.circle_startX);
	            				sp.circleHeight = Math.abs(e.getY() - sp.circle_startY);
	                			gc1.strokeOval(e.getX(), sp.circle_startY, sp.circleWidth, sp.circleHeight);
	                			gc2.strokeOval(e.getX(), sp.circle_startY, sp.circleWidth, sp.circleHeight);
	                		}
	                		else {
	                			sp.circleWidth = Math.abs(e.getX() - sp.circle_startX);
	            				sp.circleHeight = Math.abs(e.getY() - sp.circle_startY);
	                			gc1.strokeOval(e.getX(), e.getY(), sp.circleWidth, sp.circleHeight);
	                			gc2.strokeOval(e.getX(), e.getY(), sp.circleWidth, sp.circleHeight);
	                		}
	            			ht.save(sp); // 생성한 그리기 정보를 백업
	            			System.out.println("저장된 도형의 수 : " + Drawer.count());
	            			System.out.println("저장내용 : " + ht.load().circle_startX + " : " + ht.load().circle_startY + " : " +
	            					+ ht.load().circleWidth + " : " + ht.load().circleHeight);
	            		}
	            	}
	            		
	            });
	            
	    		// 마우스가 드래그 되었을 때
	            gc1.getCanvas().setOnMouseDragged(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent e) {
	            		if(use[2] == true) {
	            			if(sp.circle_startX < e.getX() && sp.circle_startY < e.getY()) {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeOval(sp.circle_startX, sp.circle_startY, Math.abs(e.getX() - sp.circle_startX), Math.abs(e.getY() - sp.circle_startY));
	                		}
	                		else if(sp.circle_startX < e.getX() && sp.circle_startY > e.getY()) {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeOval(sp.circle_startX, e.getY(), Math.abs(e.getX() - sp.circle_startX), Math.abs(e.getY() - sp.circle_startY));
	                		}
	                		else if(sp.circle_startX > e.getX() && sp.circle_startY < e.getY()) {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeOval(e.getX(), sp.circle_startY, Math.abs(e.getX() - sp.circle_startX), Math.abs(e.getY() - sp.circle_startY));
	                		}
	                		else {
	                			gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
	                			gc1.strokeOval(e.getX(), e.getY(), Math.abs(e.getX() - sp.circle_startX), Math.abs(e.getY() - sp.circle_startY));
	                		}
	            		}
	            	}
	            });
	    	}
        });
	}
	
	// PolyLine을 그리는 method
	public static void drawPolyLine(GraphicsContext gc1, GraphicsContext gc2, TB_Set tb) {
		Shapes sp = new Shapes(); // 생성할 그림객체 생성
		
		// 사용하는 stroke에 대한 정보를 가져오는 부분
		tb.cp.setOnAction(event-> {
			cc = tb.cp.getValue();
			gc1.setStroke(cc); // 입력으로 들어온 값으로 색상을 변경함
			gc2.setStroke(cc);
			sp.pLineColor = cc; // 색상값 백업
		});
		tb.sd.valueProperty().addListener(event-> {
			double value = tb.sd.getValue(); // 슬라이더로부터 해당 값을 가져옴
			gc1.setLineWidth(value); // 이에 따라 선의 굵기를 변경
			gc2.setLineWidth(value);
			sp.pLineThick = value; // 두깨 백업
		});
		
		// 처음 그리는 직선의 경우 다음의 규칙을 반영해서 그린다.
		// 마우스의 버튼이 눌렸을 때
		gc1.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		if(counter != 10) {
        			sp.pLine_startX = e.getX();
            		sp.pLine_startY = e.getY();
        		}
        		else if(counter == 10) {
					Shapes sp = new Shapes(); // 생성할 그림객체 생성 - 새로운 선에 대한 객체
	        		sp.pLine_startX = tmp_line_startX;
	        		sp.pLine_startY = tmp_line_startY;
					sp.pLine_endX = e.getX();
					sp.pLine_endY = e.getY();
		       		gc1.setFill(Color.BLACK);
		       		gc1.strokeLine(tmp_line_startX, tmp_line_startY, sp.pLine_endX, sp.pLine_endY);
		       		gc2.setFill(Color.BLACK);
		       		gc2.strokeLine(tmp_line_startX, tmp_line_startY, sp.pLine_endX, sp.pLine_endY);
		       		tmp_line_startX = sp.pLine_endX;
	        		tmp_line_startY = sp.pLine_endY;
	        		ht.save(sp); // 생성한 그리기 정보를 백업
	        		System.out.println("저장된 도형의 수 : " + Drawer.count());
	        		System.out.println("저장내용 : " + ht.load().pLine_startX + " : " + ht.load().pLine_startY + " : "
            				+ ht.load().pLine_endX + " : " + ht.load().pLine_endY);
				}
        	}
        });
        
		// 마우스의 버튼의 입력이 해제되었을 때(버튼이 때어졌을 때)
        gc1.getCanvas().setOnMouseReleased(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		if(use[3] == true) {
        			if(counter != 10) {
        				sp.pLine_endX = e.getX();
                		sp.pLine_endY = e.getY();
                		gc1.setFill(Color.BLACK);
                		gc1.strokeLine(sp.pLine_startX, sp.pLine_startY, sp.pLine_endX, sp.pLine_endY);
                		gc2.setFill(Color.BLACK);
                		gc2.strokeLine(sp.pLine_startX, sp.pLine_startY, sp.pLine_endX, sp.pLine_endY);
                		tmp_line_startX = sp.pLine_endX;
                		tmp_line_startY = sp.pLine_endY;
                		counter = 10; // 직선을 그린 회차수를 반영
                		ht.save(sp); // 생성한 그리기 정보를 백업
                		System.out.println("저장된 도형의 수 : " + Drawer.count());
                		System.out.println("저장내용 : " + ht.load().pLine_startX + " : " + ht.load().pLine_startY + " : "
                				+ ht.load().pLine_endX + " : " + ht.load().pLine_endY);
        			}
        		}
        	}
        });
	}

	// 사용자 정의 곡선을 그리는 method - 특별한 형식없이 자유롭게 그림
	public static void drawUserDefine(GraphicsContext gc1, GraphicsContext gc2, TB_Set tb) {
		// 툴바의 Color Picker에서 색상 선택하는 경우에 대한 액션
		tb.cp.setOnAction(event-> {
			cc = tb.cp.getValue();
			gc1.setStroke(cc); // 입력으로 들어온 값으로 색상을 변경함
			gc2.setStroke(cc);
			
		});
		// 슬라이더 바에서 상태값을 가져와서 이에 리스너를 추가함
		tb.sd.valueProperty().addListener(evnet-> {
			double value = tb.sd.getValue(); // 슬라이더로부터 해당 값을 가져옴
			thickness = value; // 슬라이드 바로부터 가져오는 크기값을 별도의 변수에 저장
			gc1.setLineWidth(value); // 이에 따라 선의 굵기를 변경
			gc2.setLineWidth(value);
		});
		
		// 마우스의 버튼이 눌러졌을 때 - 선을 그리는 부분
		gc1.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		Shapes sp = new Shapes();
        		sp.udLineColor = cc;
        		sp.udLineThick = thickness;
        		gc1.beginPath(); // 선을 그리기 시작함을 선언함
        		gc1.lineTo(e.getX(), e.getY());
        		sp.udLineX = e.getX();
        		sp.udLineY = e.getY();
        		gc1.stroke(); // 선을 그림
        		gc2.beginPath(); // 선을 그리기 시작함을 선언함
        		gc2.lineTo(e.getX(), e.getY());
        		gc2.stroke(); // 선을 그림
        		ht.save(sp); // 생성한 요소를 저장
        		udCounter++;  // 저장된 UserDefine 요소의 수 1증가
        		latest_udc++;
        		System.out.println("저장된 도형 수 : " + Drawer.count());
        		System.out.println("저장된 좌표 수 : " + udCounter);
        		System.out.println("저장내용 : " + ht.load().udLineX + " : " + ht.load().udLineY);
        	} 
        });
		
		// 마우스를 드래그 하는 경우 - 마우스의 이동에 따라서 지속적으로 선을 그림
		gc1.getCanvas().setOnMouseDragged(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		if( use[4] == true) {
        			Shapes sp = new Shapes();
        			gc1.lineTo(e.getX(), e.getY());
        			sp.udLineX = e.getX();
            		sp.udLineY = e.getY();
            		gc1.stroke();
            		gc2.lineTo(e.getX(), e.getY());
            		gc2.stroke();
            		ht.save(sp); // 생성한 요소를 저장
            		udCounter++; // 저장된 UserDefine 요소의 수 1증가
            		latest_udc++;
            		System.out.println("저장된 도형 수 : " + Drawer.count());
            		System.out.println("저장된 좌표 수 : " + udCounter);
            		System.out.println("저장내용 : " + ht.load().udLineX + " : " + ht.load().udLineY);
        		}
        	}
        });
		
		gc1.getCanvas().setOnMouseReleased(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		if(use[4] == true) {
        			ht.length_save(latest_udc);
        			latest_udc = 0; // 다음번의 사용을 위해서 초기화
        		}
        	}
        });
	}
	
	// 현재 Canvas에 기록된 모든 내용을 삭제함
	public static void allClear(GraphicsContext gc1, GraphicsContext gc2)
	{
		Shapes sp = new Shapes();
		gc1.clearRect(0, 0, gc1.getCanvas().getWidth(), gc1.getCanvas().getHeight());
		gc2.clearRect(0, 0, gc2.getCanvas().getWidth(), gc2.getCanvas().getHeight());
		sp.rect_startX = 0;
		sp.rect_startY = 0;
		sp.rectWidth = gc1.getCanvas().getWidth();
		sp.rectHeight = gc2.getCanvas().getHeight();
		ht.save(sp); // 수행내용 백업본에 반영
	}
	
	// 사용자가 선택하는 영역(비트맵) 단위로 내용 삭제 -> 백업내용에도 당연히 반영함 
	public static void partClear(GraphicsContext gc1, GraphicsContext gc2)
	{
		// 마우스의 버튼이 눌러졌을 때 - 선을 그리는 부분
		gc1.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		Shapes sp = new Shapes();
        		sp.udLineColor = cc;
        		sp.udLineThick = thickness;
        		gc1.beginPath(); // 선을 그리기 시작함을 선언함
        		gc1.lineTo(e.getX(), e.getY());
        		sp.udLineX = e.getX();
        		sp.udLineY = e.getY();
        		gc1.setStroke(Color.rgb(244, 244, 244));
        		gc2.setStroke(Color.rgb(244, 244, 244));
        		gc1.stroke(); // 선을 그림
        		gc2.beginPath(); // 선을 그리기 시작함을 선언함
        		gc2.lineTo(e.getX(), e.getY());
        		gc2.stroke(); // 선을 그림
        		ht.save(sp); // 생성한 요소를 저장
        		udCounter++; // 저장된 UserDefine 요소의 수 1증가
        		System.out.println("저장된 도형 수 : " + Drawer.count());
        		System.out.println("저장된 좌표 수 : " + udCounter);
        		System.out.println("저장내용 : " + ht.load().udLineX + " : " + ht.load().udLineY);
        	} 
        });
		
		// 마우스를 드래그 하는 경우 - 마우스의 이동에 따라서 지속적으로 선을 그림
		gc1.getCanvas().setOnMouseDragged(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		if(use[5] == true)
        		{
        			Shapes sp = new Shapes();
        			gc1.lineTo(e.getX(), e.getY());
        			sp.udLineX = e.getX();
            		sp.udLineY = e.getY();
            		gc1.setStroke(Color.rgb(244, 244, 244));
            		gc2.setStroke(Color.rgb(244, 244, 244));
            		gc1.stroke();
            		gc2.lineTo(e.getX(), e.getY());
            		gc2.stroke();
            		ht.save(sp); // 생성한 요소를 저장
            		udCounter++; // 저장된 UserDefine 요소의 수 1증가
            		System.out.println("저장된 도형 수 : " + Drawer.count());
            		System.out.println("저장된 좌표 수 : " + udCounter);
            		System.out.println("저장내용 : " + ht.load().udLineX + " : " + ht.load().udLineY);
        		}
        	}
        });
	}
	
	// 저장된 도형의 수 반환
	public static int count()
	{
		return ht.count();
	}
	
}