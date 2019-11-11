package test;




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import application.Ship;

public class ShipTest {
	static Ship ship;
	int hit;

	
	
	@BeforeClass
	public static  void check() {
		ship =new Ship(1,true);
		
	}
	
	
	@Test
	public void testShipIsAliveSuccess() {
		boolean checkAlive =ship.shipIsAlive();
		assertTrue(checkAlive);
		
	}
	/**
	 * Test method for shiphitsucess.
	 */
	
	
	@Test
	public void testshipPartHitSuccess() {
		ship.percentageDestroyed++;
		ship.shipPartHit();
		assertEquals(1, ship.percentageDestroyed);
	}
	
	@Test
	public void testshipPartHitSuccess2() {
		ship.percentageDestroyed++;
		ship.shipPartHit();
		assertNotEquals(2, ship.percentageDestroyed);
	}
	
	@Test
	public void testShipIsAliveFailure() {
		ship.percentageDestroyed -= 1;
	
		boolean checkAlive =ship.shipIsAlive();
		assertFalse(checkAlive);
	}
	
	@Test
	public void testShipPartHit() {
		Ship s = new Ship(5, true);
		s.percentageDestroyed-=5;
		s.shipPartHit();
		assertNotEquals(-1,s.percentageDestroyed);
	}
	
	@AfterClass
	public static void removeReferences() {
		ship=null;
	}
	

	
	

}
