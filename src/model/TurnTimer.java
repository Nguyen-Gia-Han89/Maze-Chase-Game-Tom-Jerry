package model;

import java.util.Timer;
import java.util.TimerTask;

public class TurnTimer {
	private final int maxTime = 10; // thời gian tối đa 10 giây
	private int timeLeft;
	private Timer timer;
	private boolean running;

	public TurnTimer() {
		this.timeLeft = maxTime;
	}

	public void start() {
		if (timer != null)
			timer.cancel();
		timer = new Timer();
		running = true;
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (timeLeft > 0) {
					timeLeft--;
				} else {
					running = false;
					timer.cancel();
				}
			}
		}, 1000, 1000);
	}

	public void resetTurn() {
		this.timeLeft = maxTime;
		start();
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void stop() {
		running = false;
		if (timer != null)
			timer.cancel();
	}

	public boolean isRunning() {
		return running;
	}
}