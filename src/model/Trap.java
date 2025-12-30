package model;

public class Trap {
	private Position position;

	public Trap(Position position) {
		super();
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return "Trap [position=" + position;
	}
	
	/**
	 * Tạo bản sao trap (clone) Dùng khi clone Maze để không ảnh hưởng trap gốc.
	 */
	public Trap clone() {
		return new Trap(position.clone());
	}

}
