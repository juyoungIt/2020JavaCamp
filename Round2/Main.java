// 처리결과를 화면에 출력하기 위한 프로그램의 main 부분

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// 실행을 위한 main method 부분
public class Main extends Application {
	
	// 윈도우 구성에 필요한 핵심요소 호출
	public static final MB_Set menu = new MB_Set(); // 메뉴바 생성
    public static final TB_Set toolBar = new TB_Set(); // 툴바 생성
    public static final CV_Set canvas = new CV_Set();  // canvas 생성
	
    public static void main(String[] args) {
    	Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	
        // Scene에 사용할 레이아웃 생성 및  관련설정
        VBox root = new VBox(); // container class로 VBox를 사용
        
        root.getChildren().add(menu.load_MB());     // 레이아웃에 MenuBar 추가
        root.getChildren().add(toolBar.load_TB());  // 레이아웃에 툴바추가
        root.getChildren().add(canvas.load_CV());   // 레이아웃에 canvas 추가
        
        // Stage에 attach 할 Scene 생성
        Scene scene = new Scene(root, 1800, 1000);

        // Stage에 생성한 Scene 전달 -> 구현 UI 실행 및 출력
        primaryStage.setTitle("Round2 - Graphic Editor"); // stage가 생성하는 윈도우 제목
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}