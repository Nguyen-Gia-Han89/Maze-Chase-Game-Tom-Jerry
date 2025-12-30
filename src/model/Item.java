package model;

/**
 * Lớp trừu tượng Item
 */
public abstract class Item {
	protected Position position;
	protected String name;
	public Item(Position position, String name) {
		super();
		this.position = position;
		this.name = name;
	}
	public Position getPosition() {
		return position;
	}
	public String getName() {
		return name;
	}
	

	@Override
	public String toString() {
		return "Item [position=" + position + ", name=" + name ;
	}

	// ===== Clone =====
	public abstract Item clone();
}
