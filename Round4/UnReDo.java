// Undo, Redo 옵션에 대한 동작을 기술하는 class

public class UnReDo
{
	private static int undoCount = 0; // undo를 한 횟수를 저장
	private static int currentIdx; // 현재 참조하는 인덱스 값을 저장
	private static int currentSize;    // 현재 백업공간에 저장된 이미지의 수를 저장
	
	public static void undo()
	{
		currentSize = BackUp.size() - 1; // 가장 최근에 데이터가 저장된 인덱스를 로딩
		currentIdx = currentSize - undoCount;
		if(currentIdx < 0) {
			Main.showWarnning("더 이상 되돌릴 작업이 없습니다.");
			return;
		}
		// 백업을 진행하지 않고 해당 이미지를 프레임에 업데이트 함
		Main.updateToFrameImgDoNotBackUp(BackUp.pop(currentIdx));
		Current_Image.save(BackUp.pop(currentIdx)); // 현재 유지하는 이미지에 반영
		// -> undo나 redo의 경우 프레임이 업데이트하는 내용을 백업할 필요가 없기 때문
		undoCount++; // undoCount의 값을 1증가
	}
	
	public static void redo()
	{
		// Undo를 하지 않은 상태에서 undo를 시도하는 경우
		if(undoCount <= 0) {
			Main.showWarnning("최근에 Undo한 기록이 없습니다.");
			return;
		}
		// 되돌린 기록이 존재하는 경우
		else
		{
			undoCount--; // redo 했으므로 undo Count의 값을 1 감소시킴
			Main.updateToFrameImgDoNotBackUp(BackUp.pop(currentSize-undoCount));
			Current_Image.save(BackUp.pop(currentSize-undoCount)); // 현재 유지하는 이미지에 반영
		}
	}
}
