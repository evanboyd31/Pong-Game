package ppPackage;

import javax.swing.JLabel;

import acm.program.GraphicsProgram;

/**
 * Class which keeps track of player scores. Changes in score are reflected in
 * JLabels in the NORTH section of the ppSim display. This class contains
 * methods to set player names, increment scores, and clear scores, as
 * described in the assignment 4 hand-out provided by Professor Ferrie.
 * 
 * @author evanb
 *
 */
public class ppScoreboard {
	// instance variables
	GraphicsProgram GProgram; // Reference to ppSim
	String leftPlayerName; // left player's name
	String rightPlayerName; // right player's name
	int leftPlayerScore; // left player's score
	int rightPlayerScore; // right player's score
	JLabel leftPlayerNameLabel; // label used to display the left player's name
	JLabel leftPlayerScoreLabel; // label used to display the left player's score
	JLabel rightPlayerNameLabel; // label used to display the right player's name
	JLabel rightPlayerScoreLabel; // label used to display the right player's score

	/**
	 * Constructor of the ppScoreboard. The constructor uses a reference to the
	 * GraphicsProgram (ppSim) to add labels which contain each player's name and
	 * their respective score. Labels are added to the GProgram in the NORTH section of
	 * the window.
	 * 
	 * @param GProgram a GraphicsProgram reference (ppSim)
	 */
	public ppScoreboard(GraphicsProgram GProgram) {
		// store a reference to ppSim in the GProgram instance variable
		this.GProgram = GProgram;

		// create a new JLabel for the left player's name and add it to the NORTH
		// section of GProgram
		leftPlayerNameLabel = new JLabel();
		this.GProgram.add(leftPlayerNameLabel, GProgram.NORTH);

		// // create a new JLabel for the left player's score and add it to the NORTH
		// section of GProgram
		leftPlayerScoreLabel = new JLabel(Integer.toString(leftPlayerScore));
		this.GProgram.add(leftPlayerScoreLabel, GProgram.NORTH);

		// create a new JLabel for the right player's name and add it to the NORTH
		// section of GProgram
		rightPlayerNameLabel = new JLabel();
		this.GProgram.add(rightPlayerNameLabel, GProgram.NORTH);

		// create a new JLabel for the right player's score and add it to the NORTH
		// section of GProgram
		rightPlayerScoreLabel = new JLabel(Integer.toString(rightPlayerScore));
		this.GProgram.add(rightPlayerScoreLabel, GProgram.NORTH);
	}

	/**
	 * Setter method to set the name of the left player (the agent paddle). The left
	 * player's name is displayed in the leftPlayerName label in the display.
	 * 
	 * @param leftPlayerName a string containing the name of the left player
	 */
	public void setLeftPlayerName(String leftPlayerName) {
		this.leftPlayerName = leftPlayerName;
		leftPlayerNameLabel.setText(this.leftPlayerName);
	}

	/**
	 * Setter method to set the name of the right player (the human). The right
	 * player's name is displayed in the rightPlayerName label in the display.
	 * 
	 * @param rightPlayerName a string containing the name of the right player
	 */
	public void setRightPlayerName(String rightPlayerName) {
		this.rightPlayerName = rightPlayerName;
		rightPlayerNameLabel.setText(this.rightPlayerName);
	}

	/**
	 * Increment the left player's score by 1. This method is called from ppBall
	 * whenever the ball is missed by the right paddle, or the right paddle hits the
	 * ball out of bounds. The change in leftPlayerScore is reflected in the display
	 * by calling updateScoreLabels.
	 */
	public void incrementLeftScore() {
		leftPlayerScore++;
		updateScoreLabels();
	}

	/**
	 * Increment the right player's score by 1. This method is called from ppBall
	 * whenever the ball is missed by the left paddle, or the left paddle hits the
	 * ball out of bounds. The change in rightPlayerScore is reflected in the display
	 * by calling updateScoreLabels.
	 */
	public void incrementRightScore() {
		rightPlayerScore++;
		updateScoreLabels();
	}

	/**
	 * Update the score labels of each player to reflect the values of the score
	 * instance variables.
	 */
	public void updateScoreLabels() {
		/*
		 *  The player score values need to be converted to a string to be presented in the
		 *  text of the labels.
		 */
		leftPlayerScoreLabel.setText(Integer.toString(leftPlayerScore));
		rightPlayerScoreLabel.setText(Integer.toString(rightPlayerScore));
	}

	/**
	 * Method to clear the score board. The score instance variables are reset to
	 * zero, then the score labels are updated by calling updateScoreLabels. This
	 * method is called by ppSim whenever the user presses the button labeled
	 * "Clear".
	 */
	public void clear() {
		leftPlayerScore = 0;
		rightPlayerScore = 0;
		updateScoreLabels();
	}

}
