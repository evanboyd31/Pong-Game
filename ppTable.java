package ppPackage;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;
import java.awt.Color;

/**
 * Class to add ground of ping pong table to a graphics program, and exports 
 * methods to convert world coordinates to screen coordinates and vice versa,
 * and a method to clear the screen of all objects.
 * This class contains code from my submission for assignments 2 and 3.
 * 
 * @author evanb
 */
public class ppTable {
	// instance variable
	GraphicsProgram GProgram; //reference to ppSim

	/**
	 * Constructor of the ping pong table. Adds GRect object representing the ground
	 * by calling the drawGroundPlane method.
	 * 
	 * @param GProgram The graphics program which we are adding the ground to
	 */
	public ppTable(GraphicsProgram GProgram) {
		// assign instance variable
		this.GProgram = GProgram;
		drawGroundPlane();
	}

	/**
	 * Method to convert world coordinates to screen coordinates. This method
	 * contains code from my submissions for assignments 1, 2, and 3.
	 * 
	 * @param P a GPoint at x and y in world coordinates
	 * @return a new GPoint at the corresponding screen coordinates
	 */
	public GPoint W2S(GPoint P) {
		// first get the "world" x & y coordinates
		double X = P.getX();
		double Y = P.getY();

		/*
		 * using the "transformation of coordinates" formulas on Page 9 of the
		 * assignment 1 handout from Prof. Ferrie, calculate the screen coordinates in
		 * pixels
		 */
		double x = (X - Xmin) * Xs;
		double y = ymax - ((Y - Ymin) * Ys);

		return new GPoint(x, y);
	}

	/**
	 * Method to convert screen coordinates to world coordinates The code contained
	 * in this method was covered by Ms. Poulin during Tutorial #6
	 * 
	 * @param P A GPoint containing screen coordinates
	 * @return a new GPoint at the corresponding world coordinates
	 */
	public GPoint S2W(GPoint P) {
		double ScrX = P.getX();
		double ScrY = P.getY();
		// convert to world coords by rearranging the transformation of coordinates formulas
		double WorldX = ScrX / Xs + Xmin;
		double WorldY = (ymax - ScrY) / Ys + Ymin;
		return new GPoint(WorldX, WorldY);
	}

	/**
	 * Method to erase the current display by removing all objects in the center
	 * region of the screen, then regenerate the ground plane by calling
	 * drawGroundPlane. This method is called by ppSim whenever the user
	 * presses the button labeled "New Serve".
	 */
	public void newScreen() {
		GProgram.removeAll();
		// the table has been erased from the screen, draw a new ground plane
		drawGroundPlane();
	}

	/**
	 * Method to draw the ground plane of the ping pong table. This method contains
	 * code covered by Ms. Poulin during tutorial.
	 */
	public void drawGroundPlane() {
		/*
		 * Create floor floor will be a rectangle 3 pixels thick placed at HEIGHT pixels
		 * away from the top of the screen. Color will be black and added to GProgram
		 */
		GRect floor = new GRect(0, ppSimParams.HEIGHT, ppSimParams.WIDTH + OFFSET, 3);
		floor.setColor(Color.BLACK);
		floor.setFilled(true);
		GProgram.add(floor);
	}
}
