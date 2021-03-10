// 현재 프로그램에서 다루고 있는 이미지를 저장하는 기능을 지원하는 class

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

// 현재 프로그램에서 다루고 있는 이미지를 저장하는 내용을 다루는 class

public class Save_Image
{
    // 인자로 전달받은 파일의 내용을 저장
    public static void save()
    {
    	// 저장 시 사용자에게 제공하는 인터페이스를 창에 띄우기 위함
    	JFileChooser fileChooser = new JFileChooser(); // filechooser 객체선언
    	
    	//기본 Path의 경로 설정 -> 최초로 창을 띄웠을 때 기본적으로 접근하는 경 
    	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));

        try
        {
        	BufferedImage image = Current_Image.load(); // 현재 저장되어 있는 정보를 로딩함
        	int result = fileChooser.showSaveDialog(Main.window);
        	// 저장을 진행한 경우
        	if(result == 0)
        	{
        		String fileSavePath = fileChooser.getSelectedFile().getAbsolutePath(); // 사용자가 파일을 저장하는 절대경로 값을 가져옴
        		String fileName = fileChooser.getSelectedFile().getName(); // 사용자가 지정한 파일저장명을 가져옴
            	
        		
        		File output = new File(fileSavePath + "." + "png"); // 확장자를 png로 지정하여 저장
            	output.renameTo(new File(fileName)); // 사용자가 입력한 내용으로 저장할 파일의 이름을 설정함
                ImageIO.write(image, "png", output); // 해당 파일을 로컬저장소에 저장함
        	}
        }
        catch(Exception e)
        {
        	Main.showWarnning("처리할 이미지가 없습니다. 이미지를 먼저 선택해 주세요.");
        	// -> 처리할 이미지가 존재하지 않는 상태에서 저장을 시도하는 경우
        }

    }
}
