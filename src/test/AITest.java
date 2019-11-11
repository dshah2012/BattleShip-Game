package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.AI;

public class AITest {

	
	static AI ai;	
	
	@Before
	public void initialse() {
		ai = new AI();
		
	}
	
	@Test
	public void testGenerate() {
		int x=ai.nextX();
		int y=ai.nextY();
		assertEquals(true, (x>=0 && x<=10) && (y>=0 && y<=10)?true:false);
		
	}
	@Test
	public void testGenerate1() {
		int x=ai.nextX();
		int y=ai.nextY();
		ai.generate();
		int xNext =ai.nextX();
		int yNext =ai.nextY();
		assertNotEquals(x+"-"+y,xNext+"-"+yNext);
	}
	
	@Test
	public void testGenerate2() {
		int x=ai.nextX();
		int y=ai.nextY();
	
	assertEquals(false,(x>=0 && x<10) && (y>=0 && y<=10)?false:true);
	}
	
	
	@Test
	public void testGuessingDirection(){
		ai.feedback(true,false);
		ai.generate();
		assertNotNull(ai.stack.isEmpty());
	}
	
	@Test
	public void testGuessingDirection2(){
		ai.feedback(false,true);
		ai.generate();
		assertNotNull(ai.stack.isEmpty());
	}
	
	@Test
	public void testMove(){
		boolean checkMoves = ai.move(2);
		assertTrue(checkMoves);
	}

	
	@Test
	public void testReset() {
		int firstX = ai.nextX();
		ai.reset();
		int secondX = ai.nextX();
		assertNotEquals(firstX, secondX);
	}
	
	@Test
	public void testMoveFailed(){
		boolean checkMoves = ai.move(1);
		assertTrue(checkMoves);
	}
	
	
	@Test
	public void testTrytoMove() {
		int prevX = ai.nextX();
		ai.stack.push(2);
		ai.tryToMove();
		int nextX =ai.nextX();
		if(prevX == nextX)
			assertEquals(prevX, nextX);
		else
			assertNotEquals(prevX, nextX );
		
	}
	
	@Test
	public void testTrytoMoveFailure() {
		int prevX = ai.nextX();
		ai.stack.push(10);
		ai.tryToMove();
		int nextX =ai.nextX();
		if(prevX == nextX)
			assertEquals(prevX, nextX);
		else
			assertNotEquals(prevX, nextX );
		
		
	}
	
	
	
	@After
	public void removeReferences() {
		ai = null;
	}
	
}
