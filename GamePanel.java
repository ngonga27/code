package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	//SCREEN SETTING
	final int originalTileSize = 16;// 16x16 tile
	final int scale = 3;
	
	final int tileSize = originalTileSize * scale;//48x48 tile
	final int maxScreenCol = 20;//20 col
	final int maxScreenRow = 15;//15 row
	final int screenWidth = tileSize * maxScreenCol;//48*20=960 pixel
	final int screenHeight = tileSize *maxScreenRow;//720 pixel
	
	//FPS: khung hình mỗi giây
	int FPS = 60;//60 khung hình mỗi giây
	
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;//luồng giữ cho chương trình chạy cho đến khi dừng lại
	
	//set player's default position
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;//tốc độ người chơi

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;//0,01666 giây
		//sd nano giây để kiểm tra thời gian hiện tại của hệ thống
		// 1 giây= 1.000.000.000 nano giây
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		// vòng lặp trò chơi
		while(gameThread != null) {
			
			//UPDATE: cập nhật thông tin vị trí của nhân vật khi có đầu vào từ bàn phím
			update();
			
			//DRAW: vẽ màn hình với thông tin được cập nhật
			repaint();
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime/1000000;//chuyển đổi nano giây -->mili giây
				
				if(remainingTime <0) {
					remainingTime = 0;
				}
				
				Thread.sleep((long)remainingTime) ;
				
				nextDrawTime += drawInterval;
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	public void update() {
		if(keyH.upPressed == true) {
			playerY -= playerSpeed; 
		}
		if(keyH.downPressed == true) {
			playerY += playerSpeed; 
		}
		if(keyH.leftPressed == true) {
			playerX -= playerSpeed; 
		}
		if(keyH.rightPressed == true) {
			playerX += playerSpeed; 
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
		//vẽ 1 hcn trên màn hình với tọa độ x,y, width,height của hcn
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		g2.dispose();
	}
}
