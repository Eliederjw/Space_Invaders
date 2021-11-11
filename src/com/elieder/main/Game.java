package com.elieder.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.elieder.entities.Entity;
import com.elieder.entities.Player;
import com.elieder.graficos.Spritesheet;
import com.elieder.graficos.UI;
import com.elieder.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	
	private BufferedImage GAME_BACKGROUND;
	
	private static final long serialVersionUID = 1L;
	
	public int backY = 0;
	public int backY2 = -HEIGHT;
	public int backSpeed = 1;
	
	public static JFrame frame;
	
	public int initialScore = 208;
	public static int score = 0;
	
	public int initialLevel = 10;
	public static int level = 1;
	
	public int initialLife = 10;
	public static double life = 100;
	
	
	public static final int START_SCREEN = 0, PLAYING = 1, GAME_OVER = 2;
	public static int gameState = START_SCREEN;
	
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 320;
	public static final int SCALE = 2;
	public static final int spriteSize = 16, spriteWidth = 16, spriteHeight = 16;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	
	public EnemySpawn enemySpawn;
	
	public UI ui;
	
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		loadBackground();
				
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
//		Inicializando objetos
		spritesheet = new Spritesheet("/Spritesheet.png");
				ui = new UI();
		
		enemySpawn = new EnemySpawn();
		
		entities = new ArrayList<Entity>();
		
	}
	
	public void initFrame() {
		frame = new JFrame("Space Invaders");
		frame.add(this);
		
		frame.setResizable(false);
		frame.pack();					
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {
	 Game game = new Game();
	 game.start();
	 
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if (delta >=1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
//				System.out.println("FPS: " +frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
	}

	public void tick() {
		
		switch (gameState) {
		case PLAYING:
			tickPlaying();
			break;
			
		case GAME_OVER:
			ui.tick();
			break;
		}
		
		moveBackground();
		
	}
	
	private void tickPlaying() {
		if (life <= 0) {
			entities.clear();
			gameState = GAME_OVER;
			return;
		}
		
		enemySpawn.tick();
		
		ui.tick();
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}

	private void moveBackground() {
		backY += backSpeed;
		if (backY >= HEIGHT) {
			backY = - HEIGHT;
		}
		
		backY2 += backSpeed;
		if (backY2 >= HEIGHT) {
			backY2 = -HEIGHT;
		}		
	}

	public void render () {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
//		Render Background
		g.drawImage(GAME_BACKGROUND, 0, backY, null);
		g.drawImage(GAME_BACKGROUND, 0, backY2, null);
		
		
//		Render States
		switch (gameState) {
		case PLAYING:
			renderPlaying(g);
			break;
			
		case GAME_OVER:
			break;
		}
				
		
		g.dispose();
		g = bs.getDrawGraphics();		
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null );
		ui.render(g);	 					
		
		bs.show();
	}
	
	private void renderPlaying(Graphics g) {
		//	Render entities
		Collections.sort(entities, Entity.nodeSorter);
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
	}
	
	private void loadBackground() {
		try {
			GAME_BACKGROUND = ImageIO.read(getClass().getResource("/Background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// KEYBOARD EVENTS
	@Override
	public void keyTyped(KeyEvent e) {
			
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch (gameState) {
		case START_SCREEN:
			keyPressedStartScreen(e);
			break;
		case PLAYING:
			keyPressedPlayign(e);
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// Right false
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			player.right = false;
		break;
		
		case KeyEvent.VK_D:
			player.right = false;
		break;
		
		// Left false	
		case KeyEvent.VK_LEFT:
			player.left = false;
		
		case KeyEvent.VK_A:
			player.left = false;
			
			// canShoot True
		case KeyEvent.VK_SPACE:
			player.canShoot = true;
		break;
			}
	}
		
	// MOUSE EVENTS
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override	
	public void mousePressed(MouseEvent e) {
	}	

	@Override
	
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}

	
	private void keyPressedPlayign(KeyEvent e) {
		// Right true
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			player.right = true;
			break;

		case KeyEvent.VK_D:
			player.right = true;
			break;

		// Left true	
		case KeyEvent.VK_LEFT:
			player.left = true;
			break;

		case KeyEvent.VK_A:
			player.left = true;
			break;

		// Shooting true
		case KeyEvent.VK_SPACE:
			if (player.canShoot) player.isShooting = true;
			break;
		}		
	}

	private void keyPressedStartScreen(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			startGame();
		}
	}

	private void startGame() {
		score = initialScore;
		life = initialLife;
		level = initialLevel;
		
		player = new Player(WIDTH/2 - 8, HEIGHT - 20, spriteSize, spriteSize, 3, null);
		entities.add(player);
		
		gameState = PLAYING;
		
	}

}
