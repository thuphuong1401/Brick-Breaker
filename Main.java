/*Phuong Vu, pvu3, Project 4, lab TR 12:30 - 13:45
 I did not copy code from anyone on this assignment
 */
package brickBreaker;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

public class Main { //set up the frame, create an instance of the class 
	public static void main(String[] args) {
		JFrame obj = new JFrame();
		BreakBrick breakbrick = new BreakBrick();
		obj.setLayout(new BorderLayout());
		obj.setBounds(10, 10, 700, 600);
		obj.setTitle("Break those Bricks!");
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(breakbrick);
	}
}
