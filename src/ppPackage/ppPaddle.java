package ppPackage;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

import java.awt.Color;

/**
 * Class which serves as template for ppPaddle objects and exports 5 methods to
 * get the velocity of the paddle, set the position of the paddle, get the
 * position of the paddle, get the sign of the Y-Velocity of the paddle, and a
 * predicate method to detect collisions of ppBall objects with a ppPaddle. This
 * class contains code from the assignment 3 hand out provided by Professor
 * Ferrie, and code covered during tutorial with Ms. Poulin.
 * 
 * @author evanb
 *
 */
public class ppPaddle extends Thread {
	// instance variables
	double X; // position in X
	double Y; // position in Y
	ppTable myTable; // Ref. to the ppTable class
	GraphicsProgram GProgram; // Ref. to graphics program (ppSim)
	GRect myPaddle; // rectangle to represent the paddle
	double Vx; // X velocity of the paddle
	double Vy; // Y velocity of the paddle
	Color myColor; // color of the paddle

	/**
	 * Constructor of the ppPaddle class. Copies all parameters to corresponding
	 * instance variables, and adds a ppPaddle object to GProgram. This constructor
	 * contains code covered by Ms. Poulin during tutorial 6
	 * 
	 * @param X        the X position of the ppPaddle object
	 * @param Y        the Y position of the ppPaddle object
	 * @param myTable  instance of the ppTable class
	 * @param GProgram the GraphicsProgram which we are adding the paddle to
	 */
	public ppPaddle(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		// copy constructor parameters into corresponding instance variables
		this.X = X;
		this.Y = Y;
		this.myTable = myTable;
		this.GProgram = GProgram;
		this.myColor = myColor;

		// upper left corner of paddle in world coordinates
		double upperLeftX = X - ppSimParams.ppPaddleW / 2;
		double upperLeftY = Y + ppSimParams.ppPaddleH / 2;
		// convert the upper left corner coordinates into screen coordinates by calling
		// W2S
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
		double ScrX = p.getX();
		double ScrY = p.getY();
		// assign a new GRect to myPaddle instance variable at a location using the
		// calculated screen coords
		this.myPaddle = new GRect(ScrX, ScrY, ppPaddleW * Xs, ppPaddleH * Ys);
		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		GProgram.add(myPaddle);
	}

	/**
	 * The run method of the ppPaddle class. The velocities of the ppPaddle is
	 * updated within a while loop. This method contains code from the assignment 3
	 * hand-out.
	 */
	public void run() {
		// set the lastX and lastY to the initial X & Y positions
		double lastX = X;
		double lastY = Y;
		// initialize while loop condition to true
		boolean running = true;
		
		// while loop to update the velocity instance variables
		while (running) {
			// update velocity instance variables
			Vx = (X - lastX) / TICK;
			Vy = (Y - lastY) / TICK;
			// move the current positions into the last position variables
			lastX = X;
			lastY = Y;
			// pause the GProgram so changes can be seen at an appropriate rate
			GProgram.pause(TICK * TSCALE); // Time to mS
		}
	}

	/**
	 * Method which returns the X and Y velocities of the ppPaddle encapsulated in a
	 * GPoint.
	 * 
	 * @return a new GPoint containing the X and Y velocity instance variables
	 */
	public GPoint getV() {
		return new GPoint(Vx, Vy);
	}

	/**
	 * Method to set the position of the paddle in accordance with the location of
	 * the mouse. This method contains code that was written during tutorial 6 by
	 * Ms. Poulin
	 * 
	 * @param P A GPoint containing world coordinates
	 */
	public void setP(GPoint P) {
		// P is in world coordinates
		double upperLeftX = P.getX() - ppPaddleW / 2; // X position of upper left corner of paddle
		double upperLeftY = P.getY() + ppPaddleH / 2; // Y position of upper left corner of paddle

		// update the instance variables for world coordinates of the paddle
		Y = P.getY();
		X = P.getX();

		// calculate screen coordinates of upper left corner of paddle
		GPoint screenP = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
		double ScrX = screenP.getX();
		double ScrY = screenP.getY();
		// update the position of the paddle using the calculated screen coordinates
		this.myPaddle.setLocation(ScrX, ScrY);
	}

	/**
	 * Method to get the location of the center of the paddle in world coordinates
	 * 
	 * @return GPoint containing X & Y world coordinates
	 */
	public GPoint getP() {
		return new GPoint(X, Y);
	}

	/**
	 * Method to get the sign of the velocity in the y-direction
	 * 
	 * @return 1 if the instance variable Vy >= 0, return -1 otherwise
	 */
	public double getSgnVy() {
		// if paddle is going up, return 1 (Vy > 0)
		if (Vy >= 0)
			return 1;
		// if paddle is going down return -1 (Vy < 0)
		else
			return -1;
	}

	/**
	 * Predicate method to compare the height of the ping pong ball with the Y-range
	 * of the paddle. This method contains code from the assignment 3 hand-out
	 * 
	 * @param Sx The absolute X position of the ppBall in world coordinates
	 * @param Sy The absolute Y position of the ppBall in world coordinates
	 * @return true if the Y position of the ball falls within range of paddle,
	 *         false otherwise
	 */
	public boolean contact(double Sx, double Sy) {
		/*
		 * check y position of the ball against the paddle position when this method is
		 * called, we are already assuming that x position of the ball is the x position
		 * of the paddle, so we only need to compare the y positions.
		 */
		return (Sy >= Y - ppPaddleH / 2 && Sy <= Y + ppPaddleH / 2);
	}
}
