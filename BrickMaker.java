/*Phuong Vu, pvu3, Project 4, lab TR 12:30 - 13:45
 I did not copy code from anyone on this assignment
 */
package brickBreaker;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class BrickMaker { //this class creates the "bricks" you saw in the game. The idea this using 2D array to store and draw each brick"
	public int map[][];
	public int brickWidth;
	public int brickHeight;

	public BrickMaker(int row, int col) {
		map = new int[row][col];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = 1;
			}
		}
		brickWidth = 540/col;
		brickHeight = 150/row;
	}

	public void draw(Graphics2D g) {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j] > 0) {
					g.setColor(Color.RED);
					g.fillRect(j*brickWidth + 80, i*brickHeight + 50, brickWidth, brickHeight);

					g.setStroke(new BasicStroke(3)); //draw line to "separate" bricks
					g.setColor(Color.BLACK);
					g.drawRect(j*brickWidth + 80, i*brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
	}

	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}

}
