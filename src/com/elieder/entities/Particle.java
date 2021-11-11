package com.elieder.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.elieder.main.Game;
import com.elieder.world.Camera;

public class Particle extends Entity{

	public int lifeTime = 10; int curLife = 0;
	
	public double dx = 0;
	public double dy = 0;
	public Color color = new Color(255, 255, 255);
	
	public Particle(int x, int y, int width, int height, int speed, Color color, int lifeTime, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
		this.color = color;
		this.lifeTime = lifeTime;
	}
	
	public void tick() {
		x += dx * speed;
		y += dy * speed;
		
		curLife++;
		if (lifeTime == curLife) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(this.color);
		g.fillRect(getX() , getY(), width, height);
	}
}
