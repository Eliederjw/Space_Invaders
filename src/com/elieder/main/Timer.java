package com.elieder.main;

public class Timer {

	private int currentFrame = 0, maxFrames;
	private boolean stopped = true, counting = false;
	
	public Timer(int maxFrames) {
		this.maxFrames = maxFrames;
	}
	
	public boolean timerCounting() {
		if (!stopped) {
			currentFrame++;
			if (currentFrame <= maxFrames) {
				counting = true;
			} else {
				currentFrame = 0;
				counting = false;
				stopped = true;
			}
		}
		
		return counting;
	}
	
	public void resetTimer() {
		stopped = false;
	}
}
