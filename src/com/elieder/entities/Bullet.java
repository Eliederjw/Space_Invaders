package com.elieder.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

import com.elieder.main.Game;
import com.elieder.world.Camera;

public class Bullet extends Entity {

	public Bullet(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		this.x = x - (width/2);
	}
	
	public void tick() {
		
		move();
		
		enemmyCollision();
	}
	
	public void render (Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(getX(), getY(), width, height);
	}

	private void enemmyCollision() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Enemy) {
				if (isColliding(this, e)) {
					((Enemy) e).takeDamage(1);
					destroy();
				}
			}
		}
	}

	private void move() {
		y -= speed;
		
		if (y < 0) {
			free();
		}
	}

	private void free() {
		Game.entities.remove(this);
	}
	
	private void destroy() {
		emitParticles();
		Game.entities.remove(this);
	}
	
	private void emitParticles() {
		
		// Emitir particles brancas
		int particleW = 2;
		int particliH = 2;
		int particleSpeed = 1;
		Color particleColor = new Color (255, 255, 255);
		int particleLifeTime = 8;
		int particleAmount = 10;
		
		for (int i = 0; i < particleAmount; i++) {
			Particle particle = new Particle(
					getX(),
					getY(),
					particleW,
					particliH,
					particleSpeed,
					particleColor,
					particleLifeTime,
					null);
			Game.entities.add(particle);
		}
		
		// Emitir particles laranjas
		particleW = 1;
		particliH = 1;
		particleSpeed = 1;
		particleColor = new Color (255, 131, 0);
		particleLifeTime = 5;
		particleAmount = 10;
		
		for (int i = 0; i < particleAmount; i++) {
			Particle particle = new Particle(
					getX(),
					getY(),
					particleW,
					particliH,
					particleSpeed,
					particleColor,
					particleLifeTime,
					null);
			Game.entities.add(particle);
		}	
	}

}
