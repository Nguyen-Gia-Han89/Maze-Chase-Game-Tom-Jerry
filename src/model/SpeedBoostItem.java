package model;

/**
 * Item tăng tốc cho Character. Khi nhặt, nhân vật đi được 2 ô/lượt trong
 * duration lượt.
 */
public class SpeedBoostItem extends Item {
	private static int speed; 

	public SpeedBoostItem(Position position) {
		super(position, "SpeedBoost");
		this.speed =1;
	}

	public static int getSpeed() {
		return speed;
	}
	
	/**
     * Tạo bản sao của item. Dùng khi clone Maze/Map để không ảnh hưởng đến object gốc.
     */
    @Override
    public Item clone() { 
        return new SpeedBoostItem(position.clone()); 
    }
	
}