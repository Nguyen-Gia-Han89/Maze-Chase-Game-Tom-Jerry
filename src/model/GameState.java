package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Lưu trạng thái hiện tại của trò chơi.
 * Bao gồm:
 * - Jerry và Tom
 * - Mê cung hiện tại
 * - Số Cheese còn lại
 * - Lượt hiện tại và thời gian còn lại trong lượt
 */
public class GameState {
	private Jerry jerry;
	private Tom tom;
	private MazeManager maze;
	private int remainingCheese; // số miếng Cheese còn trên bản đồ
	private TurnType turn; // lượt của ai đang di chuyển
	private int turnTimeLeft;
	
	private TurnType winner;
	private boolean paused = false;
    
	public GameState(Jerry jerry, Tom tom, MazeManager maze, int remainingCheese, TurnType turn, int turnTimeLeft) {
		super();
		this.jerry = jerry;
		this.tom = tom;
		this.maze = maze;
		this.remainingCheese = remainingCheese;
		this.turn = turn;
		this.turnTimeLeft = turnTimeLeft;
	}

	// ===== GETTTERS =====
	public Jerry getJerry() {
		return jerry;
	}

	public Tom getTom() {
		return tom;
	}

	public MazeManager getMaze() {
		return maze;
	}

	public int getRemainingCheese() {
		return remainingCheese;
	}
	

	public void setRemainingCheese(int remainingCheese) {
		this.remainingCheese = remainingCheese;
	}

	public TurnType getTurn() {
		return turn;
	}

	public void setTurn(TurnType turn) {
		this.turn = turn;
	}

	public TurnType getWinner() {
	    return winner;
	}

	public void setWinner(TurnType w) {
	    winner = w;
	}
    
	
	public int getTurnTimeLeft() {
		return turnTimeLeft;
	}

	public void setTurnTimeLeft(int turnTimeLeft) {
		this.turnTimeLeft = turnTimeLeft;
	}

	public boolean isPaused() {
	    return paused;
	}

	public void setPaused(boolean paused) {
	    this.paused = paused;
	}
	
	// ===== GAME LOGIC =====
    /**
     * Kiểm tra trò chơi kết thúc chưa
     * @return true nếu Jerry nhặt đủ cheese hoặc bị Tom bắt
     */
	public boolean isGameOver() {
	    // Tom thắng
	    if (jerry.getLives() <= 0) {
	        winner = TurnType.TOM;
	        return true;
	    }

	    // Jerry thắng
	    if (remainingCheese == 0 &&
	        jerry.getPosition().equals(maze.getCurrentMaze().getExitPosition())) {
	        winner = TurnType.JERRY;
	        return true;
	    }

	    return false;
	}    

	
	public void handleTomCatchJerry() {
	    if (tom.canCatchJerry(jerry.getPosition())) {
	        if (jerry.isHasShield()) {
	            jerry.setHasShield(false); 
	            // Có thể đẩy Tom lùi lại 1 ô hoặc giữ nguyên tùy bạn
	        } else {
	            jerry.setLives(jerry.getLives() - 1);
	            jerry.setPosition(maze.getCurrentMaze().getJerryStart());
	            tom.setPosition(maze.getCurrentMaze().getTomStart());
	            
	            if (jerry.getLives() <= 0) {
	                setWinner(TurnType.TOM);
	            }
	        }
	    }
	}

	//lượt di chuyển hiện tại của các nhân vật
    public boolean move(Direction direct) {
	    	GameCharacter current = (turn == TurnType.JERRY) ? jerry : tom;
	    	Position newPosition = current.getNextPosition(direct);
	    	
	    	// Tom k vào Cheese
	    	if (current instanceof Tom && maze.getCurrentMaze().isDenZone(newPosition)) {
	    		return false;
	    	}

    	
	    	if(maze.getCurrentMaze().canMove(newPosition)) {
    			current.move(direct); 
            
            if(current instanceof Jerry) {
            		current.getPosition();
            }else {
            		current.getPosition();
            }
            return true;
    		}
	    	return false;
    }
    
    //lượt di chuyển hiện tại có đạp trúng tăng tốc không
    public void stepOnSpeedBoost() {
		if (turn != TurnType.JERRY) return; // Tom không dùng speed

	    if (maze.getCurrentMaze().hasSpeedBoost(jerry.position)) {
	        maze.getCurrentMaze().removeSpeedBoost(jerry.position);
	        jerry.setSpeed(SpeedBoostItem.getSpeed() - 1);
	        jerry.setNumOfStep(3);
	    }
	}
    
    //lượt di chuyển hiện tại có đạp trúng Shield không
    public void stepOnShield() {
    		GameCharacter current = (turn == TurnType.JERRY) ? jerry : tom;
        if(maze.getCurrentMaze().isShieldItem(current.position)) {
            	maze.getCurrentMaze().removeShieldItem(current.position);
            	current.hasShield=true;
        }
    }
  
  //lượt di chuyển hiện tại có đạp trúng bẫy không
    public void stepOnTrap() {
    		GameCharacter current = (turn == TurnType.JERRY) ? jerry : tom;
        if(maze.getCurrentMaze().hasTrap(current.position)) {
        	if (!current.hasShield) {
                // 🔥 QUAN TRỌNG: ép mất lượt
                current.forceEndTurn(); 
                if (current instanceof Jerry) {
                		tom.setSpeed(1);
                } else {
                		jerry.setSpeed(1);
                }
            } else {
                current.setHasShield(false);
            }
        	
            	maze.getCurrentMaze().removeTrap(current.getPosition());
        }
    	
    }
  //lượt di chuyển hiện tại có đạp trúng Cheese không
    public void checkCheese() {
        if (turn == TurnType.JERRY) {
	        	if(maze.getCurrentMaze().hasCheese(jerry.getPosition())) {
	            	maze.getCurrentMaze().removeCheese(jerry.getPosition());
	            	jerry.collectCheese();
	            	jerry.setSpeed(2);
	            	remainingCheese--;
	        }
        }
    }
    
    public void changeNextTurnType() {

        if (turn == TurnType.TOM) {
            // Nếu Tom còn speed → tiếp tục lượt và giảm speed
            if (tom.getTotalSpeed() > 0) {
                tom.subTotalSpeed(); 
                if(tom.getSpeed()>0) {
                	tom.setSpeed(-1);
                }
                return;
            }
            // Hết speed → đổi lượt
            turn = TurnType.JERRY;
            tom.resetSpeed();

        } else { // Lượt Jerry
            // Nếu Jerry còn speed → tiếp tục lượt và giảm speed
            if (jerry.getTotalSpeed()>0) {
                jerry.subTotalSpeed();
                if(jerry.getSpeed()>0) {
                	jerry.setSpeed(-1);
                }
                return;
            }
            if(jerry.getNumOfStep()>0) {
            	jerry.setSpeed(SpeedBoostItem.getSpeed());
            	jerry.subNumOfStep();
        	}
            // Hết speed → đổi lượt
            turn = TurnType.TOM;
            jerry.resetSpeed();
        }
    }
    
 // ==== 5. EVALUATE (AI) =====
    /**
     * Hàm đánh giá trạng thái cho thuật toán Minimax.
     * Điểm càng cao → càng tốt cho JERRY.
     */
    public int evaluate() {
        Maze mazeObj = this.maze.getCurrentMaze();
        Position jPos = jerry.getPosition();
        Position tPos = tom.getPosition();

        int score = 0;

        // 1. Điểm từ cheese Jerry
        score += jerry.getCheeseCollected() * 50;

        // 2. Khoảng cách tới cheese hoặc exit
        if (!jerry.checkWinCondition(Cheese.getNumber())) {
            int closestCheeseDist = shortestPathDistance(jerry, mazeObj.getCheesePositions(), jerry);
            score += (50 - closestCheeseDist * 5);
        } else {
            int distanceToExit = shortestPathDistance(jerry, mazeObj.getExitPosition(), jerry);
            score += (50 - distanceToExit * 20);
        }

        // 3. Khoảng cách tới Tom (đe dọa)
        int distTomToJerry = shortestPathDistance(tom, jPos, tom);
        score += distTomToJerry * 10;
        // Nếu Tom rất gần Jerry → giảm mạnh điểm
        if(distTomToJerry <= 6 && jerry.getSpeed() <= 1) {
            score -= 30;
        }

        // 4. Trap
        if (mazeObj.hasTrap(jPos)) {
            score += jerry.hasShield ? 5 : -30;
        }

        // 5. Shield thuận lợi
        if (!jerry.hasShield && mazeObj.isShieldItem(jPos))
            score += 10;

        // 6. Speed boost
        if (mazeObj.hasSpeedBoost(jPos))
            score += 20;

        // 7. Tom như mối đe dọa
        if (tom.hasShield)
            score -= 10;
        if (tom.getSpeed() > 1)
            score -= 20;
        if (mazeObj.hasTrap(tPos) && !tom.hasShield)
            score += 10;

        // 8. Tom camp cheese: nếu Tom gần cheese zone → giảm score Jerry
        for(Position cheesePos : mazeObj.getCheesePositions()) {
            int distTomToCheese = shortestPathDistance(tom, cheesePos, tom);
            if(distTomToCheese <= 1) { // Tom đứng sát cheese
                score -= 20; // Jerry khó nhặt cheese
            }
        }

        return score;
    }

    // ===== BFS TÌM ĐƯỜNG =====
    private int shortestPathDistance(GameCharacter character, Position target, GameCharacter cloneCharacter) {
        Maze mazeObj = this.maze.getCurrentMaze();
        Position start = cloneCharacter.getPosition();
        if (start.equals(target))
            return 0;

        boolean[][] visited = new boolean[mazeObj.getLayout().length][mazeObj.getLayout()[0].length];
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        visited[start.getX()][start.getY()] = true;

        int distance = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            distance++;
            for (int i = 0; i < size; i++) {
                Position curr = queue.poll();

                for (Direction dir : Direction.values()) {
                    Position next = curr.getNextPosition(dir);

                    if (!mazeObj.canMove(next) || visited[next.getX()][next.getY()])
                        continue;

                    // Tom không đi vào cheese zone khi target != Jerry
                    if (character instanceof Tom && mazeObj.isDenZone(next)) {
                    	    continue;
                    	}


                    if(next.equals(target))
                        return distance;

                    visited[next.getX()][next.getY()] = true;
                    queue.add(next);
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private int shortestPathDistance(GameCharacter character, List<Position> targets, GameCharacter cloneCharacter) {
        int minDist = Integer.MAX_VALUE;
        for (Position target : targets) {
            int dist = shortestPathDistance(character, target, cloneCharacter);
            if (dist < minDist)
                minDist = dist;
        }
        return minDist;
    }


	// ===== CLONE STATE ====
	/**
	 * Tạo bản sao GameState → dùng cho AI Minimax.
	 */
	public GameState clone() {
		// Clone Jerry
		Jerry clonedJerry = this.jerry.clone();

		// Clone Tom
		Tom clonedTom = this.tom.clone();

		// Clone MazeManager (bao gồm cả Maze bên trong)
		MazeManager clonedMaze = this.maze.clone();

		// Tạo GameState mới với tất cả bản sao
		GameState clonedState = new GameState(clonedJerry, clonedTom, clonedMaze, this.remainingCheese, this.turn,
				this.turnTimeLeft);

		return clonedState;

	}
	
	// ===== AI HELPER (Dùng để mô phỏng move) =====

	/**
	 * Áp dụng việc di chuyển cho một nhân vật trong trạng thái game này (thường là
	 * trạng thái clone).
	 * * @param character Bản sao của Jerry hoặc Tom.
	 * @param nextPos Vị trí mới cần di chuyển tới.
	 * @return true nếu vị trí của nhân vật được cập nhật thành công, false nếu vị trí
	 * mới nằm ngoài phạm vi hoặc không hợp lệ.
	 */
	public boolean applyMove(GameCharacter character, Position nextPos) {
	    if (!this.maze.getCurrentMaze().canMove(nextPos)) {
	        return false;
	    }

	    // 2. Cập nhật vị trí của nhân vật clone
	    character.setPosition(nextPos);

	    // 3. Cập nhật vị trí bắt đầu mới trong Maze hiện tại
	    // Điều này quan trọng để các lần gọi BFS/logic sau này dùng đúng vị trí mới.
	    if (character instanceof Jerry) {
	        // Cần cập nhật vị trí của Jerry trong GameState hiện tại (là bản clone)
	        this.jerry.setPosition(nextPos);
	        this.maze.getCurrentMaze().setJerryStart(nextPos);
	    } else if (character instanceof Tom) {
	        // Cần cập nhật vị trí của Tom trong GameState hiện tại (là bản clone)
	        this.tom.setPosition(nextPos);
	        this.maze.getCurrentMaze().setTomStart(nextPos);
	    }

	    // 4. Các logic khác (nhặt cheese, dẫm bẫy) CÓ THỂ được gọi ở đây,
	    // nhưng trong Minimax đơn giản, ta chỉ cần cập nhật vị trí và tính điểm.

	    return true;
	}

}