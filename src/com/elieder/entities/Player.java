package com.elieder.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.elieder.graficos.Animation;
import com.elieder.main.Game;
import com.elieder.world.Camera;
import com.elieder.world.World;

public class Player extends Entity{
	
	public boolean right, up, left, down;
	
	public double life = 100;
	
	public boolean isShooting = false;
	public boolean canShoot = true;
	
	private BufferedImage[] PLAYER_IDLE = {
			Game.spritesheet.getSprite(0 * Game.spriteSize, 0 * Game.spriteSize, Game.spriteSize, Game.spriteSize)};
	private BufferedImage[] PLAYER_RIGHT = {
			Game.spritesheet.getSprite(1 * Game.spriteSize, 0 * Game.spriteSize, Game.spriteSize, Game.spriteSize),
			Game.spritesheet.getSprite(2 * Game.spriteSize, 0 * Game.spriteSize, Game.spriteSize, Game.spriteSize)};
	private BufferedImage[] PLAYER_LEFT = {
			Game.spritesheet.getSprite(3 * Game.spriteSize, 0 * Game.spriteSize, Game.spriteSize, Game.spriteSize),
			Game.spritesheet.getSprite(4 * Game.spriteSize, 0 * Game.spriteSize, Game.spriteSize, Game.spriteSize)};
	
	private Animation rightAnimation;
	private Animation leftAnimation;
	
	
	private int xDir = 0;
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		sprite = PLAYER_IDLE[0];
		
		rightAnimation = new Animation(PLAYER_RIGHT, 50);
		rightAnimation.setLoop(false);
		leftAnimation = new Animation(PLAYER_LEFT, 50);
		leftAnimation.setLoop(false);
		
		
	}
	
	public void tick() {
		
		depth = 1;
		
		setMask("Bottom", "Center");
		
//		MOVIMENTO
		// Move pra direita
		if (right) {
			xDir = 1;
		}
		
		// Move pra esquerda
		else if (left) {
			xDir = -1;
		}
		
		// Parado
		else xDir = 0;
		
		move();
		
		fire();
		
		limitBorder();
		
		Camera.x = Camera.clamp((int)x - Game.WIDTH / 2,  0, World.WIDTH * 16 - Game.WIDTH );
		Camera.y = Camera.clamp((int)y - Game.HEIGHT / 2, 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void takeDamage(int damage) {
		
	}
	
	public void render(Graphics g) {
			
//		renderMask(new Color(255, 0, 0, 127), g);
		
		animate();
		
		// render Player
		g.drawImage(sprite, this.getX(), this.getY(), null);
//		g.setColor(Color.red);
//		g.fillRect(0, 0, 16, 16);
		
	}
	
	private void fire() {
		if(isShooting) {
			isShooting = false;
			canShoot = false;
			int xx = getX() + 8;
			int yy = getY();
			Bullet bullet = new Bullet(xx, yy, 2, 4, 4, null);
			Game.entities.add(bullet);
			
		}
	}

	private void limitBorder() {
		
		if (x >= Game.WIDTH) {
			x = - 16;
		}
		
		if (x+16 < 0) {
			x = Game.WIDTH;
		}
	}

	private void move(){
		x += speed*xDir;
	}
	
	private void animate() {
		switch (xDir) {
		case 1:
			sprite = rightAnimation.animate();
			leftAnimation.resetAnimation();
		break;
		
		case -1:
			sprite = leftAnimation.animate();
			rightAnimation.resetAnimation();
		break;
		
		case 0:
			sprite = PLAYER_IDLE[0];
			leftAnimation.resetAnimation();
			rightAnimation.resetAnimation();
		break;
		}
		
	}
	
	private void renderMask(Color color, Graphics g){
		g.setColor(color);
		g.fillRect(maskX - Camera.x, maskY - Camera.y, maskW, maskH);
	}
	
	private void setMask (String vAlign, String hAlign){
		
		switch (vAlign) {
		case "Top":
			maskY = (int) y;
			break;
			
		case "Center":
			
			maskY = ((int) y - maskH/2) + (height/2);
			break;
			
		case "Bottom":
			maskY = ((int) y + height) - maskH;
			break;
		}
		
		switch (hAlign) {
		case "Left":
			maskX = (int) x;
			break;
			
		case "Center":
			maskX = ((int) x - maskW/2) + (width/2);
			break;
			
		case "Right":
			maskX = ((int) x + width) - maskW;
			break;
			
			
		}
	}
}
