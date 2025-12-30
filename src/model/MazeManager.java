package model;

import java.util.ArrayList;
import java.util.List;

/**
 * MazeManager quản lý danh sách các Maze trong trò chơi. Bao gồm việc thêm
 * maze, lấy maze hiện tại và điều hướng giữa các maze.
 */
public class MazeManager {

	/** Danh sách các Maze trong trò chơi */
	private List<Maze> mazeList;

	/** Chỉ số Maze hiện tại đang được chơi trong danh sách */
	private int currentMazeIndex;

	/**
	 * Constructor mặc định. Khởi tạo danh sách maze trống và bắt đầu từ maze đầu tiên (index 0)
	 */
	public MazeManager() {
		this.mazeList =new ArrayList<Maze>();
		this.currentMazeIndex = 0;
	}

	// ====== Getter ======
	public int getCurrentMazeIndex() {
		return currentMazeIndex;
	}

	

	public List<Maze> getMazeList() {
		return mazeList;
	}

	/**
	 * Lấy Maze hiện tại.
	 *
	 * @return Maze hiện tại nếu có, null nếu danh sách rỗng hoặc chỉ số không hợp lệ
	 */
	public Maze getCurrentMaze() {
		if (mazeList.isEmpty()) {
			return null;
		}
		if (currentMazeIndex < 0 || currentMazeIndex >= mazeList.size()) {
			return null;
		}
		return mazeList.get(currentMazeIndex);
	}

	/**
	 * Thêm một Maze mới vào danh sách.
	 *
	 * @param m Maze cần thêm, không được null
	 */
	public void addMaze(Maze m) {
		if (m != null) {
			mazeList.add(m);
		}
	}

	/**
	 * Chuyển sang Maze tiếp theo trong danh sách. Nếu đang ở maze cuối cùng, không làm gì.
	 */
	public void nextMaze() {
	    if (currentMazeIndex < mazeList.size() - 1) {
	        currentMazeIndex++;
	    } else {
	        // Nếu là màn cuối, có thể quay về màn 0 hoặc báo thắng cuộc
	        System.out.println("Đã hết map!");
	    }
	}

	/**
	 * Quay lại Maze trước đó trong danh sách. Nếu đang ở maze đầu tiên, không làm gì.
	 */
	public void previousMaze() {
		if (currentMazeIndex > 0) {
			currentMazeIndex--;
		}
	}
	
	/**
     * Clone toàn bộ MazeManager để phục vụ Minimax.
     * - Tạo MazeManager mới.
     * - Clone từng Maze bên trong.
     * - Giữ nguyên currentMazeIndex.
     * 
     * @return MazeManager bản sao hoàn chỉnh
     */
	public MazeManager clone() {
        MazeManager cloned = new MazeManager();
        // clone từng Maze trong danh sách
        for(Maze m : this.mazeList) {
            cloned.mazeList.add(m.clone());
        }
        cloned.currentMazeIndex = this.currentMazeIndex;
        return cloned;
    }

	
	/**
	 * Chọn mê cung hiện tại dựa trên chỉ số trong danh sách maze.
	 * @param index
	 */
	public void setCurrentMaze(int index) {
	    if(index >= 0 && index < mazeList.size()) {
	        currentMazeIndex = index;
	    }
	}
}
