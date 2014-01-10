package net.whackashark;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;

// Dialog boxes info from: http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html

/**
 * GUI Class for WHACK-A-SHARK
 * @author bottin
 * @author welchk
 * @version 2
 */
public class WhackaGUI extends JFrame {
	
	private GameState gamestate;
	private Score score;
	
	private final ImageIcon mole = new ImageIcon(getClass().getResource("/Resources/shark.jpg"));
	private final ImageIcon hole = new ImageIcon(getClass().getResource("/Resources/dolphin_small.jpg"));
	
	private JButton[] field;
	private JLabel scorelabel;
	
	private JLabel timelabel;
	private int timeLeft;
	private Timer onesec;
	
	private JPanel panel;
	private Timer timer;
	private Timer gameTime;
	
	private boolean isPaused = false;
	private boolean isOver = false;
	private JButton pausebtn;
	private JButton newgamebtn;
	private JButton exitbtn;
	private JButton highscore;
	
	/** Ran every second, or when the user clicks a tile (whichever comes first).
	 * Subtracts points if the user did not click in time, or if they clicked a dolphin.
	 */
	class click implements ActionListener {
		private int location;
		public click(int loc) {
			location = loc;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(isPaused) {
				return;
			}
			if(location == gamestate.getMolecation()) { //its the mole
				score.addPoint();
			}
			else if (location == -1) { // they did not click in time
				score.takePoint();
			}
			else { //its a dolphin
				for(int i=0; i<5; i++) {
					score.takePoint();
				}
			}
			field[gamestate.getMolecation()].setIcon(hole);
			gamestate.randomMolecation();
			field[gamestate.getMolecation()].setIcon(mole);
			
			scorelabel.setText("Score: " + score.getScore());
			timer.restart();
		}
		
	}
	
	/** Ran every second, updates the time remaining label.
	 */
	class second implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			timeLeft--;
			timelabel.setText("Time remaining: " + timeLeft + " seconds");
		}
	}
	/** Ran when the user clicks the high score button. Shows the current high score. */
	class HighScore implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, score.getHighScore());
		}
	}
	
	/** Pauses the game. Resumes if paused. */
	class pauseIt implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String btn = isPaused ? "Pause" : "Resume";
			pausebtn.setText(btn);
			isPaused = !isPaused;
			if(isPaused) {
				gameTime.stop();
				onesec.stop();
			}
			else {
				gameTime = new Timer(timeLeft * 1000, new timeUp());
				gameTime.start();
				onesec.start();
			}
		}
	}
	
	/** Quits the game */
	class exitclick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}
	
	/** Start a new game */
	class newGame implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(isPaused && !isOver) {
				JOptionPane.showMessageDialog(null, "Resume the game to start a new game.");
				return;
			}
			score.resetScore();
			timer.restart();
			timer.start();
			timeLeft = 60;
			gameTime = new Timer(timeLeft * 1000, new timeUp());
			gameTime.start();
			onesec.restart();
			onesec.start();
			isOver = false;
		}
	}
	
	/** Time is up, game over
	 * If they have a high score, set it as the new high score.
	 */
	class timeUp implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			gameTime.stop();
			isOver = true;
			timer.stop();
			onesec.stop();
			String custMsg = "";
			if(score.getScore() < 0) {
				custMsg = "That's dreadful! You have failed your country!";
			}
			else if (score.getScore() < 30) {
				custMsg = "Eh. You're mediocre. Try harder next time.";
			}
			else if (score.getScore() < 60) {
				custMsg = "Good show old chap. Well done indeed!";
			}
			else {
				custMsg = "FANTASTICO!!! You might want to get your arm checked after that clicking spree!";
			}
				
			JOptionPane.showMessageDialog(null, "Time's up! Your score was " + score.getScore() + " points. " + custMsg);
			
			//High score
			boolean isHighScore = false;
			try {
				FileReader filereader = new FileReader("highscores.txt");
				Scanner scanner = new Scanner(filereader);
				if(Integer.parseInt(scanner.nextLine()) < score.getScore()) {
					isHighScore = true;
				}
			}
			catch (FileNotFoundException fnf) {
				// No high score file yet, first game!
				score.createHighScores();
				isHighScore = true;
			}
			if(isHighScore) {
				addHighScore();
			}
			
		}
	}
	
	/**
	 * Set a new high score. Prompt the user for their name first.
	 */
	private void addHighScore() {
		String name = (String) JOptionPane.showInputDialog(null, "Congratulations, you have acheived a high score! Enter your name: ", "New High Score!", JOptionPane.PLAIN_MESSAGE);
		score.addHighScore(score.getScore(), name);
		
	}
	
	/** GUI constructor. Initializes all the GUI elements and timers. */
	public WhackaGUI(int numHoles, int height) {
		this.setSize(1000, height);
		
		gamestate = new GameState(numHoles);
		score = new Score();
		field = new JButton[numHoles];
		panel = new JPanel();
		Box vbox = Box.createVerticalBox();
		timeLeft = 60;
		timelabel = new JLabel("Time Remaining: 60 seconds");
		scorelabel = new JLabel("Score: " + score.getScore());
		pausebtn = new JButton("Pause");
		pausebtn.addActionListener(new pauseIt());
		newgamebtn = new JButton("New Game");
		newgamebtn.addActionListener(new newGame());
		exitbtn = new JButton("Quit");
		exitbtn.addActionListener(new exitclick());
		highscore = new JButton("View High Score");
		highscore.addActionListener(new HighScore());
		Box rowTop = Box.createHorizontalBox();
		rowTop.add(scorelabel);
		rowTop.add(pausebtn);
		rowTop.add(newgamebtn);
		rowTop.add(exitbtn);
		rowTop.add(highscore);
		rowTop.add(timelabel);
		
		vbox.add(rowTop);
		int count = 0;
		Box newbox = Box.createHorizontalBox();
		for(int x = 0; x < field.length; x++) {
			if(count == 0) {
				newbox = Box.createHorizontalBox();
			}
			field[x] = new JButton();
			if(gamestate.getMolecation() == x) {
				field[x].setIcon(mole);
			}
			else {
				field[x].setIcon(hole);
			}
			field[x].addActionListener(new click(x));
			//panel.add(field[x]);
			newbox.add(field[x]);
			count++;
			if(count > 2) {
				vbox.add(newbox);
				count = 0;
			}
		}
		timer = new Timer(1000, new click(-1));
		timer.start();
		onesec = new Timer(1000, new second());
		onesec.start();
		gameTime = new Timer(60000, new timeUp());
		gameTime.start();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		panel.setCursor(toolkit.createCustomCursor(toolkit.getImage(getClass().getResource("/Resources/hammer.png")), new Point(0, 0), "Hammer"));
		panel.add(vbox);
		this.add(panel);
	}
	
	public static void main (String[] args) {
		// Set the number of holes
		//Custom button text
		Object[] options = {"Wimpy (6 Holes)", "Normal (9 Holes)","EXTREME (12 Holes)"};
		int difficulty = JOptionPane.showOptionDialog(null,
		    "Welcome to WHACK-A-SHARK! The object of the game is to hit the shark with the hammer. Avoid the dolphins! \nYou gain 1 point for hitting the shark, lose 5 points for hitting a dolphin, and lose 1 point for do nothing. \nSelect your difficulty level: ",
		    "PICK YOUR POISON",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[2]);
		if(difficulty == -1) { // They did not pick a difficulty
			System.exit(0);
		}
		int numHoles = 6;
		if (difficulty == 1) {
			numHoles = 9;
		}
		if (difficulty == 2) {
			numHoles = 12;
		}
		JFrame frame = new WhackaGUI(numHoles, numHoles * 250 + 50);
		frame.setVisible(true);
	}
}
