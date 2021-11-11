package com.elieder.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.elieder.main.Game;

public class Tile {
	
	public static BufferedImage TILE_SKY = Game.spritesheet.getSprite(0 * Game.spriteSize, 9 * Game.spriteSize, Game.spriteWidth, Game.spriteHeight);
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(1 * Game.spriteSize, 0 * Game.spriteSize, Game.spriteWidth, Game.spriteHeight);	

	public boolean solid = false;
		
	private BufferedImage sprite;
	protected double x, y;
	
	public Tile(double x, double y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render (Graphics g) {
		g.drawImage(sprite, (int) x - Camera.x, (int) y - Camera.y, null);			
		
		
	}
}
