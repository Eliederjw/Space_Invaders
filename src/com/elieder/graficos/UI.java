package com.elieder.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.elieder.main.Game;

public class UI {
	
		
	public static int seconds = 0, minutes = 0;
	
	private BufferedImage[] GAME_OVER = {
			Game.spritesheet.getSprite(0 * Game.spriteSize, 2 * Game.spriteSize, 8 * Game.spriteSize, Game.spriteSize),
			Game.spritesheet.getSprite(0 * Game.spriteSize, 3 * Game.spriteSize, 8 * Game.spriteSize, Game.spriteSize),
			Game.spritesheet.getSprite(0 * Game.spriteSize, 4 * Game.spriteSize, 8 * Game.spriteSize, Game.spriteSize),
			Game.spritesheet.getSprite(0 * Game.spriteSize, 5 * Game.spriteSize, 8 * Game.spriteSize, Game.spriteSize)};
	
	private BufferedImage[] GAME_TITLE = {
			Game.spritesheet.getSprite(0 * Game.spriteSize, 6 * Game.spriteSize, 12 * Game.spriteSize, 3 * Game.spriteSize)};
	
	private int frames = 0;
	
	private int countdownFrames = 0, maxCountdownFrames = 60 * 5;
	
	private int animationFrames = 0, maxAnimationFrames = 8, spriteIndex = 0;
	private int enterAnimationFrames = 0, maxEnterAnimationFrames = 20;
	private boolean renderEnterText = true;
	
	private int gameOverX = ((Game.WIDTH*Game.SCALE)/2) - 128;
	private int gameOverY = ((Game.HEIGHT*Game.SCALE)/2) - 16;
	
	private int titleX = ((Game.WIDTH*Game.SCALE)/2) - 192;
	private int titleY = 120;
	
	private BufferedImage sprite;
	
	public void tick() {
		switch (Game.gameState) {
		case Game.GAME_OVER:
			countdownStart();
		}
	}

	public void render(Graphics g) {
		
		switch (Game.gameState) {
		case Game.PLAYING:
			renderPlaying(g);
			break;
			
		case Game.GAME_OVER:
			renderGameOver(g);
			break;
			
		case Game.START_SCREEN:
			renderStartScreen(g);
			break;
		}
		
		
	}
	
	private void countdownStart() {
		countdownFrames++;
		if (countdownFrames == maxCountdownFrames) {
			countdownFrames = 0;
			Game.gameState = Game.START_SCREEN;
		}
	}

	private void renderStartScreen(Graphics g) {
		
		animateEnterText();
		
		if (animateEnterText()) renderText("Arial", Font.BOLD, 25, "Press Enter to Start", "center", Game.WIDTH*Game.SCALE/2, Game.HEIGHT*Game.SCALE/2, Color.white,g);
		
		// Render title
		g.drawImage(GAME_TITLE[0], titleX, titleY, 192 * Game.SCALE, 48*Game.SCALE, null);
	
	}

	private boolean animateEnterText() {
		enterAnimationFrames++;
		if (enterAnimationFrames == maxEnterAnimationFrames) {
			enterAnimationFrames = 0;
			renderEnterText = !renderEnterText;
		}
		return renderEnterText;
				
	}

	private void renderGameOver(Graphics g) {
		// Render 'Your Score'
		renderText("Arial", Font.BOLD, 30, "Your Score", "center", Game.WIDTH*Game.SCALE/2, 120, Color.white, g);

		// Render game score
		renderText("Arial", Font.BOLD, 50, Integer.toString(Game.score), "center", Game.WIDTH*Game.SCALE/2, 200, Color.white, g);
		
		
		
		sprite = animateGameOver();
		
		g.drawImage(sprite, gameOverX, gameOverY, 128*2, 16*2, null);
	}

	private BufferedImage animateGameOver() {
		animationFrames++;
		if (animationFrames == maxAnimationFrames) {
			animationFrames = 0;
			spriteIndex++;
			if (spriteIndex >= GAME_OVER.length-1) {
				spriteIndex = 0;
			}
		}
		return GAME_OVER[spriteIndex];
	}

	private void  renderPlaying (Graphics g) {
		renderScore(g);
		
		renderLevel(g);
		
		renderLife(g);
	}
		
	private void renderLife(Graphics g) {
		
		// Render life bar vida máxima (de baixo
		g.setColor(Color.darkGray);
		g.fillRect(Game.WIDTH*Game.SCALE - 120, 10, 100, 10);
		
		// Render life bar atual (de cima)
		g.setColor(Color.white);
		g.fillRect(Game.WIDTH*Game.SCALE - 120, 10, (int)((Game.life/100) * 100), 10);
	}

	private void renderLevel(Graphics g) {
		// Render level
		renderText("Arial", 
				Font.BOLD,
				25, 
				"Level " + Game.level, 
				"center", 
				(Game.WIDTH*Game.SCALE)/2, 
				25, 
				Color.white, 
				g);
				
	}

	private void renderScore(Graphics g) {
		// Render score
		renderText("Arial", 
				Font.BOLD, 
				25, 
				"Score: " + Game.score, 
				"right", 
				20, 
				25, 
				Color.white, 
				g);		
	}

	private void renderText(String font, int style, int size, String text, String align, int x, int y, Color color, Graphics g) {
		int textAlignment = 0;
		g.setColor(color);
		g.setFont(new Font(font, style, size));
		
		Rectangle2D stringBound = g.getFontMetrics().getStringBounds(text, g);
		
		switch (align) {
		case "center":
			textAlignment = (int) stringBound.getCenterX();
			break;
		case "right":
			textAlignment = 0;
			break;
		case "left":
			textAlignment = (int) stringBound.getWidth();
		}
		
		g.drawString(text, x - textAlignment, y);
	}

	private String formatTime() {
		String formatTime = "";
		// Formata os minutos para ficar 01, 02, 03...
		if (minutes < 10) formatTime += "0"+minutes+":";
		else formatTime += minutes+":";
		
		//Formata os segundos para ficar 01, 02, 03...		
		if (seconds < 10) formatTime += "0"+seconds;
		else formatTime += seconds;	
		
		return formatTime;
	}

	private void countTime() {
	//contar os minutos e segundos
			frames++;
			if(frames == 60) {
				frames = 0;
				seconds++;
				if (seconds == 60) {
					seconds = 0;
					minutes++;
				}
			}		
	}
	
	
	
}
