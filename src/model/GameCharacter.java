package model;

/**
 * Lớp trừu tượng Character đại diện cho một nhân vật trên bản đồ trò chơi. Nhân
 * vật có vị trí, tốc độ di chuyển và có thể thu thập vật phẩm. Các lớp con (ví
 * dụ: Tom, Jerry) sẽ kế thừa và triển khai hành vi cụ thể.
 */
public abstract class GameCharacter {

	/** Vị trí hiện tại của nhân vật trên bản đồ */
	protected Position position;

	/** Số lượt di chuyển */
	protected int speed;

	/** Trạng thái nhân vật đang có lá chắn hay không */
	protected boolean hasShield;

	/** Tham chiếu tới Maze để kiểm tra tường, vật phẩm, bẫy */


	/**
	 * Constructor khởi tạo Character
	 * 
	 * @param position vị trí ban đầu của nhân vật
	 * @param maze     tham chiếu đến Maze hiện tại
	 */
	public GameCharacter (Position position) {
		this.position = position;
		this.speed = 0; 
		this.hasShield = false;
	}

	// ====== GETTERS & SETTERS ======

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean isHasShield() {
		return hasShield;
	}

	public void setHasShield(boolean hasShield) {
		this.hasShield = hasShield;
	}

	

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = this.speed+ speed;
	}

	// ====== PHƯƠNG THỨC DI CHUYỂN ======
	/**
	 * Di chuyển thật sự 1 bước theo hướng. Controller đảm bảo bước di chuyển hợp
	 * lệ.
	 * 
	 * @param direction hướng di chuyển
	 */
	public void move(Direction direction) {

		/**
		 * Di chuyển thật sự theo tốc độ. Controller đã đảm bảo hợp lệ.
		 * 
		 * @param direction hướng di chuyển
		 */
		int x = position.getX();
		int y = position.getY();
		// Xác định hướng di chuyển dựa theo Direction
		switch (direction) {
		case UP:
			x -= 1;
			break;
		case DOWN:
			x += 1;
			break;
		case LEFT:
			y -= 1;
			break;
		case RIGHT:
			y += 1;
			break;
		}
		Position newPos = new Position(x, y);
		setPosition(newPos);

	}
	public Position getNextPosition(Direction direction) {
		int x = position.getX();
		int y = position.getY();
		switch (direction) {
		case UP:
			x -= 1;
			break;
		case DOWN:
			x += 1;
			break;
		case LEFT:
			y -= 1;
			break;
		case RIGHT:
			y += 1;
			break;
		}
		return new Position(x, y);
	}

	protected abstract void resetSpeed();
	protected abstract void forceEndTurn();

	// reset trạng thái chung cho mọi nhân vật
    public void resetState(Position startPos) {
        this.position = startPos;
        this.hasShield = false;
        resetSpeed();
    }

}
