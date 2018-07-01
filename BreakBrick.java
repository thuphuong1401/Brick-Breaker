/*Phuong Vu, pvu3, Project 4, lab TR 12:30 - 13:45
 I did not copy code from anyone on this assignment
 */
package brickBreaker;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BreakBrick extends JPanel implements KeyListener, ActionListener {
	private boolean play = false;
	private int score = 0;
	public Random rand = new Random();
	public int row = 3 + rand.nextInt(6); 
	public int col = 3 + rand.nextInt(6);
	private int totalBricks = row*col;
	private Timer timer;
	private Timer timer2;
	private int delay = 6;
	private int playerX = 310;
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	private int paddleLength = 170;
	public boolean pause = false;
	private BrickMaker map;
	public HashMap<String,Integer> HighScore = new HashMap<String,Integer>();
	public ArrayList<Character> userName = new ArrayList<>();
	public boolean displayHighScore = false;
	public boolean displayName = true;

	public BreakBrick() {
		map = new BrickMaker(row,col);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer2 = new Timer(8000,this);
		timer.start();
	}

	public void paint(Graphics g) {

		//background
		g.setColor(Color.BLACK);
		g.fillRect(1, 1, 692, 592);

		//enter username
		if(displayName) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("serif", Font.BOLD,25));
			g.drawString("Please enter your name, then press SPACE to start!", 100, 250);
			for(int i = 0; i < userName.size(); i++) {
				String s = "" + userName.get(i);
				g.drawString(s, 250 + 20*i,300);
			}
		}

		//draw map of bricks
		map.draw((Graphics2D) g);

		//borders
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);

		//scores
		g.setColor(Color.WHITE);
		g.setFont(new Font("serif", Font.BOLD,25));
		g.drawString(" "+score, 590, 30);

		//the paddle
		g.setColor(Color.CYAN);
		g.fillRect(playerX, 550, paddleLength, 10);

		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);

		if(totalBricks <= 0) { //if there is no bricks left, you won the game
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD,25));
			g.drawString("You Won! Scores: " + score, 200, 300);
			g.setFont(new Font("serif", Font.BOLD,25));
			g.drawString("Press ENTER to restart", 200, 350);
			g.drawString("Press SHIFT to view high scores", 200, 400);
			if(userName.size() > 0) {
				String temp = " ";
				for (int i = 0; i < userName.size(); i++) {
					char c = userName.get(i);
					temp = temp + c;
				}
				HighScore.put(temp, score);
				System.out.println(HighScore);
				for(int i = 0; i < userName.size(); i++) {
					userName.remove(userName.size() - 1);
				}
				userName = new ArrayList<Character>();
			}
		}

		if(ballposY > 570) { //if the ball went out of the screen, you lose
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD,25));
			g.drawString("Game Over! Scores: " + score, 200, 300);
			g.drawString("Press ENTER to restart", 200, 350);
			g.drawString("Press SHIFT to view high scores", 200, 400);
			if(userName.size() > 0) {
				String temp = " ";
				for (int i = 0; i < userName.size(); i++) {
					char c = userName.get(i);
					temp = temp + c;
				}
				HighScore.put(temp, score);
				System.out.println(HighScore);
				for(int i = 0; i < userName.size(); i++) {
					userName.remove(userName.size() - 1);
				}
				userName = new ArrayList<Character>();
			}
		}

		//pause the game
		if(pause == true) {
			g.setFont(new Font("serif", Font.BOLD,25));
			g.drawString("PAUSED. Press Left or Right Key to Continue", 100, 300);
		}
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent args0) {
		//timer.start(); 
		timer2.start();
		//System.out.println(timer2 + "  " + args0.getSource());

		if(args0.getSource().equals(timer2)) {
			paddleShortened();
			repaint();
		}

		if(play) {
			if(new Rectangle(ballposX, ballposY,20,20).intersects(new Rectangle(playerX, 550, paddleLength, 10))) { //collision with paddle
				ballYdir  = -ballYdir; //reflect the ball upwards when the ball collides with the paddle
			}

			//collision with the bricks
			//the idea here is to imagine the ball is wrapped in a square, and when that square intersects the bricks, the bricks will disappear
			A: for(int i = 0; i < map.map.length; i++) {
				for(int j = 0; j < map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j*map.brickWidth + 80;
						int brickY = i*map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20); //ball "wrapped" in a square
						Rectangle brickRect = rect;

						if(ballRect.intersects(brickRect)) { //collision with bricks
							map.setBrickValue(0,i,j); //brickvalue 1 suggests the bricks are visible, 0 suggests that the bricks disappears
							totalBricks--;
							score += 5;

							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							break A;

						}
					}
				}
			}

			ballposX += ballXdir;
			ballposY += ballYdir;

			//ball reflecting from borders
			if(ballposX < 0) { //left border
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) { //top border
				ballYdir = - ballYdir;
			}
			if(ballposX > 670) { //right border
				ballXdir = - ballXdir;
			}
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX > 700 - paddleLength) {
				playerX = 700 - paddleLength;
			} else {
				moveRight();
				pause = false;
			}
		}

		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX  <= 0) {
				playerX = 0;
			} else {
				moveLeft();
				pause = false;
			}
		}

		else if(e.getKeyCode() == KeyEvent.VK_ENTER) { //game over and the user presses ENTER
			if(!play) { 
				displayName = true;	
				timer.start();
				timer2.start();
				//play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				map = new BrickMaker(row + 1,col + 1);
				totalBricks = (row + 1)*(col + 1);
				paddleLength = 150; //reset paddle length to the original length
				repaint();
			}
		}

		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { //pause the game
			play = false;
			pause = true;
		}

		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			play = true; //start game
			displayName = false;
		}

		else if(e.getKeyCode() == KeyEvent.VK_SHIFT) { //high score list
			displayHighScore = false;
			Graphics g = getGraphics();
			timer.stop();
			timer2.stop();
			drawHighScore(g);
		}

		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) { //delete characters in name
			userName.remove(userName.size() - 1);

		}
		else {//enter the username
			if(displayName) {
				userName.add(e.getKeyChar());
			}
			else {

			}
		}
	}


	public void moveRight() {
		play = true;
		playerX += 20;
	}

	public void moveLeft() {
		play = true;
		playerX -= 20;
	}

	public void paddleShortened() { //the paddle is shortened every 8 secs
		paddleLength -= 8;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	} 

	//draw high score board
	public void drawHighScore(Graphics g) {
		if(displayHighScore == false) {
			g.setColor(Color.BLACK);
			g.fillRect(1, 1, 692, 592);
			int i = 0;
			//for (i = 0; i < userName.size(); i++) userName.remove(i);
			userName = new ArrayList<Character>();
			for(String Name: HighScore.keySet()) {				
				Integer Score = HighScore.get(Name);
				g.setColor(Color.WHITE);
				g.setFont(new Font("serif", Font.BOLD,20));
				i += 5;
				g.drawString(Name, 100, 20 + 5*i);
				g.drawString(Integer.toString(Score), 300, 20 + 5*i);
			}
			g.drawString("Press Enter to continue playing", 200, 550);
		}
	}
}
