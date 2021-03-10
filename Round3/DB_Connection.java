// java와 mariadb를 연결하는 커넥터

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.ObservableList;

 
public class DB_Connection {
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection con;
    private static PreparedStatement pst;
    private static Statement st;
    private static ResultSet rs;
    
    // DB에 접속
    public DB_Connection()
    {
    	try {
            Class.forName(driver); // 드라이버를 로드
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/round3?serverTimezone=UTC",
                    "root",
                    "jy152629");
            if( con != null ) {
                System.out.println("DB 접속 성공");
                con.close(); // 연결확인이 목적이므로 연결 확인 후 연결을 종료함
            }
            
        } catch (ClassNotFoundException e) { 
            System.out.println("드라이버 로드 실패");
        } catch (SQLException e) {
            System.out.println("DB 접속 실패");
            e.printStackTrace();
        }
    }
    
    // DB 테이블에 데이터를 추가하는 method
 	public static void user_insert(SignUp_Data userData)
 	{
 		try
 		{
 			con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/round3?serverTimezone=UTC",
                    "root",
                    "jy152629");
 			// 정보를 저장하는 sql문 생성
 			String sql = String.format("insert into userInfo values('%s','%s','%s','%s','%s','%s')", 
 					userData.getUserID().trim(), userData.getUserPW().trim(), userData.getUserName().trim(), 
 					userData.getUserBD().trim(), userData.getUserPN().trim(), userData.getUserMail().trim());
 		
 			pst = con.prepareStatement(sql);
 			pst.executeUpdate();
 		}
 		catch(Exception e) {
 			e.printStackTrace();
 		}
 		finally
 		{
 			try
 			{
 				// connection과 statement를 닫음
 				if(con != null && !con.isClosed())
					con.close();
				if(pst != null && !pst.isClosed())
 					pst.close();
 			}
			catch(SQLException e){
				e.printStackTrace();
			}
 		}
 	}
 	
 	// 인자로 전달받은 ID가 가지는 PW값을 DB에서 조회하여 반환
 	public static String userLoad(String id)
 	{
 		String password = ""; // 로딩하는 PW를 저장

 		try
 		{
 			con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/round3?serverTimezone=UTC",
                    "root",
                    "jy152629");
 			st = con.createStatement();
 			// db에 전달할 명령어를 작성함
 			String sql = "SELECT userPW FROM userInfo WHERE userID = " + "'" + id + "'";
 			rs = st.executeQuery(sql); // 해당 SQL 명령문을 전달 및 수행
 			while(rs.next())
 				password = rs.getString("userPW");
 		}
 		catch(Exception e){
 			e.printStackTrace();
 		}
 		finally {
 			try
 			{
 				// connection과 statement를 닫음
 				if(con != null && !con.isClosed())
 					con.close();
 				if(st != null && !st.isClosed())
 					st.close();
 			}
 			catch(SQLException e){	}
 		}
 		return password; // 조회한 비밀번호 값을 return
 	}
 	
 	
 	// 입력받은 정보를 바탕으로 사용자 개인정보를 수정
 	public static void edit_UserInfo(SignUp_Data updateData)
 	{
 		try
 		{
 			// DB와의 연결을 생성
 			con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/round3?serverTimezone=UTC",
                    "root",
                    "jy152629");
 			st = con.createStatement();
 			
 			// 관련 명령을 수행하기 위한 sql 문을 생성함
 			
 			String sql = String.format("UPDATE userInfo SET userPW='%s',userName='%s' WHERE userID='%s'", 
 					updateData.getUserPW().trim(), updateData.getUserName().trim(),updateData.getUserID().trim());
 			pst = con.prepareStatement(sql);
 			pst.executeUpdate();
 			
 			String sq2 = String.format("UPDATE userInfo SET userBD='%s',userPN='%s',userMail='%s' WHERE userID='%s'", 
 					updateData.getUserBD().trim(), updateData.getUserPN().trim(),
 					updateData.getUserMail().trim(), updateData.getUserID().trim());
 			pst = con.prepareStatement(sq2);
 			pst.executeUpdate();

 		}
 		catch(Exception e){
 			e.printStackTrace();
 		}
 		finally {
 			try
 			{
 				// connection과 statement를 닫음
 				if(con != null && !con.isClosed())
 					con.close();
 				if(st != null && !st.isClosed())
 					st.close();
 			}
 			catch(SQLException e){	}
 		}
 	}
 	
 	// 탈퇴 -> 사용자 개인정보 모두 삭제
 	public static void delete_UserInfo(String userID)
 	{
 		try
 		{
 			// DB와의 연결을 생성
 			con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/round3?serverTimezone=UTC",
                    "root",
                    "jy152629");
 			st = con.createStatement();
 			
 			// 관련 명령을 수행하기 위한 sql 문을 생성함
 			
 			String sql = String.format("DELETE from userInfo WHERE userID='%s'", userID);
 			pst = con.prepareStatement(sql);
 			pst.executeUpdate();

 		}
 		catch(Exception e){
 			e.printStackTrace();
 		}
 		finally {
 			try
 			{
 				// connection과 statement를 닫음
 				if(con != null && !con.isClosed())
 					con.close();
 				if(st != null && !st.isClosed())
 					st.close();
 			}
 			catch(SQLException e){	}
 		}
 	}
 	
 	// table에 저장된 모든 정보를 로드함
 	public static ArrayList<String> load_allInfo()
 	{
 		ArrayList<String> list = new ArrayList<String>(); // 로드하는 내용을 저장할 arrayList 생성
 		
 		try
 		{
 			// DB와의 연결을 생성
 			con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/round3?serverTimezone=UTC",
                    "root",
                    "jy152629");
 			st = con.createStatement();
 			
 			// 관련 명령을 수행하기 위한 sql 문을 생성함
 			String sql = "SELECT * FROM userInfo";
 			// db에 전달할 명령어를 작성
 			rs = st.executeQuery(sql); // 해당 SQL 명령문을 전달 및 수행
 			while(rs.next()) {
 				String id = rs.getString(1);
 				String pw = rs.getString(2);
 				String name = rs.getString(3);
 				String bd = rs.getString(4);
 				String phone = rs.getString(5);
 				String mail = rs.getString(6);
 				
 				list.add(id);
 				list.add(pw);
 				list.add(name);
 				list.add(bd);
 				list.add(phone);
 				list.add(mail);
 			}
 		}
 		catch(Exception e){
 			e.printStackTrace();
 		}
 		finally {
 			try
 			{
 				// connection과 statement를 닫음
 				if(con != null && !con.isClosed())
 					con.close();
 				if(st != null && !st.isClosed())
 					st.close();
 			}
 			catch(SQLException e){	}
 		}
 		return list; // 생성한 배열을 반환
 	}
}