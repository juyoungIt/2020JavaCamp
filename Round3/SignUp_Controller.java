import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUp_Controller
{
	// 가독성 향상을 위해 화면 배치 순서대로 정렬함
	@FXML private TextField id;             // 사용자 ID를 입력하는 부분
	@FXML private Button douleCheck;        // ID에 대한 중복확인을 요청하는 버튼
	@FXML private Label doubleCheck_state;  // ID 중복확인 상태를 알려주는 Label
	
	@FXML private PasswordField pw;             // 사용자 password를 입력하는 부분
	@FXML private PasswordField re_pw;          // 재확인을 위해 PW를 입력하는 부분
	@FXML private Label pwCheck_state;      // pw를 2회 정확하게 입력했는지에 대한 상태를 표시
	
	@FXML private TextField name;           // 사용자 이름을 입력하는 부분
	
	@FXML private DatePicker bd;            // 사용자 생일정보를 입력하는 부분
	
	@FXML private TextField phone;    // 사용자 전화번호를 입력하는 부분
	
	@FXML private TextField mail;    // 사용자 메일주소를 입력하는 부분
	
	@FXML private Button signUp;       // 회원가입 버튼
	@FXML private Button cancleButton; // 취소버튼
	
	public FXMLLoader loader;
	public Parent root;
	public Stage stage;

	@FXML
	public void pop()
	{
		try
		{
			loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
			root = (Parent) loader.load();
			stage = new Stage();
			stage.setTitle("Kakao Bank Sign Up");
			stage.setScene(new Scene(root));
			stage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 각 버튼들에 대한 동작
	@FXML // 회원가입 버튼을 누른 경우에 대한 동작
	public void signUp()
	{
		// 회원가입 페이지를 통해서 입력받는 데이터를 저장하는 객체 생성
		SignUp_Data data = new SignUp_Data();
		
		// 사용자 정보를 객체에 전달
		data.setUserID(id.getText());
		data.setUserPW(pw.getText());
		data.setUserName(name.getText());
		String d = bd.getValue().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		data.setUserBD(d);	
		data.setUserPN(phone.getText());
		data.setUserMail(mail.getText());
		
		System.out.println("user_insert method 수행");
		DB_Connection.user_insert(data); // 해당 객체를 전달하여 DB에 정보 저장
	}
	
	@FXML // 취소버튼을 누른 경우에 대한 동작
	public void cancleButton()
	{
		Stage stage = (Stage)cancleButton.getScene().getWindow();
		stage.close(); // 회원가입 과정을 취소하고 입력을 종료함.
	}
}
