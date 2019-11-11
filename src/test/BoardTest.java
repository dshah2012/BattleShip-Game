package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.Board;
import application.Ship;
import application.Board.Cell;
import javafx.geometry.Point2D;

/**
 * @author k3
 *
 */

public class BoardTest {

	static Board board;
	static Cell cell;
	
	
	@Before
	public  void check() {
		board = new Board(false, null);
	
		cell = new Cell(3, 4,board);
		
	
	}
	
	@After
	public  void clear() {
		board =null;
		cell = null;
	}
	
	
	
	/**
	*test method for valid point test
	*/
	
	@Test
	public void IsValidPointTest()
	{
		
		Point2D p = new Point2D(3, 4);
		assertEquals(true, board.isValidPoint(p));
	
	}
	
	
	@Test
	public void IsInValidPointTest()
	{
		
		Point2D p = new Point2D(-1, 5);
		assertEquals(false, board.isValidPoint(p));
	
	}
	/**
	 * Test method for invalidpoint.
	 */
	
	
	@Test
	public void IsInValidPointTest2()
	{
		
		Point2D p = new Point2D(0, 20);
		assertEquals(false, board.isValidPoint(p));
	
	}
	/**
	 * Test method for positionship.
	 */
	
	@Test
	public void testPositionShip() {
		Ship s = new Ship(2, true);
		
		boolean placed = board.positionShip(s, 3, 5, false);
		
		assertTrue(placed);
	}
	
	
	@Test
	public void testInvShipPos() {
		Ship s = new Ship(5, true);
		
		boolean placed = board.positionShip(s, 20, 5, false);
		
		assertFalse(placed);
	}
	
	@Test
	public void testHorizShipPos() {
		
		Ship s = new Ship(5, false);
		
		boolean placed = board.positionShip(s, 5, 5, false);
		
		assertTrue(placed);
	}
	
	
	@Test
	public void testInvHorizShipPos() {
		
		Ship s = new Ship(5, false);
		
		boolean placed = board.positionShip(s, 5, 12, false);
		
		assertFalse(placed);
	}
	
	
	@Test
	public void testMovableShip() {
		
		Ship s = new Ship(5, false);
		board.positionShip(s, 5, 4 , false);
		boolean removed = board.positionShip(s, 5, 4, true);
		assertTrue(removed);
	}
	
	@Test
	public void testMovableShip2() {
		
		Ship s = new Ship(1, false);
		board.positionShip(s, 0, 0 , false);
		boolean removed = board.positionShip(s, 0, 0, true);
		assertTrue(removed);
	}
	
	@Test
	public void testInvMovableShip() {
		
		Ship s1 = new Ship(3, false);
		board.positionShip(s1, 5, 4 , false);
		boolean removed = board.positionShip(s1, 3, 6 , true);
		assertFalse(removed);
	}
	
	

	@Test
	public void testInvMovableShip2() {
		
		Ship s1 = new Ship(3, false);
		boolean removed = board.positionShip(s1, 5, 6, true);
		assertFalse(removed);
	}
	
	@Test
	public void testInvMovableShip3() {
		
		Ship s1 = new Ship(1, false);
		board.positionShip(s1, 9, 9 , false);
		boolean removed = board.positionShip(s1, 8, 9, true);
		assertFalse(removed);
	}
	
	@Test
	public void testShoot() {
		
		Cell c = new Cell(6, 1, board);
		c.ship = null;
		boolean isShipHit =c.shoot();
		assertFalse(isShipHit);
		
	}

	@Test
	public void testShoot1() {
		
		Cell c = new Cell(8, 1, board);
		c.ship = null;
		boolean isShipHit =c.shoot();
		assertFalse(isShipHit);
		
	}

	
	@Test
	public void testShoot3() {
		
		Cell c = new Cell(3, 1, board);
		c.ship = null;
		boolean isShipHit =c.shoot();
		assertFalse(isShipHit);
		
	}

	@Test
	public void testShoot2() {
		
		Cell c = new Cell(0, 5, board);
		c.ship = null;
		boolean isShipHit =c.shoot();
		int a = board.amountOfships;
		assertEquals(5, a);
	}
	
	@Test
	public void testGetCell() {
		
		Cell ce = board.getCell(3, 9);
		assertNotNull(ce);
	}
	
	
	@Test
	public void testGetCell2() {
		
		Cell ce = board.getCell(9, 9);
		assertNotNull(ce);
	}
	
	
	@Test
	public void testGetCell3() {
		
		Cell ce = board.getCell(4, 5);
		assertNotNull(ce);
	}
	
	
	@Test
	public void testGetCell4() {
		
		Cell ce = board.getCell(5, 5);
		assertNotNull(ce);
	}
	

	@Test
	public void testValidPlacementMethod() {
		Ship s = new Ship(3, false);
		Ship s1 = new Ship(2,true);
		board.getCell(2, 8).ship = s1;
		boolean valid =board.validPlacementShip(s, 1, 8);
		assertFalse(valid);
	}
	
	
	@Test
	public void testValidPlacementMethodValid() {
		Ship s = new Ship(3, false);
		boolean valid =board.validPlacementShip(s, 1, 8);
		assertTrue(valid);
	}
	
	
	@Test
	public void testValidPlacementMethodValidCheck() {
		Ship s = new Ship(4, true);
		boolean valid =board.validPlacementShip(s, 1, 5);
		assertTrue(valid);
	}
	
	@Test
	public void testValidPlacementMethod2() {
		
		Ship s1 = new Ship(3,false);
		board.positionShip(s1, 7, 9, false);
		Ship s = new Ship(5, false);
		boolean valid =board.validPlacementShip(s, 3, 9);
		assertFalse(valid);
	}
	
	

}
