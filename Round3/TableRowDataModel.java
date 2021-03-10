//table에 대한 내용을 다루는 class

import com.mysql.cj.conf.StringProperty;

public class TableRowDataModel
{
	private StringProperty id;
	private StringProperty pw;
	private StringProperty name;
	private StringProperty bd;
	private StringProperty phone;
	private StringProperty mail;
	
	public TableRowDataModel(StringProperty s1, StringProperty s2, StringProperty s3,
			StringProperty s4, StringProperty s5, StringProperty s6)
	{
		this.id = s1;
		this.pw = s2;
		this.name = s3;
		this.bd = s4;
		this.phone = s5;
		this.mail = s6;
	}
	
	public StringProperty idProperty()
	{
		return id;
	}
	
	public StringProperty pwProperty()
	{
		return pw;
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public StringProperty bdProperty()
	{
		return bd;
	}
	
	public StringProperty phoneProperty()
	{
		return phone;
	}
	
	public StringProperty mailProperty()
	{
		return mail;
	}	
}