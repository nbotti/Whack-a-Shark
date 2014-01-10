package net.whackashark;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameStateTest {

	@Test
	public void testGameState() {
		GameState gs = new GameState(6);
		Assert.assertEquals(6,gs.getNumHoles());
	}

	@Test
	public void testSetNumHoles() {
		GameState gs = new GameState(6);
		gs.setNumHoles(9);
		Assert.assertEquals(9, gs.getNumHoles());
	}

	@Test
	public void testRandomMolecation() {
		GameState gs = new GameState(9);
		int origLoc = gs.getMolecation();
		gs.randomMolecation();
		Assert.assertEquals(true, gs.getMolecation() != origLoc);
	}

}
