// 현재 프로그램에서 다루고 있는 이미지를 흑백으로 처리해주는 method

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;

public class Gray_Scale
{	
	public static BufferedImage image;
	public static JLabel label;
	public static int width;
	public static int height;
	
	// 현재 프로그램에서 다루고 있는 이미지를 흑백화 하여서 다시 출력
	public static void gray_scale()
	{
		try
		{
	         image = Loading_Image.img; // 이전에 열은 이미지를 가져옴
	         width = image.getWidth();
	         height = image.getHeight();
	         
	         for(int i=0; i<height; i++) {
	            for(int j=0; j<width; j++) {
	               Color c = new Color(image.getRGB(j, i));
	               int red = (int)(c.getRed() * 0.299);
	               int green = (int)(c.getGreen() * 0.587);
	               int blue = (int)(c.getBlue() *0.114);
	               Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
	               image.setRGB(j,i,newColor.getRGB());
	            }
	         }
	         // 처리가 완료된 이미지가 출력될 수 있도록 Main Class의 method를 호출
	         Main.updateToFrameImg(image);
	      } catch (Exception e) {
	    	  Main.showWarnning("처리할 이미지가 없습니다. 이미지를 먼저 선택해 주세요.");
	    	  // -> 처리할 이미지가 없는 상태에서 이미지 처리를 시도하는 경우
	      }
	}
}
