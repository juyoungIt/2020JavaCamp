// 사용자가 회원가입 시 입력하는 정보들을 다루는 부분

public class SignUp_Data
{
	private String userID;     // 사용자 id
	private String userPW;     // 사용자 비밀번호
	private String userName;   // 사용자 이름
	private String userBD;     // 사용자 생일정보
	private String userPN;     // 사용자 전화번호
	private String userMail;   // 사용자 이메일 정보
	
	
	// 기본 생성자, 모든 사용자 정보를 null로 초기화
	public SignUp_Data()
	{
		userID = "null";
		userPW = "null";
		userName = "null";
		userBD = "null";
		userMail = "null";
		userPN = "null";
	}
	
	public void setUserID(String s)
	{
		userID = s;
	}
	
	public void setUserPW(String s)
	{
		userPW = s;
	}
	
	public void setUserName(String s)
	{
		userName = s;
	}
	
	public void setUserBD(String s)
	{
		userBD = s;
	}
	
	public void setUserPN(String s)
	{
		userPN = s;
	}
	
	public void setUserMail(String s)
	{
		userMail = s;
	}
	
	// 사용자 ID값을 가져옴
	public String getUserID()
	{
		return userID;
	}
	
	// 사용자 PW값을 가져옴
	public String getUserPW()
	{
		return userPW; 
	}
	
	// 사용자 이름을 가져옴
	public String getUserName()
	{
		return userName;
	}
	
	// 사용자 생일정보를 가져옴
	public String getUserBD()
	{
		return userBD;
	}
	
	// 사용자 전화번호 정보를 가져옴
	public String getUserPN()
	{
		return userPN;
	}
	
	// 사용자 이메일 정보를 가져옴
	public String getUserMail()
	{
		return userMail;
	}
}
