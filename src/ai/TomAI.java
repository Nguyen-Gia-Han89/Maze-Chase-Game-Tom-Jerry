package ai;

import model.GameState;
import model.Jerry;
import model.Direction;
import model.GameCharacter;
import model.Position;

/**
 * TomAI là bộ điều khiển trí tuệ nhân tạo cho Tom.
 * Sử dụng thuật toán Minimax để tìm nước đi tối ưu
 * nhằm bắt Jerry. 
 *
 * Ý tưởng:
 * - Tom = Min (cố gắng giảm điểm của Jerry)
 * - Jerry = Max (tăng điểm của mình)
 * - Mỗi lượt, AI dự đoán các trạng thái trong MAX_DEPTH bước
 *   và chọn hướng đi tốt nhất.
 */
public class TomAI {
	private final int MAX_DEPTH = 4;

	/**
     * Lấy hướng di chuyển tốt nhất cho Tom dựa trên trạng thái hiện tại.
     * @param state Trạng thái Game hiện tại
     * @return Direction hướng đi tối ưu (UP, DOWN, LEFT, RIGHT)
     */
	public Direction getBestMove(GameState state) {
        // Tom là Min, cố gắng tìm giá trị Min (giảm điểm)
        int bestValue = Integer.MAX_VALUE; // Khởi tạo với giá trị lớn nhất
        Direction bestMove = null;

        for (Direction dir : Direction.values()) {
            GameState nextState = simulateMove(state, state.getTom(), dir);
            if(nextState == null) continue;
            
            // Lượt tiếp theo là của Jerry (Max)
            int value = minimax(nextState, MAX_DEPTH - 1, true); 

            if (value < bestValue) {
                bestValue = value;
                bestMove = dir;
            }
        }

        return bestMove;
    }

	/**
     * Minimax cơ bản (không có Alpha-Beta).
     * Hàm này được mô phỏng theo logic trong hình ảnh.
     * @param state Trạng thái game hiện tại
     * @param depth Độ sâu còn lại
     * @param isJerryTurn true nếu lượt Jerry (Max), false nếu lượt Tom (Min)
     * @return giá trị đánh giá cho node này
     */
    private int minimax(GameState state, int depth, boolean isJerryTurn) {
        // cơ sở
        if (depth == 0 || state.isGameOver()) {
            return state.evaluate();
        }

        // đệ quy
        if (isJerryTurn) {
            // Jerry = Max
            int temp = Integer.MIN_VALUE;
            
            for (Direction dir : Direction.values()) {
                GameState nextState = simulateMove(state, state.getJerry(), dir);
                if(nextState == null) continue;
                
                int value = minimax(nextState, depth - 1, false);
                
                if (value > temp) {
                    temp = value;
                   // ghi lại node / state đang xét
                }
            }
            // return
            return temp;
        } else {
            // Tom = Min
            int temp = Integer.MAX_VALUE;
           
            for (Direction dir : Direction.values()) {
                GameState nextState = simulateMove(state, state.getTom(), dir);
                if(nextState == null) continue;

                int value = minimax(nextState, depth - 1, true);
                
                if (value < temp) {
                    temp = value;
                    // ghi lại node / state đang xét (Ở đây chỉ cần giá trị)
                }
            }
            // return
            return temp;
        }
    }

    /**
     * Mô phỏng di chuyển một nhân vật.
     * Clone GameState hiện tại, di chuyển nhân vật đến vị trí mới.
     * @param state Trạng thái game hiện tại
     * @param c Nhân vật (Tom hoặc Jerry)
     * @param dir Hướng di chuyển
     * @return GameState mới sau khi di chuyển, hoặc null nếu đi vào tường
     */
    private GameState simulateMove(GameState state, GameCharacter c, Direction dir) {
        Position nextPos = c.getNextPosition(dir);
        if (!state.getMaze().getCurrentMaze().isWall(nextPos)) {
            GameState clone = state.clone();
            clone.applyMove(c instanceof Jerry ? clone.getJerry() : clone.getTom(), nextPos);
            return clone;
        }
        return null;
    }
}
