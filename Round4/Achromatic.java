import java.awt.Color;
import java.awt.image.BufferedImage;

// 불러온 이미지를 무채색화 하여 출력해주는 기능과 관련된 class

public class Achromatic
{
	public static BufferedImage image; // 다루는 이미지를 저장
	public static int width; // 해당 이미지의 가로 픽셀의 수
	public static int height; // 해당 이미지의 세로 픽셀의 수
	public static long total_red = 0; // 모든 red 컬러코드값의 합
	public static long total_green = 0; // 모든 green 컬러코드값의 합
	public static long total_blue = 0; // 모든 blue 컬러코드값의 합
	public static long ave_red;
	public static long ave_green;
	public static long ave_blue;
	
	// 불러온 이미지를 무채색화 하여 출력해주는 기능을 하는 class
	public static void achromaicImage()
	{
		/*
		<해당 기능 구현에 대한 아이디어>
		-> 무채색 계열의 색상의 경우 rgb 3색상의 컬로코드값이 모두 동일하다는 특징이 있음
		-> 그래서 픽셀의 컬러코드값 획득, 셋 중에서 가장 큰 값으로 컬러코드값 통일 -> 무채색화
		 */
		
		try
		{
	         image = Current_Image.load();  // 현재 프로그램에서 유지하고 있는 이미지를 가져옴
	         width = image.getWidth();   // 해당 이미지의 가로 픽셀의 수를 구함
	         height = image.getHeight(); // 해당 이미지의 세로 픽셀의 수를 구함
	         
	         // 현재 다루고 있는 이미지의 모든 픽셀이 가지는 컬러코드의 평균치를 계산함
	         for(int i=0; i<height; i++) {
	            for(int j=0; j<width; j++) {
	               Color c = new Color(image.getRGB(j, i)); // 해당 픽셀의 컬러값을 얻어옴
	               
	               // 색상이 있던 픽셀들을 백색 픽셀과 흑색 픽셀로만 구분하여서 픽셀의 색상값을 교체함 -> 무채색화
	               // 우선 해당 픽셀의 3가지 색상코드 값을 획득함
	               total_red += c.getRed();
	               total_green += c.getGreen();
	               total_blue += c.getBlue();
	            }
	         }
	         // 각 rgb 컬러별 컬러코드의 평균치 계산
	         long totalCount = image.getWidth()*image.getHeight();
	         ave_red = total_red/totalCount;
	         ave_green = total_green/totalCount;
	         ave_blue = total_blue/totalCount;
	         System.out.println(ave_red + " : " + ave_green + " : " + ave_blue);
	         
	         // 현재 다루고 있는 이미지의 모든 픽셀에 대해서 다음을 반복함
	         for(int i=0; i<height; i++) {
	            for(int j=0; j<width; j++) {
	               Color c = new Color(image.getRGB(j, i)); // 해당 픽셀의 컬러값을 얻어옴
	              
	               // -> 3가지 컬러값을 가장 큰 값 하나로 통일
	               
	               // 색상이 있던 픽셀들을 백색 픽셀과 흑색 픽셀로만 구분하여서 픽셀의 색상값을 교체함 -> 무채색화
	               // 우선 해당 픽셀의 3가지 색상코드 값을 획득함
	               int red = c.getRed();
	               int green = c.getGreen();
	               int blue = c.getBlue();
	               
	               // 다음 조건을 적용하여 이미지를 픽셀단위로 다시 그림
	               if(red<ave_red || green<ave_green || blue < ave_blue) {
	            	   red = 0;
	            	   green = 0;
	            	   blue = 0;
	               }
	               else {
	            	   red = 255;
	            	   green = 255;
	            	   blue = 255;
	               }
	               
	               // 산출한 값을 바탕으로 새로운 컬러값 생성 
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
