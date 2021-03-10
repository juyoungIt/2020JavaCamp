import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignIn_Controller
{	
	@FXML private TextField input_id;     // 사용자 ID를 입력하는 부분
	@FXML private TextField input_pw;     // 사용자 password를 입력하는 부분
	@FXML private Label login_statement;  // 로그인 상태를 출력해주는 Label
	@FXML private Button login;           // ID/PW 입력 후 로그인 시도를 요청하는 버튼
	@FXML private Button signup;          // 회원가입 페이지를 요청하는 버튼
	@FXML private Button quit;            // 로그인 창을 나가는 버튼
	
	public FXMLLoader loader;
	public Parent root;
	public Stage stage;
	public static SignIn_Data logData;
	
	@FXML // login 버튼을 누른 경우에 대한 동작
	public void login()
	{    
        // 로그인 시 입력하는 정보를 로드하여 별도의 객체에 저장
        logData = new SignIn_Data(input_id.getText(), input_pw.getText());
        String pwFromDB = DB_Connection.userLoad(logData.getID());
        System.out.println(pwFromDB);
        
        // 로그인 성공 시 사용자에게 다른 화면을 보여주고, 실패한 경우 메시지로 알려줌
        if(pwFromDB.equals(logData.getPW())) {
        	// 로그인 후 사용자 정보를 띄우는 새로운 창을 로드함
        	// 관리자(admin)인 경우 -> 관리자용 윈도우를 별도로 띄움
        	if(logData.getID().equals("admin"))
        	{
        		try
        		{
            		Stage logStage = (Stage)login.getScene().getWindow(); // 로그인 창에 대한 정보를 얻어옴
        			loader = new FXMLLoader(getClass().getResource("SuperUser.fxml"));
        			root = (Parent) loader.load();
        			stage = new Stage();
        			stage.setTitle("welcome! Super User");
        			stage.setScene(new Scene(root));
        			stage.show();
        			logStage.close(); // 기존에 있던 로그인 창을 닫는다.
        		}
        		catch(Exception e){
        			e.printStackTrace();
        		}
        	}
        	else
        	{
        		try
        		{
            		Stage logStage = (Stage)login.getScene().getWindow(); // 로그인 창에 대한 정보를 얻어옴
        			loader = new FXMLLoader(getClass().getResource("NormalUser.fxml"));
        			root = (Parent) loader.load();
        			stage = new Stage();
        			stage.setTitle("welcome!");
        			stage.setScene(new Scene(root));
        			stage.show();
        			logStage.close(); // 기존에 있던 로그인 창을 닫는다.
        		}
        		catch(Exception e){
        			e.printStackTrace();
        		}
        	}
        }
        else {
        	login_statement.setTextFill(Color.RED);
        	login_statement.setText("Login Failed, please check your ID or Password");
        }
	}
	
	@FXML // 회원가입 버튼을 누른 경우에 대한 동작(로그인 창에서의 회원가입 버튼)
	public void signup()
	{
		SignUp_Controller controller = new SignUp_Controller();
		controller.pop(); // 회원가입 창을 실행
	}
	
	@FXML // quit 버튼을 누른 경우에 대한 동작
	protected void quit()
	{
		System.exit(0); // 프로세스를 종료함
	}
}
