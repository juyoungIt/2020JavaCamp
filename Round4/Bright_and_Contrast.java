import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

// 현재 프로그램에서 관리하고 있는 이미지의 밝기와 대비를 변경시켜주는 class
// 해당 항목선택 시 값의 제어를 위해 측면에 툴바를 추가하는 작업을 진행할 계획임

public class Bright_and_Contrast
{
	public static BufferedImage image; // 현재 프로그램에서 다루고 있는 이미지를 가져오는 부분
	public static int runCode = -1; // 밝기 및 대비 조절 작업에 대한 실행코드를 저장 -> 해당 작업의 수행과 마침을 지정하여 백업시점을 결정하기 위함
	/*
	 <runCode value matching>
	 0 - 밝기조절, 대비조절 이외에 다른 처리를 수행했거나 수행한 처리가 존재하지 않는 경우
	 1 - 밝기조절
	 2 - 대비조절
	 */
	
	// 사용자가 전달하는 값을 바탕으로 해당 이미지의 밝기를 조절함
	public static void adjustBright(BufferedImage image, int bright)
	{
		// 이전에 대비조절을 수행한 기록이 있는 경우
		if(runCode == 2)
		{
			Current_Image.save(image); // 현재 이미지 상태를 최신 이미지 상태로 최신화
			BackUp.push(image); // 해당 이미지에 대한 백업을 진행
			runCode = 1; // runCode를 1로 변경함
		}
		
		if(runCode == 0)
			runCode = 1;
		
		if(BackUp.size() == 0)
			Main.showWarnning("처리할 이미지가 없습니다. 이미지를 먼저 선택해 주세요.");
		BufferedImage tmpImg = BackUp.deepCopy(image); // 현재 프로그램에서 다루는 이미지를 deepCopy(중간 작업내용이 영향을 미치지 못하도록 하기 위함)
		
		// 각 픽셀에 해당하는 rgb별 코드값을 저장하는 배열선언 -> 각 배열을 정수형 코드값을 저장함
		int red[][] = new int [tmpImg.getHeight()][tmpImg.getWidth()];   // Red
		int blue[][] = new int [tmpImg.getHeight()][tmpImg.getWidth()];  // Blue
		int green[][] = new int [tmpImg.getHeight()][tmpImg.getWidth()]; // Green
		
		// 이미지의 픽셀들에 대한 값을 다시 조정하는 부분
		for(int y=0 ; y<tmpImg.getHeight() ; y++)
		{
			for(int x=0 ; x<tmpImg.getWidth() ; x++)
			{
				Color color = new Color(tmpImg.getRGB(x, y)); // 현재 조회 중인 픽셀의 색상 값을 로드함
				// 사용자가 입력하는 값에 따라서 사용하는 사진의 밝기값을 결정함
				red[y][x] = color.getRed() + bright;
				blue[y][x] = color.getBlue() + bright;
				green[y][x] = color.getGreen() + bright;
				
				// 컬러코드 값이 지정한 영역을 벗어나지 않도록 픽셀이 가지는 가지는 색상값을 재조정함
				red[y][x] = Math.max(0, red[y][x]); 
				blue[y][x] = Math.max(0, blue[y][x]); 
				green[y][x] = Math.max(0, green[y][x]);
				red[y][x] = Math.min(255, red[y][x]); 
				blue[y][x] = Math.min(255, blue[y][x]); 
				green[y][x] = Math.min(255, green[y][x]);
				
				Color adjustedColor = new Color(red[y][x], green[y][x], blue[y][x]);
				tmpImg.setRGB(x, y, adjustedColor.getRGB());
			}
		}
		Main.updateToFrameImgDoNotBackUp(tmpImg); // 변경된 이미지를 프레임에 업데이트
		// -> 슬라이드 바에 의해서 계속 값이 변경될 가능성이 있으므로 백업을 진행하지 않고 적용한 내용만 업데이트 
	}
	
	// 사용자가 전달하는 값을 바탕으로 해당 이미지의 대를 조절함 -> 조정이 약간 애매하게 되는 부분이 있는 것 같다
	public static void adjustContrast(BufferedImage image, int cont)
	{
		// 이전에 대비조절을 수행한 기록이 있는 경우
		if(runCode == 1)
		{
			Current_Image.save(image); // 현재 이미지 상태를 최신 이미지 상태로 최신화
			BackUp.push(image); // 해당 이미지에 대한 백업을 진행
			runCode = 2; // runCode를 1로 변경함
		}
		
		if(runCode == 0)
			runCode = 2; // runCode를 2로 변경
		
		BufferedImage tmpImg = BackUp.deepCopy(image);
		RescaleOp rescale = new RescaleOp((float)(cont/100.0), 0, null);
		Current_Image.save(tmpImg); // 변경되는 내용을 현재 이미지 상태로 유지 -> 서로 연동되도록 하기 위함
		Main.updateToFrameImgDoNotBackUp(rescale.filter(tmpImg, null));
		// -> 슬라이드 바에 의해서 계속 값이 변경될 가능성이 있으므로 백업을 진행하지 않고 적용한 내용만 업데이트
	}
}
