import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class NormalUser_UI_Controller
{	
	@FXML private Button page;      // 마이페이지 버튼
	@FXML private Button edit;      // 개인정보 수정버튼
	@FXML private Button out;       // 탈퇴 버튼
	@FXML private Button logout;    // 로그아웃 버튼
	
	public static FXMLLoader loader;
	public static Parent root;
	public static Stage stage;
	
	@FXML // 마이페이지 버튼을 누른 경우에 대한 동작
	public void userInfo_show()
	{
		/*
		 1. 현재 로그인 한 id를 기억하자! -> 이걸 어떻게 할 것인지가 관건
		 */
	}
		
	@FXML // 개인정보 수정 버튼을 누른 경우에 대한 동작
	public void userInfo_edit()	
	{
		// 개인정보 수정 창을 열음
		try
		{
			loader = new FXMLLoader(getClass().getResource("Edit_Page.fxml"));
			root = (Parent) loader.load();
			stage = new Stage();
			stage.setTitle("User Information Edit");
			stage.setScene(new Scene(root));
			stage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
		
	@FXML // 개인정보 삭제(탈퇴) 버튼을 누른 경우에 대한 동작
	public void userInfo_del()
	{
		String id = SignIn_Controller.logData.getID(); // 로그인 시 사용한 ID값을 기억
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("사용자 계정 삭제");
		alert.setHeaderText("탈퇴 하시겠습니까?");
		alert.setContentText("");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
		} else {
		    // ... user chose CANCEL or closed the dialog
		}

		출처: https://studymake.tistory.com/583 [스터디메이크]
		DB_Connection.delete_UserInfo(id); // 해당 ID를 가진 계정을 삭제함
	}
	
	@FXML // 로그아웃 버튼을 누른 경우에 대한 동작
	public void logout()
	{
		// 연결을 취소하고 로그인 창을 다시 띄운다.
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("로그아웃");
		alert.setHeaderText("로그아웃 하시겠습니까?");
		alert.setContentText("로그아웃 이전에 모든 내용을 저장했는 지 확인해주시기 바랍니다.");
		try
		{
			Stage ustage = (Stage)logout.getScene().getWindow();
			loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
			root = (Parent) loader.load();
			stage = new Stage();
			stage.setTitle("login page");
			stage.setScene(new Scene(root));
			stage.show();
			ustage.close(); // 기존의 사용자 창을 닫고 로그인 창을 다시 띄운다.
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
