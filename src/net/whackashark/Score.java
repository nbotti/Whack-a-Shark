package net.whackashark;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Class to keep track of all scoring in the game 
 * @author bottin
 * @author welchk
 * @version 2
 *
 */
public class Score {
	private int gameScore;
	
	/**
	 * Initialise the player's score as 0
	 */
	public Score() {
		gameScore = 0;
	}
	
	/**
	 * Add a point to the score
	 */
	public void addPoint() {
		gameScore++;
	}
	
	/**
	 * Take a point from the score
	 */
	public void takePoint() {
		gameScore--;
	}
	
	/**
	 * Reset the score to 1
	 * Called when the player begins a new game
	 */
	public void resetScore() {
		gameScore = 1;
	}
	
	/**
	 * Create a text file displaying the current high score
	 */
	public void createHighScores() {
		try {
			PrintWriter printer = new PrintWriter("highscores.txt");
			printer.write("");
			printer.close();
		}
		catch (FileNotFoundException fnf) {
			//This should never happen
		}
	}
	
	/**
	 * Add a high score to the high score file
	 * @param highScore the high score achieved by the player
	 * @param name the name of the player
	 */
	public void addHighScore(int highScore, String name) {
		try {
			PrintWriter printer = new PrintWriter("highscores.txt");
			String time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()); // http://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
			printer.write(highScore + "\n" + name + "\n" + time);
			printer.close();
		}
		catch (FileNotFoundException fnf) {
			//This should never happen
		}
	}
	
	/**
	 * Get the highest score recorded
	 * @return the current high score
	 */
	public String getHighScore() {
		try {
			FileReader filereader = new FileReader("highscores.txt");
			Scanner scanner = new Scanner(filereader);
			String highScore = scanner.nextLine();
			String name = scanner.nextLine();
			String time = scanner.nextLine();
			return name + " had a high score of " + highScore + " on " + time;
		}
		catch (FileNotFoundException fnf) {
			return "No high score yet.";
		}
	}
	
	/**
	 * Get the current score of the player
	 * @return the player's current score
	 */
	public int getScore() {
		return gameScore;
	}
}
