package com.elieder.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.elieder.main.Game;
import com.elieder.world.Camera;

public class Enemy extends Entity{
	
		
	public static BufferedImage[] ENEMY = {
			Game.spritesheet.getSprite(0 * Game.spriteSize, 1 * Game.spriteSize, Game.spriteSize, Game.spriteSize)};
	
	public double life;
	
	private Graphics2D g2;
	private int radians = 0;
	
	private int damagePower;
	private double radDirection;
	
	private int pointValue;
	
	private double vSpeed;
	
	public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);	

		sprite = ENEMY[0];
		
		int enemyTypeRate = rand.nextInt(100);
		
		if (enemyTypeRate > 85) createEnemyLarge();
		else if (enemyTypeRate <= 85 && enemyTypeRate >= 25) createEnemyMedium();
		else createEnemySmall();
	}
		
 	public void tick() {
 		depth = 0;

 		setMask("Center", "Center");
 		
 		move();
 	}

 	public void render(Graphics g) {
// 		animate();
 		sprite = ENEMY[0];
 		
 		rotate(g);

//		renderMask(new Color(255, 0, 0, 127),g);
 	}
 	
	public void takeDamage(int damage) {
		life-=damage;
		
		if(life <= 0) {
			destroy();
			return;
		}
	}

	private void createEnemySmall() {
		int minLife = 0;
		int maxLife = 1;
		
		int minDamage = 1;
		int maxDamage = 3;
		
		this.width = 6;
		this.height = 6;
		
		pointValue = maxLife;
		
		// O número 1.0 ou -1.0 para controlar o lado que vai virar
		double posNeg = (double)(rand.nextInt(2)-1);
		radDirection = Math.pow(-1.0, posNeg);
		
		life = maxLife;
		
		damagePower = rand.nextInt(maxDamage - 1) + minDamage;
		
		// Varia a velocidade 
		int speedRate = rand.nextInt(50);
		if (speedRate < 5 ) vSpeed = speed * rand.nextDouble() * Game.level;
		else vSpeed = speed * rand.nextDouble() * Game.level/2;
	}
	
	private void createEnemyMedium() {
		int minLife = 2;
		int maxLife = 3;
		
		int minDamage = 1;
		int maxDamage = 10;
		
		this.width = 16;
		this.height = 16;
		
		pointValue = maxLife;
		
		// O número 1.0 ou -1.0 para controlar o lado que vai virar
		double posNeg = (double)(rand.nextInt(2)-1);
		radDirection = Math.pow(-1.0, posNeg);
		
		life = rand.nextInt(maxLife - 1) + minLife;
		
		damagePower = rand.nextInt(maxDamage - 1) + minDamage;
		
		// Varia a velocidade
		int speedRate = rand.nextInt(100);
		if (speedRate < 1) vSpeed = speed * Entity.rand.nextDouble() * Game.level/2;
		else vSpeed = speed * Entity.rand.nextDouble() * Game.level/3;
	}
	
	private void createEnemyLarge() {
		int minLife = 4;
		int maxLife = 6;
		
		int minDamage = 10;
		int maxDamage = 20;
		
		pointValue = maxLife;
		
		this.width = 32;
		this.height = 32;
		
		// O número 1.0 ou -1.0 para controlar o lado que vai virar
		double posNeg = (double)(rand.nextInt(2)-1);
		radDirection = Math.pow(-1.0, posNeg);
		
		
		life = rand.nextInt(maxLife - 1) + minLife;
		damagePower = rand.nextInt(maxDamage - 1) + minDamage;
		
		// Varia a velocidade
		int speedRate = rand.nextInt(200);
		if (speedRate < 1) vSpeed = speed * rand.nextDouble() * Game.level/3;
		else vSpeed = speed * rand.nextDouble() * Game.level/5;
	}

	private void rotate(Graphics g) {
		int xPivotPoint = getX() + width/2;
		int yPivotPoint = getY() + height/2;
		
		radians += radDirection;
 		
 		g2 = (Graphics2D) g;
 		// Pega as transfomação atual
 		AffineTransform old = g2.getTransform();
 		// Rotaciona no ponto xPivotPoint e yPivotPoint
 		g2.rotate(Math.toRadians(radians), xPivotPoint, yPivotPoint);
 		// Renderiza a imagem
 		g.drawImage(sprite, this.getX(), this.getY(), width, height, null);
 		// Volta a transformação antes de rotacionar, senão rotaciona tudo.
 		g2.setTransform(old);
 		
 
	}
	
	private void move() {
		
		y+=vSpeed;
		
		if(y > Game.HEIGHT) {
			causeDamage(damagePower);
			autoDestroy();
		}
	}
	
	private void causeDamage(int damage) {
		Game.life-= damage;
	}

	private void destroy(){
		emitParticles();
		increaseScore();
		increaseLevel();
		Game.entities.remove(this);
		
	}
	
	private void autoDestroy() {
		emitParticles();
		Game.entities.remove(this);
	}

	private void increaseLevel() {
		if (Game.score % 20 == 0) {
			Game.level++;
		}
	}

	private void increaseScore() {
		Game.score += pointValue;
		
	}

	private void emitParticles() {
		int particleW = 2;
		int particliH = 2;
		int particleSpeed = 1;
		Color particleColor = new Color (255, 255, 255);
		int particleLifeTime = 40;
		int particleAmount = 50;
		
		for (int i = 0; i < particleAmount; i++) {
			Particle particle = new Particle(
					getX() + width/2,
					getY() + height/2,
					particleW,
					particliH,
					particleSpeed,
					particleColor,
					particleLifeTime,
					null);
			Game.entities.add(particle);
		}		
	}

	private void renderMask (Color color, Graphics g) {
		g.setColor(color);
		g.fillRect(maskX - Camera.x, maskY - Camera.y, maskW, maskH);
		
	}
	
	private void animate(){
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
