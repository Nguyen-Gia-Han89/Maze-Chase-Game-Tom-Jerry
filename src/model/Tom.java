package model;

import java.util.ArrayList;

public class Tom extends GameCharacter {
	private static int tomSpeed=1;
	private int totalSpeed;

	public Tom(Position position) {
		super(position);
		this.totalSpeed=totalSpeed();
	}

    
	public static int getTomSpeed() {
		return tomSpeed;
	}

	public static void setTomSpeed(int tomSpeed) {
		Tom.tomSpeed = tomSpeed;
	}

	public int totalSpeed() {
		 return tomSpeed + this.getSpeed();
	}

	public int getTotalSpeed() {
		return totalSpeed;
	}
	
	public void setTotalSpeed(int totalSpeed) {
		this.totalSpeed = totalSpeed;
	}
	public void resetSpeed() {
		this.totalSpeed=totalSpeed();
	}



	@Override
	public String toString() {
		return "Tom [totalSpeed=" + totalSpeed + "]";
	}


	public boolean canCatchJerry(Position jerryPos) {
	    return this.getPosition().equals(jerryPos);
	}

	
	public void subTotalSpeed() {
		this.setTotalSpeed(this.totalSpeed-1);
	}
	
	/**
     * Tạo bản sao Tom.
     */
	public Tom clone() {
        Tom cloned = new Tom(this.getPosition().clone());
        cloned.setSpeed(this.getSpeed());
        return cloned;
    }
	
	@Override
	protected void forceEndTurn() {
	    speed = 0;
	    totalSpeed = 0;
	}
	
	public void reset() {
	    this.hasShield = false;
	    resetSpeed();
	}

}
