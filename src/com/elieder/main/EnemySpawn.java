package com.elieder.main;

import com.elieder.entities.Enemy;
import com.elieder.entities.Entity;

public class EnemySpawn {
	public int targetTime = 60*2;
	public int curTime = 0;
	
	public void tick(){
		
		
		curTime++;
		if(curTime >= targetTime) {
			curTime = 0;
			int yy = -16;
			int xx = Entity.rand.nextInt(Game.WIDTH  - 16);
			
			Enemy enemy = new Enemy (xx, yy, 16, 16, 1, null);
			Game.entities.add(enemy);
			
			randomizeTime();
		}
	}

	private void randomizeTime() {
		int rate = Entity.rand.nextInt(2) + 1;
		targetTime = 60 * rate;
	}
}
