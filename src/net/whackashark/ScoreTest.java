package net.whackashark;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScoreTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		Score score = new Score();
		Assert.assertEquals(0, score.getScore());
	}
	
	@Test
	public void testAddPoint(){
		Score score = new Score();
		score.addPoint();
		Assert.assertEquals(1, score.getScore());
	}
	
	@Test
	public void testTakePoint(){
		Score score = new Score();
		score.takePoint();
		Assert.assertEquals(-1, score.getScore());
	}
	
	@Test
	public void testResetScore(){
		Score score = new Score();
		score.takePoint();
		score.resetScore();
		Assert.assertEquals(1, score.getScore());
	}

	@Test
	public void testCreateHighScore(){
		Score score = new Score();
		score.createHighScores();
		score.addHighScore(72, "Katie");
		Assert.assertEquals(true, !score.getHighScore().equals("No high score yet.")); ;
	}
}
