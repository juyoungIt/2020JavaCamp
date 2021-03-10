import java.util.ArrayList;

// 생성하는 그림 객체에 대한 정보를 모두 저장하는 class

public class History
{
	public ArrayList<Shapes> hist; // 백업공간
	public ArrayList<Integer> length_hist; // 입력되는 길이에 대한 백업공간
	
	// default constructor
	public History()
	{
		hist = new ArrayList<Shapes>(1000);
		length_hist = new ArrayList<Integer>(100);
	}
	
	// 생성한 Shapes 객체를 저장
	public void save(Shapes sp)
	{
		hist.add(sp);
	}
	
	// 산출하여 구한 사용자 정의 곡선의 길이 값을 저장
	public void length_save(Integer length)
	{
		length_hist.add(length);
	}
	
	// 백업공간에 저장된 Shapes의 수를 반환
	public int count()
	{
		return hist.size();
	}
	
	// 백업공간에 저장된 Shapes의 수를 반환
	public int length_info_count()
	{
		return length_hist.size();
	}
	
	// 가장 최근에 저장한 값을 로드
	public Shapes load()
	{
		return hist.get(count()-1);
	}
	
	// 가장 최근에 저장한 사용자 정의곡선의 길이를 로드
	public Integer length_load()
	{
		return length_hist.get(length_info_count()-1);
	}
	
	// 특정 index를 지정하여 Shapes 조회, overloading
	public Shapes load(int idx)
	{
		return hist.get(idx);
	}
	
	// 특정 index를 지정하여 길이정보 조회, overloading
	public Integer length_load(int idx)
	{
		return length_hist.get(idx);
	}
}
