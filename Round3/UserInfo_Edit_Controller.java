import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// 사용자 정보를 수정하는 페이지에 대한 컨트롤을 다루는 class

public class UserInfo_Edit_Controller
{
	@FXML TextField id;          // 사용자 입력 id 정보를 다루는 부분 - 수정불가
	@FXML PasswordField pw;      // 사용자 입력 pw 정보를 다루는 부분
	@FXML PasswordField re_pw;   // 확인을 위해 pw를 재입력하는 부분
	@FXML TextField name;        // 사용자 입력 이름정보를 다루는 부분
	@FXML DatePicker date;       // 사용자 생일에 대한 정보를 다루는 부분
	@FXML TextField phone;       // 사용자 입력 전화번호를 다루는 부분
	@FXML TextField mail;        // 사용자 입력 메일주소를 다루는 부분
	
	@FXML Button save;    // 변경한 내용을 저장하는 버튼
	@FXML Button cancle;  // 내용 변경을 취소하는 버튼
	
	// 각 버튼의 입력에 대한 동작을 다루는 부분
	@FXML // save 버튼이 클릭된 경우
	public void save()
	{
		// 사용자가 재입력한 정보를 바탕으로 객체를 다시 생성
		SignUp_Data update = new SignUp_Data();
		update.setUserID(id.getText());
		update.setUserPW(pw.getText());
		update.setUserName(name.getText());
		String d = date.getValue().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		update.setUserBD(d);	
		update.setUserPN(phone.getText());
		update.setUserMail(mail.getText());
		
		DB_Connection.edit_UserInfo(update); // 사용자에게 입력받은 정보를 바탕으로 사용자 정보 업데이트
	}
	
	@FXML // 내용변경을 취소하는 버튼 -> 내용변경을 취소하고 해당 페이지를 종료함
	public void cancle()
	{
		Stage stage = (Stage)cancle.getScene().getWindow();
		stage.close(); // 내용변경을 취소하고 해당 윈도우를 닫는다.
	}
}
