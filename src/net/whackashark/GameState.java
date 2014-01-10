package net.whackashark;

import java.util.Random;

/**
 * Class to represent the game state
 * @author bottin
 * @author welchk
 * @version 2
 *
 */
public class GameState {
	private int numHoles;
	private int moleLocation;
	
	/**
	 * Initialise the game state
	 * @param holes the number of holes, input by the user via dialogue box
	 */
	public GameState(int holes) {
		numHoles = holes;
		randomMolecation();
	}
	
	/**
	 * Set the number of holes for the game
	 * @param holes the desired number of holes
	 */
	public void setNumHoles(int holes) {
		numHoles = holes;
	}
	
	/**
	 * Randomly change the mole's location
	 */
	public void randomMolecation() {
		Random rnd = new Random();
		int oldLoc = moleLocation;
		moleLocation = rnd.nextInt(numHoles);
		if(oldLoc == moleLocation) {
			randomMolecation();
		}
	}
	
	/**
	 * Get the current location of the mole
	 * @return the mole's current location
	 */
	public int getMolecation() {
		return moleLocation;
	}
	
	/**
	 * Get the number of holes in the game
	 * @return the number of holes
	 */
	public int getNumHoles(){
		return numHoles;
	}
}
