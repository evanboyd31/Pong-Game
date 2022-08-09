package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

/**
 * Class extending graphics program where the game will be display.
 * Creates instances of ppTable, ppBall, ppPaddle, ppPaddleAgent, and ppScoreboard.
 * Creates buttons and sliders used to control game play. 
 * This class contains code from the assignment 3 and 4 hand-outs.
 * 
 * @author evanb
 *
 */
public class ppSim extends GraphicsProgram {
	// instance variables
	ppTable myTable; // reference to the ppTable class
	ppPaddle RPaddle; // reference to user controlled paddle
	ppPaddleAgent LPaddle; // reference to agent paddle
	ppBall myBall; // reference to ball
	RandomGenerator rgen; // random number generator
	Color iColor; // color of ppBall
	double iYinit; // initial ball position
	double iLoss; // energy loss on contact with ground
	double iVel; // initial ball velocity
	double iTheta; // initial launch angle
	ppScoreboard myScoreboard; // reference to the ppScoreboard class

	public static void main(String[] args) {
		new ppSim().start(args);
	}

	/**
	 * The init method of ppSim. Resizes the window, creates an instance of ppTable,
	 * ppPaddle, ppPaddleAgent, ppScoreboard and starts simulations. "New Serve",
	 * "Clear", "Quit", and "Trace" buttons are added to the SOUTH region of the window.
	 * Sliders to control the time multiplier and the agent paddle lag are created here.
	 * This run method contains code from the assignment 4 hand-out.
	 */
	public void init() {

		// resize the window using the width, height, and offset from ppSimParams
		this.resize(ppSimParams.WIDTH + OFFSET, ppSimParams.HEIGHT + OFFSET);

		// create the JButtons and JToggleButton for 4 menu items in the SOUTH region of the window
		this.add(new JButton("New Serve"), SOUTH); // creates a new game
		this.add(new JButton("Clear"), SOUTH); // clears the scores of the players
		traceButton = new JToggleButton("Trace"); // when selected, the trace of the ball is displayed
		this.add(traceButton, SOUTH);
		this.add(new JButton("Quit"), SOUTH); // exits the program

		/*
		 * Create the time multiplier slider. The values that the slider covers are
		 * minTime to maxTime, and the default value of the slider is set to defaultTime,
		 * as declared in ppSimParams (defaultTime = TSCALE).
		 * The value of the slider multiplies the TICK value in ppBall and ppPaddleAgent 
		 * so the ball's position is updated at a slower rate, making the ball easier or more 
		 * difficult to follow for the user.
		 */
		timeSlider = new JSlider(minTime, maxTime, defaultTime);
		this.add(new JLabel("+t"), SOUTH); // label left side of slider
		this.add(timeSlider, SOUTH);
		this.add(new JLabel("-t"), SOUTH); // label right side of slider
		this.add(new JButton("rtime"), SOUTH); // button to reset the value of timeSlider

		/*
		 * Create the lag slider. The slider updates the value of AgentLag in the
		 * ppPaddleAgent class to speed up or slow down the "reaction" time of the agent
		 * paddle. The slider has a range of values of minLag to maxLag, with the initial
		 * setting being defaultLag, as declared in ppSimParams.
		 */
		lagSlider = new JSlider(minLag, maxLag, defaultLag);
		this.add(new JLabel("-lag"), SOUTH); // label left side of slider
		this.add(lagSlider, SOUTH);
		this.add(new JLabel("+lag"), SOUTH); // label right side of slider
		this.add(new JButton("rlag"), SOUTH); // button to reset the value of

		// create scoreboard object and set the names of each player
		this.myScoreboard = new ppScoreboard(this); // send the scoreboard a reference of ppSim
		myScoreboard.setLeftPlayerName("Agent");
		myScoreboard.setRightPlayerName("Human");
		
		// add action and mouse listeners
		addMouseListeners(); 
		addActionListeners();

		// random number generator
		rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);
		
		// generate ground plane and assign myTable instance variable
		this.myTable = new ppTable(this);

		/*
		 *  Create a new game. The new game method also creates and assigns the myBall
		 *  instance variable.
		 */
		newGame();

	}

	/**
	 * Method which creates a ppBall instance by generating random parameters used
	 * by the ppBall class constructor. This method contains code from the
	 * assignment 3 handout.
	 * @return a new ppBall with randomly generated parameters
	 */
	public ppBall newBall() {

		// generate random parameters for ppBall
		iColor = Color.RED;
		iYinit = rgen.nextDouble(YinitMIN, YinitMAX);
		iLoss = rgen.nextDouble(EMIN, EMAX);
		iVel = rgen.nextDouble(VoMIN, VoMAX);
		iTheta = rgen.nextDouble(ThetaMIN, ThetaMAX);

		return new ppBall(Xinit, iYinit, iVel, iTheta, iColor, iLoss, myTable, this);
	}

	/**
	 * Method to end the game taking place and create a new game. Clear the screen
	 * by calling the newScreen method in the myTable class, and create new left and
	 * right paddles, and a new ppBall. This method contains code from the
	 * assignment 4 hand-out.
	 */
	public void newGame() {
		if (myBall != null) myBall.kill(); // stop current game in play
		
		myTable.newScreen(); // wipe screen of all objects and create new ground plane
		myBall = newBall(); // create new ball with random parameters

		// create new ppPaddle/ppPaddleAgent objects and assign to instance variables
		RPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, Color.GREEN, myTable, this);
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this);

		LPaddle.attachBall(myBall); // link paddle agent to myBall
		myBall.setRightPaddle(RPaddle); // set RPaddle instance variable in ppBall
		myBall.setLeftPaddle(LPaddle); // set RPaddle instance variable in ppBall
		myBall.setScoreboard(myScoreboard); // set myScoreboard instance variable in ppBall

		pause(STARTDELAY); // pause display for STARTDELAY ms
		myBall.start(); // begin ball simulation
		LPaddle.start(); // begin agent paddle simulation
		RPaddle.start(); // begin user paddle simulation
	}

	/**
	 * Mouse Handler - a moved event moves the paddle up and down in Y. This method
	 * contains code from the assignment 3 & 4 hand-outs.
	 */
	public void mouseMoved(MouseEvent e) {
		if (myTable == null || RPaddle == null) return;
		GPoint Pm = myTable.S2W(new GPoint(e.getX(), e.getY()));
		double PaddleX = RPaddle.getP().getX();
		double PaddleY = Pm.getY();
		RPaddle.setP(new GPoint(PaddleX, PaddleY));
	}

	/**
	 * Method to handle action events from the ppSim display. This method contains
	 * code from the assignment 4 hand-out.
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand(); // get the string associated with the action command
		if (command.equals("New Serve")) {
			// user has requested a new serve, create a new game
			newGame();
		} else if (command.equals("Quit")) {
			// user has requested to quit, exit the program.
			System.exit(0);
		} else if (command.equals("Clear")) {
			// user has requested to clear scores, call the clear method in ppScoreboard
			myScoreboard.clear();
		} else if (command.equals("rtime")) {
			/*
			 * user has requested to reset the value of the time slider. Reset the value back
			 * to defaultTime.
			 */
			timeSlider.setValue(defaultTime);
		} else if (command.equals("rlag")) {
			/*
			 * user has requested to reset the value of the agent lag. Reset the value back
			 * to defaultLag.
			 */
			lagSlider.setValue(defaultLag);
		}
	}
}
