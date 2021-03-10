// 프로그램에서 다루고 있는 이미지를 관리하는 class
// 현재 프로그램에서 관리하는 이미지에 접근하기 위해서는 반드시 해당 class에 접근해야 한다.

import java.awt.image.BufferedImage;

// 작업되는 이미지의 최신상태를 유지하는 class
// -> 모든 함수는 그림을 그릴 때 해당 class에서 이미지를 가져다가 사용하도록 함

public class Current_Image
{
	private static BufferedImage image; // 가장 최근에 작업된 이미지를 저장하는 공간
	
	// 현재까지 작업한 이미지를 저장함
	public static void save(BufferedImage img)
	{
		image = img; // 인자로 전달받은 image값을 저장
	}
	
	// 가장 최근까지 작업한 이미지를 로딩함
	public static BufferedImage load()
	{
		return image; // 현재 저장하고 있는 image 값을 반환
	}
}
