package model;

public class ShieldItem extends Item{

	public ShieldItem(Position position) {
		super(position, "ShieldItem");
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Tạo bản sao của ShieldItem.     *
     * @return ShieldItem mới với vị trí được clone
     */
	public Item clone() { 
		 return new ShieldItem(position.clone()); 
	 }

}
