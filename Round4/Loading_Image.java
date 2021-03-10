// 이미지를 외부에서 로딩하는 동작을 기술하는 class

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Loading_Image
{
	public static BufferedImage img; // 로딩하는 이미지 파일 관련 정보를 저장
	
	// 입력받은 파일을 로딩해주는 method
	public static void load(File file)
	{
		try
		{
			img = ImageIO.read(file); // 이미지파일을 읽어와서 bufferedImage에 넣음
			Current_Image.save(img); // 가져온 이미지 파일의 상태를 기록
			Main.updateToFrameImg(img);  // 불러온 이미지 파일을 프레임에 업데이트
		}
		catch(Exception e) { }
	}
}
