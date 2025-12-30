package model;

public class Jerry extends GameCharacter {
	private int cheeseCollected;
	private  int jerrySpeed=0;
	private int totalSpeed;;
	private int lives = 3;
	private int numOfStep=0;

	public Jerry(Position position, int cheeseCollected) {
		super(position);
		this.cheeseCollected = cheeseCollected;
	}


	public int getCheeseCollected() {
		return cheeseCollected;
	}

	
	public int getJerrySpeed() {
		return jerrySpeed;
	}


	public int getTotalSpeed() {
		return totalSpeed;
	}
	public int totalSpeed() {
	    return jerrySpeed + this.getSpeed();
	}

	public void setTotalSpeed(int totalSpeed) {
		this.totalSpeed = totalSpeed;
	}

	public void setCheeseCollected(int cheeseCollected) {
		this.cheeseCollected=cheeseCollected ;
	}
	
	public int getLives() {
	    return lives;
	}

	public void loseLife() {
	    if (lives > 0) {
	        lives--;
	    }
	}

	public boolean isDead() {
	    return lives <= 0;
	}

	public void resetLives() {
	    lives = 3;
	}

	public void resetSpeed() {
		this.totalSpeed=totalSpeed();
	}
	
	
	public int getNumOfStep() {
		return numOfStep;
	}


	public void setNumOfStep(int numOfStep) {
		this.numOfStep =this.numOfStep+numOfStep;
	}


	public void subNumOfStep() {
		this.numOfStep = this.numOfStep-1;
	}


	@Override
	public String toString() {
		return super.toString() + "Jerry cheeseCollected: " + cheeseCollected;
	}

	public void collectCheese() {
		cheeseCollected++;
	}

	public boolean checkWinCondition(int numOfChese) {
		return this.cheeseCollected==numOfChese;

	}

	public void subTotalSpeed() {
		this.setTotalSpeed(this.totalSpeed-1);
	}
	
	/**
     * Tạo bản sao Jerry 
     * @return bản sao Jerry mới
     */
	public Jerry clone() {
        Jerry cloned = new Jerry(this.getPosition().clone(), this.cheeseCollected);
        
        cloned.setSpeed(this.getSpeed());
        cloned.cheeseCollected = this.cheeseCollected;
        cloned.lives = this.lives;
        return cloned;
    }


	public void setJerrySpeed(int jerrySpeed) {
		this.jerrySpeed = jerrySpeed;
	}


	public void setLives(int lives) {
		this.lives = lives;
	}
	@Override
	protected void forceEndTurn() {
	    speed = 0;
	    totalSpeed = 0;
	    numOfStep = 0;
	}
	
	public void reset() {
	    this.hasShield = false;
	    this.lives = 3;
	    this.cheeseCollected = 0;
	    resetSpeed();
	}


}
