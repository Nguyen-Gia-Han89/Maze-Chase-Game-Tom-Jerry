package model;

import java.util.Objects;

public class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	// GETTERS & SETTERS
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Position)) return false;
	    Position p = (Position) o;
	    return this.x == p.x && this.y == p.y;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(x, y);
	}
	
	
	/**
     * Tạo một bản sao Position.
     * @return Position mới nhưng có cùng x, y.
     */
	public Position clone() {
        return new Position(this.x, this.y);
    }

	public Position getNextPosition(Direction direction) {
	    int x = this.x;
	    int y = this.y;
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

}
