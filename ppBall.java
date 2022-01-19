package ppPackage;

import java.awt.Color;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

/**
 * Class that holds a simulation loop which calculates motion and collision for
 * each ppBall object. This class contains code from the hand-outs for
 * assignments 1, 2, 3, and 4 as well as code from my submissions for assignments
 * 1, 2, and 3.
 * 
 * @author evanb
 *
 */
public class ppBall extends Thread {
	// Instance variables
	private double Xinit; // Initial position of ball - X
	private double Yinit; // Initial position of ball - Y
	private double Vo; // Initial velocity (Magnitude)
	private double theta; // Initial direction
	private double loss; // Energy loss on collision
	private Color color; // Color of ball
	private GraphicsProgram GProgram; // Instance of ppSim class (this)
	GOval myBall; // Graphics object representing ball
	ppTable myTable; // Instance of ppTable class
	ppPaddle RPaddle; // the player paddle
	ppPaddle LPaddle; // the agent paddle
	ppScoreboard myScoreboard; // reference to the ppScoreboard class
	double X, Xo, Y, Yo, Vx, Vy; // Current position and velocities
	boolean running; // condition for the while loop to be running

	/**
	 * The constructor for the ppBall class copies parameters to instance variables,
	 * creates an instance of a GOval to represent the ping-pong ball, and adds it
	 * to the display. This constructor contains code from the assignment 2 hand-out
	 * and my submissions for assignments 2 and 3.
	 * 
	 * @param Xinit    - starting position of the ball X (meters)
	 * @param Yinit    - starting position of the ball Y (meters)
	 * @param Vo       - initial velocity (meters/second)
	 * @param theta    - initial angle to the horizontal (degrees)
	 * @param color    - ball color (Color)
	 * @param loss     - loss on collision ([0,1])
	 * @param myTable  - a reference to the ppTable class to access W2S and S2W methods
	 * @param GProgram - a reference to the ppSim class used to manage the display
	 */
	public ppBall(double Xinit, double Yinit, double Vo, double theta, Color color, double loss, ppTable myTable,
			GraphicsProgram GProgram) {
		this.Xinit = Xinit; // Copy the constructor parameters to the corresponding instance variables
		this.Yinit = Yinit;
		this.Vo = Vo;
		this.theta = theta;
		this.loss = loss;
		this.color = color;
		this.GProgram = GProgram;
		this.myTable = myTable;

		// create a point at the starting position of the ball
		GPoint p = myTable.W2S(new GPoint(Xinit, Yinit));
		double ScrX = p.getX(); // convert the simulation coordinates to screen coordinates
		double ScrY = p.getY();
		/*
		 * create an instance of GOval, store it in instance variable and add it to
		 * GProgram using the screen coordinates calculated above
		 */
		myBall = new GOval(ScrX, ScrY, 2 * bSize * Xs, 2 * bSize * Ys);
		myBall.setFilled(true);
		myBall.setColor(this.color);
		this.GProgram.add(myBall);
	}

	/**
	 * The run method contains a while loop which updates the position of the ppBall
	 * on the screen, and calculates collisions with floors and walls. The run
	 * method contains code from the assignment 1 hand out, as well as my
	 * submissions for assignments 1, 2, and 3.
	 */
	public void run() {
		// set initial x and y position to the initial x & y parameters
		Xo = Xinit;
		Yo = Yinit;

		// create a point at the starting position of the ball
		GPoint p = myTable.W2S(new GPoint(Xinit, Yinit));
		double ScrX = p.getX(); // convert the simulation coordinates to screen coordinates
		double ScrY = p.getY();

		// initialize program variables
		double time = 0; // begins at zero and increments with each cycle of the while loop
		double Vt = bMass * g / (4 * Pi * bSize * bSize * k); // terminal velocity
		double Vox = Vo * Math.cos(theta * Pi / 180); // x-comp of initial velocity
		double Voy = Vo * Math.sin(theta * Pi / 180); // y-comp of initial velocity

		// set the initial value of the running boolean to true
		running = true;

		/*
		 * simulation loop to calculate position of ball and collisions. This loop
		 * contains code from my submissions for assignments 1, 2, and 3
		 */

		while (running) {
			// update relative positions to Xo and Yo
			X = Vox * Vt / g * (1 - Math.exp(-g * time / Vt)); // relative x position
			Y = Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time; // relative y position

			// update velocities
			Vx = Vox * Math.exp(-g * time / Vt); // velocity in x direction
			Vy = (Voy + Vt) * Math.exp(-g * time / Vt) - Vt; // velocity in y direction

			/*
			 * declare potential energy and kinetic energy variables. Kinetic energy values
			 * will be updated each time the ball comes in contact with a surface in
			 * accordance with the energy loss variable
			 */
			double KEx = 0.5 * bMass * Vx * Vx;
			double KEy = 0.5 * bMass * Vy * Vy;
			double PE = bMass * g * (Y + Yo); // Y + Yo is the absolute position of the ball

			/*
			 * check to see if we have hit the ground yet. When the ball hits the ground,
			 * the height of the center is the radius of the ball
			 */
			if (Vy < 0 && Y + Yo <= bSize) {
				KEx = 0.5 * bMass * Vx * Vx * (1.0 - loss);// Update new values for KEx and KEy given
				KEy = 0.5 * bMass * Vy * Vy * (1.0 - loss);
				PE = 0; // ball is on the ground; there is no potential energy

				/*
				 * after taking into account loss of energy, reinitialize Vox and Voy. The sign
				 * of Voy is positive as it should be (ball will be moving upwards after
				 * collision with ground)
				 */
				Vox = Math.sqrt(2 * KEx / bMass);
				Voy = Math.sqrt(2 * KEy / bMass);

				/*
				 * if the current velocity of the ball is to the left (Vx < 0), Vox should also
				 * be to the left; set Vox to be the negative of itself
				 */
				if (Vx < 0) Vox = -Vox;

				// reinitialize time and motion parameters
				time = 0; // reset time to zero with each collision
				Xo += X; // increment distance
				Yo = bSize; // set height to bSize when at ground
				X = 0;// (X,Y) is the instantaneous position along the current arc.
				Y = 0; // Reset both X and Y to zero
			}

			// condition to reach the right paddle position in X direction
			if (Vx > 0 && X + Xo >= RPaddle.getP().getX() - bSize - ppPaddleW / 2) {

				// evaluate if a collision has occurred by calling the contact method
				if (RPaddle.contact(X + Xo, Y + Yo)) {
					// PE at the location of the paddle is mass*g*(Y+Yo), as declared at the top of the while loop
					KEx = 0.5 * bMass * Vx * Vx * (1.0 - loss);// Update values for KEx and KEy
					KEy = 0.5 * bMass * Vy * Vy * (1.0 - loss);

					// after taking into account loss of energy, reassign Vox and Voy
					Vox = Math.sqrt(2 * KEx / bMass);
					Voy = Math.sqrt(2 * KEy / bMass);
					
					/*
					 * From the assignment 3 hand-out, account for movement of paddle for a
					 * collision; update Vox
					 */
					Vox = Vox * ppPaddleXgain; // Scale X component of velocity
					
					
					// ensure Vox does not exceed VoxMAX
					Vox = Math.min(Vox, VoxMAX);
					
					/*
					 * The ball is traveling to the right when it collides with the right paddle.
					 * After colliding with the paddle, the ball needs to be traveling to the left.
					 * To do so, take the negative of Vox to make Vox negative
					 */

					Vox = -Vox;

					/*
					 * From the assignment 3 hand-out, get the velocity of the paddle in the
					 * Y-direction to allow user to control how hard the ball is hit
					 */
					Voy = Voy * ppPaddleYgain * RPaddle.getV().getY();

					// reinitialize time and motion parameters
					time = 0; // time is reset with each collision
					Yo += Y; // add distance accumulated in the y-direction to Yo
					Xo = RPaddle.getP().getX() - bSize - ppPaddleW / 2; // Set the x position of the ball at right paddle
					X = 0; // (X,Y) is the instantaneous position along the current arc.
					Y = 0; // Reset both X and Y to zero

				}
				/*
				 * the ball missed the paddle, terminate while loop and update score. The point
				 * goes to the left paddle, as the right paddle missed the ball.
				 */
				else {
					kill();
					/*
					 * Increment score by calling update score method in ppScoreboard. The left
					 * paddle's score is incremented by 1 and shown in the display.
					 */
					myScoreboard.incrementLeftScore();
				}
			}

			// condition to reach the left paddle position in X direction
			if (Vx < 0 && X + Xo <= LPaddle.getP().getX() + bSize + ppPaddleW / 2) {

				// evaluate if a collision has occurred by calling the contact method
				if (LPaddle.contact(X + Xo, Y + Yo)) {
					// PE at the paddle is mass*g*(Y+Yo), which is declared at the top of the while loop
					KEx = 0.5 * bMass * Vx * Vx * (1.0 - loss);// Update values for KEx and KEy
					KEy = 0.5 * bMass * Vy * Vy * (1.0 - loss);

					/*
					 * after taking into account loss of energy, reassign Vox and Voy the sign of
					 * Vox is positive as it should be (ball needs to be traveling to the right
					 * after the collision)
					 */

					Vox = Math.sqrt(2 * KEx / bMass);
					Voy = Math.sqrt(2 * KEy / bMass);

					/*
					 * From the assignment 3 hand-out, account for movement of paddle for a
					 * collision; update Vox
					 */
					Vox = Vox * LPaddleXgain; // Scale X component of velocity
					
					// ensure Vox does not exceed VoxMAX
					Vox = Math.min(Vox, VoxMAX);
					
					/*
					 * From the assignment 3 hand-out, account for velocity of left paddle in the
					 * Y-direction and update Voy by getting the sign of the Y-velocity of the left
					 * paddle.
					 */
					Voy = Voy * LPaddleYgain * LPaddle.getSgnVy();

					// reinitialize time and motion parameters
					time = 0; // reset time with each collision
					Yo += Y; // add distance accumulated in the y-direction to Yo
					Xo = LPaddle.getP().getX() + bSize + ppPaddleW / 2;// Set the x position of the ball at left paddle
					X = 0; // (X,Y) is the instantaneous position along the current arc.
					Y = 0; // Reset both X and Y to zero

				}
				/*
				 * the ball missed the left paddle, terminate while loop and update score. The
				 * point goes to the right paddle, as the left paddle missed the ball.
				 */
				else {
					kill();
					/*
					 * Increment score by calling update score method in ppScoreboard. The right
					 * paddle's score is incremented by 1 and shown in the display.
					 */
					myScoreboard.incrementRightScore();
				}
			}

			/*
			 * check to see if the ball is still in bounds in the Y range. The scores will
			 * be updated depending on who hit the ball last.
			 */
			if (Yo + Y > Ymax) {
				// the ball is out of bounds, terminate while loop.
				kill();

				/*
				 * The ball is traveling to the left (Vx < 0), therefore the right paddle hit
				 * the ball last. Increment the score 1 point for the left paddle.
				 */
				if (Vx < 0) {
					myScoreboard.incrementLeftScore();
				}

				/*
				 * The ball is traveling to the right (Vx > 0), therefore the left paddle hit
				 * the ball last. Increment the score 1 point for the right paddle.
				 */
				if (Vx > 0) {
					myScoreboard.incrementRightScore();
				}
			}

			// Update position of the ball
			p = myTable.W2S(new GPoint(Xo + X - bSize, Yo + Y + bSize));
			ScrX = p.getX();
			ScrY = p.getY();
			myBall.setLocation(ScrX, ScrY);

			// check if the trace button is pressed. If so, add a trace at the current location.
			if (traceButton.isSelected()) trace(ScrX, ScrY);

			// increment time by TICK seconds
			time += TICK;

			/*
			 * pause the GProgram for TICK * (the time slider value) ms. The time slider can
			 * be adjusted by the user to speed up or slow down game play.
			 */
			GProgram.pause(TICK * timeSlider.getValue());

			
			/*
			 * condition for terminating the while loop if the total kinetic and potential
			 * energies fall below the threshold, terminate the while loop
			 */
			if ((KEx + KEy + PE) < ETHR)
				running = false;
		}
	}

	/**
	 * Procedure to print a trace dot at the point where the center of the ball is.
	 * This procedure contains code from my submissions for assignments 1, 2, and 3.
	 * 
	 * @param ScrX The X coordinate of the ball in pixels
	 * @param ScrY The Y coordinate of the ball in pixels
	 */
	private void trace(double ScrX, double ScrY) {

		/*
		 * create a single GOval object called "traceDot". traceDot will be placed at an
		 * x coordinate of ScrX + (bSize*Xs); the y coordinate of traceDot is ScrY +
		 * (bSize * Ys). traceDot will have width = PD and height = PD set the color of
		 * traceDot to be BLACK, and setFilled to true, then add traceDot to GProgram
		 */
		GOval traceDot = new GOval(ScrX + bSize * Xs, ScrY + bSize * Ys, PD, PD);
		traceDot.setColor(Color.BLACK);
		traceDot.setFilled(true);
		GProgram.add(traceDot);
	}

	/**
	 * Method to assign the right paddle instance variable.
	 * 
	 * @param RPaddle the right ping-pong paddle
	 */
	public void setRightPaddle(ppPaddle RPaddle) {
		this.RPaddle = RPaddle;
	}

	/**
	 * Method to assign the left paddle instance variable with a reference to the
	 * agent paddle.
	 * 
	 * @param LPaddle a reference to the agent paddle.
	 */
	public void setLeftPaddle(ppPaddle LPaddle) {
		this.LPaddle = LPaddle;
	}

	/**
	 * Method to assign a reference to the ppScoreboard class. The ppScoreboard
	 * class contains methods to updates player scores and display the scores to the
	 * user.
	 * 
	 * @param myScoreboard a reference to a ppScoreboard object
	 */
	public void setScoreboard(ppScoreboard myScoreboard) {
		this.myScoreboard = myScoreboard;
	}

	/**
	 * Method which returns the absolute position of the ball in world coordinates.
	 * 
	 * @return a new GPoint encapsulating the X and Y world coordinates
	 */
	public GPoint getP() {
		return new GPoint(Xo + X, Yo + Y);
	}

	/**
	 * Method which returns the X and Y velocities of a ppBall.
	 * 
	 * @return a new GPoint encapsulating the Vx and Vy instance variables
	 */
	public GPoint getV() {
		return new GPoint(Vx, Vy);
	}

	/**
	 * Terminate the while loop within the run method by setting the running instance 
	 * variable to false.
	 */
	void kill() {
		running = false;
	}
}
