// 사용자가 로그인 시 입력하는 데이터를 다루는 class

public class SignIn_Data
{
	private String id; // 사용자 입력 ID
	private String pw; // 사용자 입력 PW
	
	// 기본 생성자 -> 모든 요소에 대한 값을 null로 초기화함
	public SignIn_Data()
	{
		id = "null";
		pw = "null";
	}
	
	// 생성자, 입력받은 id, pw 값으로 필드 초기화
	public SignIn_Data(String id, String pw)
	{
		this.id = id;
		this.pw = pw;
	}
	
	// 사용자가 입력한 ID data를 로드함
	public String getID()
	{
		return this.id;
	}
	
	// 사용자가 입력한 PW data를 로드함
	public String getPW()
	{
		return this.pw;
	}
}
