package ppPackage;

import java.awt.Color;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

/**
 * Class which represents the agent paddle which the user plays against during
 * the pong game simulation. The class extends ppPaddle so ppPaddleAgent objects
 * have access to the methods declared in ppPaddle.
 * 
 * @author evanb
 *
 */
public class ppPaddleAgent extends ppPaddle {
	// instance variables
	private ppBall myBall; // reference to the ppBall used in the game
	private int ballSkip; // incremented each iteration of while loop to position paddle

	/**
	 * The ppPaddleAgent constructor calls the super-class constructor (ppPaddle)
	 * using the given parameters. This constructor contains code covered by Ms.
	 * Poulin during tutorial.
	 * 
	 * @param X        X position of the agent paddle
	 * @param Y        Y position of the agent paddle
	 * @param myColor  The agent paddle color
	 * @param myTable  a reference to the ppTable class
	 * @param GProgram the graphics program where the simulation takes place (ppSim)
	 */
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		// call ppPaddle constructor to create a new ppPaddle object to represent agent paddle
		super(X, Y, myColor, myTable, GProgram);
	}

	/**
	 * The run method changes the position of the agent paddle in accordance with
	 * the location of ppBall. This method contains code
	 * covered by Ms. Poulin during tutorial.
	 */
	public void run() {
		ballSkip = 0; // initialize ball skip to zero
		boolean running = true; // set the while loop condition to true
		double lastX = X;
		double lastY = Y;
		while (running) {
			// update position and velocities of ppPaddle Agent as done in ppPaddle run method
			Vx = (X - lastX)/TICK;
			Vy = (Y - lastY)/TICK;
			lastX = X;
			lastY = Y;
			/*
			 * The position of the agent paddle is only updated when ballSkip is greater than 
			 * the value of the lagSlider. This ensures that the AgentPaddle position is updated
			 * with a delay.
			 */
			if (ballSkip++ >= lagSlider.getValue()) {
				// get the ball position in y
				double ballY = myBall.getP().getY();

				// set the y of the agent paddle to be that of the y position of the ball
				this.setP(new GPoint(this.getP().getX(), ballY));

				// reset the ballSkip value
				ballSkip = 0;
			}
			/*
			 * Pause the GProgram in accordance with the 
			 * current value of the timeSlider so changes 
			 * can be seen at an appropriate rate.
			 */
			GProgram.pause(TICK * timeSlider.getValue()); // Time to mS
		}
	}

	/**
	 * Sets the instance variable myBall in this class. This method is from the
	 * assignment 4 hand-out.
	 * 
	 * @param myBall the ping pong ball used in the game simulation
	 */
	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}
}
