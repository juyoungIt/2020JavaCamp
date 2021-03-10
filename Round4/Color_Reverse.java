import java.awt.Color;
import java.awt.image.BufferedImage;

// 현재 관리 중인 이미지의 색상을 반전시켜주는 기능에 대한 class

public class Color_Reverse
{
	public static BufferedImage image; // 다루는 이미지를 저장
	public static int width; // 해당 이미지의 가로 픽셀의 수
	public static int height; // 해당 이미지의 세로 픽셀의 수
	
	// 현재 프로그램에서 조회 중인 이미지의 색상을 반전시켜주는 method
	public static void reverse()
	{
		try
		{
	         image = Current_Image.load();  // 현재 프로그램에서 유지 중인 이미지를 가져옴
	         width = image.getWidth();   // 해당 이미지의 가로 픽셀의 수를 구함
	         height = image.getHeight(); // 해당 이미지의 세로 픽셀의 수를 구함
	          
	         // 현재 다루고 있는 이미지의 모든 픽셀에 대해서 다음을 반복함
	         for(int i=0; i<height; i++) {
	            for(int j=0; j<width; j++) {
	               Color c = new Color(image.getRGB(j, i)); // 해당 픽셀의 컬러값을 얻어옴
	               // 픽셀의 색상코드 값을 반전시킴
	               int red = (int)(255 - c.getRed());
	               int green = (int)(255 - c.getGreen());
	               int blue = (int)(255 - c.getBlue());
	               // 산출한 값을 바탕으로 새로운 컬로 코드값을 생성
	               Color newColor = new Color(red,green,blue);
	               // 해당 픽셀의 컬러코드값을 새롭게 생성한 코드값으로 변경
	               image.setRGB(j,i,newColor.getRGB());
	            }
	         }
	         // 처리가 완료된 이미지가 출력될 수 있도록 Main class의 method를 호출함
	         Main.updateToFrameImg(image);
	      } catch (Exception e) {
	    	  Main.showWarnning("처리할 이미지가 없습니다. 이미지를 먼저 선택해 주세요.");
	      }
	}
}
