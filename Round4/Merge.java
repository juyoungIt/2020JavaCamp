import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

// 이미지 합성을 처리하는 클래스
// 입력으로 들어온 2개의 이미지를 선택

public class Merge
{
	public static BufferedImage mergeResult; // 합성된 결과 이미지를 저장
	public static int width;   // 새롭게 생성할 합성 이미지의 가로의 너비
	public static int height;  // 새롭게 생성할 합성 이미지의 세로의 길이
	// -> 픽셀단위의 통제 및 연산을 위해서 사이즈가 다른 2개의 이미지를 서로 같은 사이즈로 맞춰서 합성을 진행해줄 필요성이 있음
	
	// 2개의 이미지를 불러와서 합성 후 해당 결과를 보여주는 method
	public static void imageMerge()
	{
		// 사용자가 저장하는 2개 이상의 파일을 로딩
		JFileChooser fileLoader = new JFileChooser(); // 로컬저장소에서 파일을 불러오기 위한 객체
		fileLoader.setMultiSelectionEnabled(true); // 한번에 여러개의 이미지를 선택할 수 있도록 함
		int result = fileLoader.showOpenDialog(Main.window); // 사용자가 파일을 선택할 수 있는 창을 윈도우 형식으로 띄움
		
		// 사용자가 확인버튼을 클릭하여 파일을 선택한 경우
		if(result == JFileChooser.APPROVE_OPTION)
		{
			File[] selected = fileLoader.getSelectedFiles(); // 불러온 파일을 파일의 배열에 저장
			
			
			// 합성할 2개의 이미지를 불러와서 BufferedImage의 형태로 다음을 저장한다.
			// 합성할 이미지들의 BufferedImage 형태의 저장선언
			// 최대 합성할 수 있는 이미지의 수를 5개로 제한함
			BufferedImage[] mergeImage = new BufferedImage[5];
			
			// 사용자 선택하여 불러온 파일을 모두 BufferedImage의 배열에 저장한다.
			// 사용자가 선택한 파일에 대한 정보를 차례로 읽어와서 이를 BufferedImage의 형태로 변환하여 저장함
			if(selected.length <= 1)
			{
				Main.showWarnning("병합을 진행할 만큼의 충분한 이미지가 로딩되지 못했습니다.");
				return;
			}
			for(int i=0 ; i<selected.length ; i++)
			{	
				try
				{	
					mergeImage[i] = ImageIO.read(selected[i]);
				}
				catch(Exception e)
				{
					Main.showWarnning("병합을 진행할 만큼의 충분한 이미지가 로딩되지 못했습니다.");
					return;
				}
			}
			
			// 불러온 두 이미지 파일의 가로, 세로길이를 비교해서 둘다 더 긴 길이를 새로운 합성 이미지의 크기로 선정
			// 두 이미지를 모두 포함할 수 있는 사이즈로 선정함으로서 중복되지 않도록 하게 하기 위함
			width = Math.max(mergeImage[0].getWidth(), mergeImage[1].getWidth());
			height = Math.max(mergeImage[0].getHeight(), mergeImage[1].getHeight());
			
			// 선정한 merge 결과 이미지의 크기르 바탕으로 빈 이미지 파일 생성
			mergeResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			int red[][] = new int [mergeResult.getHeight()][mergeResult.getWidth()];   // Red
			int blue[][] = new int [mergeResult.getHeight()][mergeResult.getWidth()];  // Blue
			int green[][] = new int [mergeResult.getHeight()][mergeResult.getWidth()]; // Green
			
			// 각 픽셀에 해당하는 rgb별 코드값을 저장하는 배열선언 -> 각 배열을 정수형 코드값을 저장함
			for(int y=0 ; y<height ; y++)
			{
				for(int x=0 ; x<width ; x++)
				{
					Color color1 = new Color(mergeImage[0].getRGB(x, y)); // 첫번재 이미지를 구성하는 픽셀의 색상 값을 로드함
					Color color2 = new Color(mergeImage[1].getRGB(x, y)); // 두번째 이미지를 구성하는 픽셀의 색상 값을 로드함
					
					/*
					// 두 이미지의 배경을 적절하게 판단해서 두 이미지를 합성함
					// 배경이 백색인 경우
					if((color1.getRed() <= 255 && color1.getRed()> 220)
							&& (color1.getGreen() <= 255 && color1.getGreen()> 220) 
							&& (color1.getBlue() <= 255 && color1.getBlue()> 220))
					{
						red[y][x] = color2.getRed();
						blue[y][x] = color2.getBlue();
						green[y][x] = color2.getGreen();
					}
					else if((color2.getRed() <= 255 && color2.getRed()> 220)
							&& (color2.getGreen() <= 255 && color2.getGreen() > 220) 
							&& (color2.getBlue() <= 255 && color2.getBlue() > 220))
					{
						red[y][x] = color1.getRed();
						blue[y][x] = color1.getBlue();
						green[y][x] = color1.getGreen();
					}
					else
					{
						red[y][x] = color1.getRed();
						blue[y][x] = color1.getBlue();
						green[y][x] = color1.getGreen();
					}
					*/
					
					// 배경이 핑크인 경우
					if((color1.getRed() <= 210 && color1.getRed() > 160)
							&& (color1.getGreen() <= 120 && color1.getGreen() > 75) 
							&& (color1.getBlue() <= 175 && color1.getBlue() > 110))
					{
						red[y][x] = color2.getRed();
						blue[y][x] = color2.getBlue();
						green[y][x] = color2.getGreen();
					}
					else if((color1.getRed() <= 210 && color1.getRed() > 160)
							&& (color1.getGreen() <= 120 && color1.getGreen() > 75) 
							&& (color1.getBlue() <= 175 && color1.getBlue() > 110))
					{
						red[y][x] = color1.getRed();
						blue[y][x] = color1.getBlue();
						green[y][x] = color1.getGreen();
					}
					else
					{
						red[y][x] = color1.getRed();
						blue[y][x] = color1.getBlue();
						green[y][x] = color1.getGreen();
					}
					
					// 산출한 결과로 해당 픽셀의 색상을 결정하여 입력
					Color adjustedColor = new Color(red[y][x], green[y][x], blue[y][x]);
					mergeResult.setRGB(x, y, adjustedColor.getRGB());
				}
			}
			Current_Image.save(mergeResult); // 처리결과를 현재 상태 유지를 유지를 위해 현재 상태로 업데이트
			Main.updateToFrameImg(mergeResult); // 변경된 이미지를 프레임에 업데이트
		}	
	}
}