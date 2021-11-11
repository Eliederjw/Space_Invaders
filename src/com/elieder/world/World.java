package com.elieder.world;

import java.awt.Graphics;

import com.elieder.entities.Entity;
import com.elieder.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
		
	public World() {
	}
	
	public static boolean isFree(int xnext, int ynext, int width, int height) {
						
		int x1 = (xnext+1)/TILE_SIZE;
		int y1 = (ynext+1)/TILE_SIZE;
		int topLeftPoint = x1 + (y1*WIDTH);
		
		int x2 = (xnext+width-1) / TILE_SIZE;
		int y2 = (ynext+1) / TILE_SIZE;
		int topRightPoint = x2 + (y2*WIDTH);
		
		int x3 = (xnext+1) / TILE_SIZE;
		int y3 = (ynext+height-1) /TILE_SIZE;
		int bottomLeftPoint = x3 + (y3*WIDTH);
	
		int x4 = (xnext+width-1) / TILE_SIZE;
		int y4 = (ynext+height-1) / TILE_SIZE;
		int bottomRightPoint = x4 + (y4*WIDTH);
		
		
		if (xnext < 0 
		|| (xnext+width)/TILE_SIZE >= WIDTH
		|| ynext < 0 
		|| (ynext+height)/TILE_SIZE >= HEIGHT) {
			return false;
			
		} else {
			
				return !((tiles[topLeftPoint] instanceof FloorTile)
					|| (tiles[topRightPoint] instanceof FloorTile)
					|| (tiles[bottomLeftPoint] instanceof FloorTile)
					|| (tiles[bottomRightPoint] instanceof FloorTile)
			);
		}
		
//		return true;
		
	}
	
	public static void restartGame(String level) {
		new Game();
		return;
	}
	
	public void tick() {
	}
	
	public void render (Graphics g) {
		int xstart = Camera.x / Game.spriteSize;
		int ystart = Camera.y / Game.spriteSize;
		
		
		int xfinal = xstart + (Game.WIDTH / Game.spriteSize);
		int yfinal = ystart + (Game.HEIGHT / Game.spriteSize);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}

