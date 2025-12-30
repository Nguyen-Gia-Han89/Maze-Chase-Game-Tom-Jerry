package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ai.TomAI;
import model.Direction;
import model.GameState;
import model.Maze;
import model.MazeManager;
import model.TurnType;
import view.GameFrame;
import view.MazePanel;

public class GameController {

	private GameState state;
	private MazePanel panel;
	private Timer turnTimer;
	private Direction lastPlayerDirection;
	private final int TURN_TIME_SEC = 10;

	public GameController(GameState state, MazePanel panel) {
		this.state = state;
		this.panel = panel;
	}
	
	public void startTurn() {
	    state.setTurnTimeLeft(TURN_TIME_SEC);
	    startTimer();

	    if (state.getTurn() == TurnType.TOM) {
	        tomAutoMove();
	    }
	    System.out.println(state.getTom().toString() + "\n" + state.getJerry().toString());
	}


	private void startTimer() {
	    if (turnTimer != null) {
	        turnTimer.cancel();
	        turnTimer = null;
	    }

	    turnTimer = new Timer();
	    turnTimer.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	            if (state.isPaused()) return;

	            int timeLeft = state.getTurnTimeLeft() - 1;
	            state.setTurnTimeLeft(timeLeft);
	            panel.repaint();

	            if (timeLeft <= 0) {
	                turnTimer.cancel();
	                handleTurnTimeout();
	            }
	        }
	    }, 1000, 1000);
	}


	private void handleTurnTimeout() {
		System.out.println("Hết giờ → đổi lượt");

		// Lượt kết thúc do hết giờ
		state.changeNextTurnType();
		setLastPlayerDirection(null);

		startTurn(); // Reset thời gian cho lượt mới
	}

	/** Bắt đầu game — KHÔNG dùng vòng lặp while */
	public void startGame() {
		startTurn(); // Khởi động lượt đầu tiên
	}

	/** Khi người chơi hoặc Tom đi xong → kết thúc lượt */
	public void move(Direction dir) {
		if (state.isPaused()) return;
		if (state.getTurn() != TurnType.JERRY)
			return;

		boolean moved = state.move(dir);

		if (moved) {

			// Kích hoạt các hiệu ứng
			state.stepOnSpeedBoost();
			state.stepOnShield();
			state.stepOnTrap();
			state.checkCheese();
			
			panel.repaint();

			if (state.isGameOver()) {
				endGame(state.getWinner());
				return;
			}

			// Chuyển lượt
			state.changeNextTurnType();
			lastPlayerDirection = null;

			startTurn(); // Reset lại 10 giây
		}
	}

	/** Người chơi nhấn phím → gọi hàm này từ KeyListener */
	public void playerMove() {
		if (state.isPaused()) return;
		if (state.getTurn() != TurnType.JERRY)
			return;

		if (lastPlayerDirection != null) {
			move(lastPlayerDirection);
		}
	}

	/** Tom tự đi khi đến lượt */
	public void tomAutoMove() {
		if (state.getTurn() != TurnType.TOM)
			return;
		tomMove();
	}

	private void endGame(TurnType winner) {
        stopGame();
        panel.repaint();

        // Lấy GameFrame cha
        GameFrame frame = (GameFrame) SwingUtilities.getWindowAncestor(panel);

        String title = (winner == TurnType.JERRY) ? "CHIẾN THẮNG!" : "THẤT BẠI!";
        String msg = (winner == TurnType.JERRY) ? "Jerry đã thoát thành công!" : "Tom đã bắt được Jerry!";

        Object[] options;
        if (winner == TurnType.JERRY) {
            options = new Object[]{"Trở về", "Chọn màn", "Chơi tiếp"};
        } else {
            options = new Object[]{"Trở về", "Chọn màn", "Chơi lại"};
        }

        int choice = JOptionPane.showOptionDialog(panel, msg, title,
                JOptionPane.DEFAULT_OPTION, 
                winner == TurnType.JERRY ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE,
                null, options, options[0]);

        if (frame == null) return;

        if (winner == TurnType.JERRY) {
            switch (choice) {
                case 0 -> frame.backToMenu(); 
                case 1 -> frame.goToMapSelect();
                case 2 -> nextMaze();
            }
        } else {
            switch (choice) {
                case 0 -> frame.backToMenu();
                case 1 -> frame.goToMapSelect();
                case 2 -> { replayCurrentMaze(); startTurn(); }
            }
        }
    }

	public Direction getLastPlayerDirection() {
		return lastPlayerDirection;
	}

	public void setLastPlayerDirection(Direction dir) {
		this.lastPlayerDirection = dir;
		playerMove(); // Jerry di chuyển ngay khi nhấn phím
	}

	/**
	 * Di chuyển Tom dựa trên AI. Gọi AI để chọn hướng đi tốt nhất, di chuyển 1 ô.
	 * Sau đó chuyển lượt sang Jerry.
	 */
	public void tomMove() {
		if (state.isPaused()) return;
		if (state.getTurn() != TurnType.TOM)
			return;

		if (turnTimer != null)
			turnTimer.cancel();

		TomAI ai = new TomAI();

		Direction dir = ai.getBestMove(state);
		System.out.println("Tom's turn. AI selected: " + dir);
		if (dir != null) {
			state.move(dir); // move 1 ô
			
			state.stepOnShield();
		    state.stepOnTrap();
		}

		// reset speed sau lượt
		panel.repaint();
		
		state.handleTomCatchJerry();

		if (state.isGameOver()) {
			endGame(state.getWinner());
			return;
		}

		// sau khi Tom đi xong → chuyển sang Jerry
		state.changeNextTurnType();
		lastPlayerDirection = null;
		startTurn();
	}


	
	
	
	
	
	
	
	

	
	public void nextMaze() {
	    MazeManager mazeManager = state.getMaze();
	    
	    // 1. Chuyển sang map tiếp theo trong danh sách
	    mazeManager.nextMaze(); 
	    
	    // 2. Gọi hàm reset đặc biệt cho chuyển màn
	    setupMazePositions(); 
	    
	    // 3. Bắt đầu lượt mới
	    startTurn();
	}

	/**
	 * Hàm hỗ trợ thiết lập vị trí nhân vật khi đổi Map hoặc Chơi lại
	 */
	private void setupMazePositions() {
	    Maze current = state.getMaze().getCurrentMaze();
	    if (current == null) return;

	    // Reset các vật phẩm trên bản đồ về trạng thái ban đầu (nếu có logic này)
	    current.resetItems();

	    // QUAN TRỌNG: Cập nhật vị trí bắt đầu mới từ file Map mới nạp
	    state.getJerry().setPosition(current.getJerryStart());
	    state.getTom().setPosition(current.getTomStart());

	    // Reset thông số nhân vật
	    state.getJerry().reset(); 
	    state.getTom().reset();

	    // Cập nhật lại số phô mai cần ăn của Map mới
	    state.setRemainingCheese(current.getCheesePositions().size());

	    // Reset trạng thái Game
	    state.setWinner(null);
	    state.setTurn(TurnType.JERRY);

	    panel.repaint();
	}

	 /**
	    * Chơi lại maze hiện tại:
	    * - Reset Jerry/Tom về vị trí start
	    * - Reset speed, shield
	    * - Reset items, cheese, traps trên map
	    * - Đặt lượt bắt đầu là Jerry
	    */
	public void replayCurrentMaze() {
	    setupMazePositions();
	    startTurn();
	    panel.requestFocusInWindow();
	}
	/**
    * Chọn maze theo index từ MazeManager
    * @param index Index maze muốn chơi
    */
	public void selectMaze(int index) {
	    MazeManager mazeManager = state.getMaze();
	    mazeManager.setCurrentMaze(index);
	    replayCurrentMaze(); // chơi luôn maze đã chọn
	}
	
	

	public void setPaused(boolean paused) {
	    state.setPaused(paused);

	    if (paused) {
	        if (turnTimer != null) {
	            turnTimer.cancel();
	            turnTimer = null;
	        }
	    } else {
	    		startTimer(); // resume
	    }
	    
	    panel.requestFocusInWindow();
	    panel.repaint();
	}


	public void stopGame() {
	    if (turnTimer != null) {
	        turnTimer.cancel();
	        turnTimer = null;
	    }
	}


	
	public KeyAdapter getKeyListener() {
	    return new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            switch (e.getKeyCode()) {
	                case KeyEvent.VK_UP -> setLastPlayerDirection(Direction.UP);
	                case KeyEvent.VK_DOWN -> setLastPlayerDirection(Direction.DOWN);
	                case KeyEvent.VK_LEFT -> setLastPlayerDirection(Direction.LEFT);
	                case KeyEvent.VK_RIGHT -> setLastPlayerDirection(Direction.RIGHT);
	            }
	        }
	    };
	}

}
