import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

// Undo, Redo를 위한 이미지의 백업을 처리하는 공간

public class BackUp
{
	private static ArrayList<BufferedImage> storage; // 프로그램에서 다루는 이미지에 대한 백업을 처리하는 공간
	
	// 기본 생성자 -> storage에 기본적인 저장공간을 할당함
	public BackUp()
	{
		// 20개의 처리된 이미지를 저장할 수 있는 공간을 우선적으로 할당
		storage = new ArrayList<BufferedImage>(20);
		// -> 이후에 저장내용이 기존 할당치를 초과하면 ArrayList의 특성에 따라 공간을 늘려서 메모리 공간을 재할당함
	}
	
	// 입력받은 BufferedImage type의 이미지를 백업
	public static void push(BufferedImage img)
	{
		BufferedImage save = deepCopy(img); // 전달받은 이미지를 deepCopy
		storage.add(save); // 해당 이미지를 백업
		// 모니터링을 위한 테스트 코드
		System.out.println("현재 저장된 데이터의 수 : " + storage.size());
	}
	
	// 인자로 전달한 인덱스에 저장된 이미지를 로딩
	public static BufferedImage pop(int idx)
	{
		return storage.get(idx); // 가장 최근에 저장한 인덱스의 이미지를 반환
	}
	
	// 인자로 전달한 특정 인덱스 이후 인덱스의 저장내용들을 모두 삭제함
	public static void cut(int idx)
	{
		int count = 0;
		// storage에 저장된 모든 요소에 접근하기 위한 것이므로 img를 별도로 다른 곳에 사용하지는 않음
		for(BufferedImage img : storage) {
			count++;
			// 인자로 전달받은 인덱스부터 그 뒤의 모든 내용들을 삭제함
			if(count > idx+1)
				storage.add(count-1, null);
		}
	}
	
	// 현재 백업된 데이터의 수를 반환함
	public static int size()
	{
		return storage.size();
	}
	
	// 인자로 받은 bufferedImage를 deepCopy해서 반환함 -> 백업과정 뿐만 아니라 다양한 작업에서 활용하는 method
	public static BufferedImage deepCopy(BufferedImage bi)
	{
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
}
