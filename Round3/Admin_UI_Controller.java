import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class Admin_UI_Controller implements Initializable
{
	@FXML private TableView<TableRowDataModel> table; // 전체적인 자료를 담는 테이블
	@FXML private TableColumn<TableRowDataModel, String> idColumn;
	@FXML private TableColumn<TableRowDataModel, String> pwColumn;
	@FXML private TableColumn<TableRowDataModel, String> nameColumn;
	@FXML private TableColumn<TableRowDataModel, String> bdColumn;
	@FXML private TableColumn<TableRowDataModel, String> phoneColumn;
	@FXML private TableColumn<TableRowDataModel, String> mailColumn;
	
	@FXML private Button addUserInfo;
	@FXML private Button editUserInfo;
	@FXML private Button delUserInfo;
	@FXML private Button logout;
	
	public static FXMLLoader loader;
	public static Parent root;
	public static Stage stage;
	
	public ObservableList<TableRowDataModel> userList = FXCollections.observableArrayList(); // 테이블 내용저장
	public ArrayList<String> list; // DB로 부터 불러오는 데이터를 저장하는 부분
      
	// DB에서 데이터를 읽어서 위의 선언한 배열에 저장하는 과정 -> 그래야 Table view로 해당 내용을 열람할 수 있기 때문
	public Admin_UI_Controller()
	{
		list = DB_Connection.load_allInfo(); // 전달한 배열을 통해서 DB에 저장된 모든 정보를 로드함
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources)
	{
		// table에 로드한 정보를 삽입
		/*
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        pwColumn.setCellValueFactory(cellData -> cellData.getValue().pwProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        bdColumn.setCellValueFactory(cellData -> cellData.getValue().bdProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        mailColumn.setCellValueFactory(cellData -> cellData.getValue().mailProperty());
        */
		/*
		userList.add(new TableRowDataModel(new SimpleStringProperty(list.get(0)), new SimpleStringProperty(list.get(1)),
				new SimpleStringProperty(list.get(2)), new SimpleStringProperty(list.get(3)),
				new SimpleStringProperty(list.get(4)), new SimpleStringProperty(list.get(5)));
		*/
        // table.setItems(userList); // 테이블에 불러온 내용을 추가
	}
	
	// 각 버튼들에 대한 동작을 정의한 부분
	@FXML // 관리자 창에서 사용자 추가 버튼을 클릭한 경우
	public void addUser()
	{
		
	}
	
	@FXML // 관리자 창에서 사용자 정보 수정 버튼을 클릭한 경우
	public void editUser()
	{
		
	}
	
	@FXML // 관리자 창에서 사용자 정보 삭제 버튼을 클릭한 경우
	public void delUser()
	{
		
	}
	
	@FXML
	public void logout()
	{
		// 연결을 취소하고 로그인 창을 다시 띄운다.
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
