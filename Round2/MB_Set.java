// 메뉴바에 대한 설정을 수행하는 class
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class MB_Set
{
	public MenuBar mb; // 프로그램 윈도우 상단에 추가되는 메뉴바
	public static int undoCount;         // undo 명령을 수행한 횟수
	public static int redoCount;         // redo 명령을 수행한 횟수
	public static int current_idx;       // 현재 조회중인 인덱스 값을 저장
	public static int undoToken;         // undo를 통해 지나간 인덱스 값을 저장
	public static int length_idx;        // 저장한 길이값의 인덱스를 저장
	public static int userDef_lineCount; // 읽어들인 사용자 정의 곡선의 수

	// top head menu
	public Menu menu1;
	public Menu menu2;
	public Menu menu3;
	public Menu menu4;
	public Menu menu5;
	// inner menu
	public MenuItem line;
	public MenuItem rect;
	public MenuItem circle;
	public MenuItem polyLine;
	public MenuItem userDef;
	public MenuItem undo;
	public MenuItem redo;
	public MenuItem allClear;
	public MenuItem partClear;
	public MenuItem shutdown;
	
	// default constructor
	public MB_Set()
	{
		mb = new MenuBar();
		undoCount = 0;
		redoCount = 0;
		undoToken = 0;
		length_idx = 0;
		userDef_lineCount = 0;
		
		// 메뉴바에 대한 설정 수
		menu1 = new Menu("그리기");
		menu1.setGraphic(new ImageView("file:draw.png"));
        menu2 = new Menu("편집");
        menu2.setGraphic(new ImageView("file:edit.png"));
        menu3 = new Menu("색상");
        menu3.setGraphic(new ImageView("file:color.png"));
        menu4 = new Menu("드로잉 스타일");
        menu4.setGraphic(new ImageView("file:style.png"));
        menu5 = new Menu("기타");
        menu5.setGraphic(new ImageView("file:set.png"));
		        
        // 1. 그리기
		line = new MenuItem("선");
        line.setGraphic(new ImageView("file:line.png"));
        rect = new MenuItem("사각형");
        rect.setGraphic(new ImageView("file:rect.png"));
        circle = new MenuItem("원");
        circle.setGraphic(new ImageView("file:circle.png"));
        polyLine = new MenuItem("PolyLine");
        polyLine.setGraphic(new ImageView("file:polyLine.png"));
        userDef = new MenuItem("사용자 정의");
        userDef.setGraphic(new ImageView("file:userDef.png"));
		        
        // 2. 편집
        undo = new MenuItem("이전작업 취소     undo");
        redo = new MenuItem("다음단계 복원     redo");
        partClear = new MenuItem("지우개");
        allClear = new MenuItem("모두 지우기");
		               
        // 4. 드로잉 스타일
		        
        // 5. 기타
        shutdown = new MenuItem("프로그램 종료");
        shutdown.setOnAction(e-> {
       		System.exit(0); // 프로세스 종료
       	});
		        
		// 생성한 각 하위 메뉴항목들을 각 메인메뉴에 추가
		        
		// 메뉴1
		menu1.getItems().add(line);
        menu1.getItems().add(rect);
        menu1.getItems().add(circle);
        menu1.getItems().add(polyLine);
        menu1.getItems().add(userDef);
		       
        // 메뉴2
        menu2.getItems().add(undo);
        menu2.getItems().add(redo);
        menu2.getItems().add(partClear);
        menu2.getItems().add(allClear);
		       
        // 메뉴4
		        
        // 메뉴5
        menu5.getItems().add(shutdown);
        
        
        // 각 메뉴에 대한 동작
        line.setOnAction(event-> {
        	for(int i = 0; i < 10 ; i++) {
        		if(Drawer.use[i] == true) {
        			Drawer.use[i] =false;
        		}
        	}
        	Drawer.use[0] = true;
        	Drawer.drawLine(Main.canvas.tlgc, Main.canvas.btlgc, Main.toolBar);
        });
        
		rect.setOnAction(event-> {
			for(int i = 0; i < 10 ; i++) {
        		if(Drawer.use[i] == true) {
        			Drawer.use[i] =false;
        		}
        	}
			Drawer.use[1] = true;
			Drawer.drawRect(Main.canvas.tlgc, Main.canvas.btlgc, Main.toolBar);
        });
		
		circle.setOnAction(event-> {
			for(int i = 0; i < 10 ; i++) {
        		if(Drawer.use[i] == true) {
        			Drawer.use[i] =false;
        		}
        	}
			Drawer.use[2] = true;
			Drawer.drawCircle(Main.canvas.tlgc, Main.canvas.btlgc, Main.toolBar);
        });
		
		polyLine.setOnAction(event-> {
			for(int i = 0; i < 10 ; i++) {
        		if(Drawer.use[i] == true) {
        			Drawer.use[i] =false;
        		}
        	}
			Drawer.use[3] = true;
			Drawer.drawPolyLine(Main.canvas.tlgc, Main.canvas.btlgc, Main.toolBar);
			Drawer.counter = 0; // 다음 번에 다시 PolyLine을 그릴 수 있도록 counter 값을 0으로 초기화
        });
		
        userDef.setOnAction(event-> {
        	for(int i = 0; i < 10 ; i++) {
        		if(Drawer.use[i] == true) {
        			Drawer.use[i] =false;
        		}
        	}
        	Drawer.use[4] = true;
        	Drawer.drawUserDefine(Main.canvas.tlgc, Main.canvas.btlgc, Main.toolBar);
		});

        // undo 명령이 입력된 경우
        undo.setOnAction(event-> {
        	// 가장 최근에 수행한 명령이 사용자 정의 곡선인 경우 -> 저장된 사용자 정의곡선 요소의 길이만큼 index를 점프하여 redrawing 수행
        	current_idx = (Drawer.ht.count()-1); // 참조해야하는 마지막 인덱스 정보를 로드함
        	length_idx = (Drawer.ht.length_info_count()-1)-userDef_lineCount; // 가장 최근에 저장된 사용자 정의곡선의 길이의 인덱스 값을 불러옴
        	
        	if(reDrawer.detector(Drawer.ht.load(current_idx)) == 5) {
        		if(length_idx < 0) return;
        		int udc = Drawer.ht.length_load(length_idx); // 저장한 사용자 정의 곡선의 길이정보 값을 가져옴
        		
        		undoToken += udc; // undoToken의 값을 업데이트 -> 해당요소를 다음 그리기에 반영하지 않기 위함
        		reDrawer.redrawing(current_idx-undoToken, Main.canvas.tlgc, Main.canvas.btlgc);
        		undoCount++; // undo를 1회 수행하였으므로 값을 1증가
        		userDef_lineCount++; // 사용자 정의 곡선을 1개 읽었으므로 값을 1증가
        	}
        	else { // 그 이외의 경우? -> 현재 참조인덱스의 이전 인덱스까지 출력 -> 사용자 정의곡선 이외의 요소들은 전부 1개의 요소로 구성되므로
        		reDrawer.redrawing(current_idx-1, Main.canvas.tlgc, Main.canvas.btlgc);
        		undoToken++; // undoToken의 값을 업데이트 -> 해당 요소를 다음 그리기에 반영하지 않기 위함
            	undoCount++; // undo 명령 실행횟수를 1증가
        	}
        });

        // redo 명령이 입력된 경우
        redo.setOnAction(event-> {
        	if(undoCount > 0 && undoCount > redoCount) {
        		reDrawer.redrawing(Drawer.ht.count()-(undoCount+2)+redoCount+2, Main.canvas.tlgc, Main.canvas.btlgc);
            	redoCount++;
        	}
        });

       // allClear 명령이 입력된 경우
       allClear.setOnAction(event-> {
    	   Drawer.allClear(Main.canvas.tlgc, Main.canvas.btlgc); // 현재 기록한 모든 내용을 삭제함
       });
       
       // partClear 명령이 입력된 경우
       partClear.setOnAction(event-> {
    	   // 다른 명령을 수행할 때 해당 명령을 제외하고 나머지 동작의 수행을 비활서와 하기 위한 부분
    	   for(int i = 0; i < 10 ; i++) {
    		   if(Drawer.use[i] == true) {
    			   Drawer.use[i] =false;
    		   }
    	   }
    	   Drawer.use[5] = true;
    	   Drawer.partClear(Main.canvas.tlgc, Main.canvas.btlgc);
       });
       
        //메뉴바에 각 메인 메뉴들을 순차적으로 추가
        mb.getMenus().add(menu1);
        mb.getMenus().add(menu2);
        mb.getMenus().add(menu3);
        mb.getMenus().add(menu4);
        mb.getMenus().add(menu5);
	}
	
	public MenuBar load_MB()
	{
		return mb;
	}
}
