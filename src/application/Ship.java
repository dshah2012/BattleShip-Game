package application;

import javafx.scene.Parent;

import java.util.ArrayList;

import application.Board.Cell;

/**
 * 
 * This is a POJO class which will help us build the ship Object that needs to be placed on the Board.

 */
public class Ship extends Parent {


	ArrayList<Cell> shotCellsOfShips = new ArrayList<Cell>();

	public int type;
	public boolean direction = true;
	public int percentageDestroyed;

	/**
	 * Constructor for initializing the Object.
	 * Other tasks carried out here are:
	 * <ol>
	 * <li>shipPartHit() to check for health of ship</li>
	 * <li> shipIsAlive() to check ship is alive or not</li>
	 * </ol>
	 * @param type - Length of the Ship
	 * @param vertical - Checks for Vertical or Horizontal
	 */
	public Ship(int type, boolean vertical) {


		this.type = type;
		this.direction = vertical;
		percentageDestroyed = type;

	}

	/**
	 * This method decrements the ship health by one each time a ship is hit
	 */
	public void shipPartHit() {
		if(percentageDestroyed>0) {
			percentageDestroyed--;
		}

	}


	/**
	 *check wheather ship is alive or destroyed
	 * @return true if the ship is alive or false if the ship has been destroyed
	 */
	public boolean shipIsAlive() {
		return percentageDestroyed > 0;
	}
}
