package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Exceptions.ShipNullCheckException;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * <p>
 * Class where the Board is being Created and all the Cells are Placed.
 * </p>
 * 
 * @author K3
 *
 */

public class Board extends Parent {

	private VBox rows = new VBox();

	private boolean opponent = false;

	public int amountOfships = 5;

	/**
	 * <p>
	 * This Constructor will help in the following
	 * </p>
	 * 
	 * @param opponent
	 *            - Boolean to keep the track of Opponent Board or Player Board
	 * @param handler
	 *            - Handler registration for each cell
	 */
	public Board(boolean opponent, EventHandler<? super MouseEvent> handler) {

		this.opponent = opponent;
		for (int y = 0; y < 10; y++) {

			HBox row = new HBox();

			for (int x = 0; x < 10; x++) {

				Cell c = new Cell(x, y, this);
				c.setOnMouseClicked(handler);
				row.getChildren().add(c);
			}

			rows.getChildren().add(row);
		}

		getChildren().add(rows);
	}

	/**
	 * <p>
	 * Method that helps to Position the Ship.
	 * </p>
	 * <p>
	 * Tasks carried out here are as follows :
	 * </p>
	 * <ol>
	 * <li>Considering the direction for placement.If true then placement of ship is
	 * vertical else its in the horizontal direction</li>
	 * <li>Horizontal thus considering X coordinates and Y if vertical are
	 * considered for ship placement of various type.</li>
	 * <li>Appropriate Placement of ships i.e Checking if the placement is on
	 * opponent grid then placement should not happen else place the type of ship
	 * specified on player 1 grid.</li>
	 * <li>Checking the ship placement validity for horizontal ships.</li>
	 * </ol>
	 * 
	 * @param ship
	 *            - Ship Object which will be placed
	 * @param x
	 *            - Coordinate X of ship
	 * @param y
	 *            - Coordinate Y of ship
	 * @return if the ship is placed correctly or not
	 */
	public boolean positionShip(Ship ship, int x, int y, boolean moveableShip) {

		if (!moveableShip) {
			if (validPlacementShip(ship, x, y)) {
				int length = ship.type;

				if (ship.direction) {

					for (int i = y; i < y + length; i++) {

						Cell cell = getCell(x, i);
						cell.ship = ship;
						

						if (!opponent) {
							cell.setFill(Color.GREEN);
							cell.setStroke(Color.BLACK);
						}

					}
				} else {

					for (int i = x; i < x + length; i++) {

						Cell cell = getCell(i, y);
						cell.ship = ship;
						
						if (!opponent) {

							cell.setFill(Color.GREEN);
							cell.setStroke(Color.BLACK);

						}

					}
				}

				return true;
			}
			return false;
		} else {

			int length = ship.type;

			if (ship.direction) {

				for (int i = y; i < y + length; i++) {

					Cell cell = getCell(x, i);
					
					if(cell.ship!=null) {
						cell.ship = null;

						if (!opponent) {

							cell.setFill(Color.WHITE);
							cell.setStroke(Color.BLACK);

						}
					}
					else {
						return false;
					}

				}
			} else {

				for (int i = x; i < x + length; i++) {

					Cell cell = getCell(i, y);
					
					if(cell.ship!=null) {
						cell.ship = null;

						if (!opponent) {

							cell.setFill(Color.WHITE);
							cell.setStroke(Color.BLACK);

						}
					}
					else {
						return false;
					}

				}
			}
			return true;
		}

	}

	/**
	 * This will get the Cell Object for a particular Coordinate
	 * 
	 * @param x
	 *            -Coordinate X
	 * @param y
	 *            -Coordinate Y
	 * @return -gets the Cell Object
	 */
	public Cell getCell(int x, int y) {

		return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);

	}

	/**
	 * This method will check and get the neighbors and checks for validity.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Cell[] checkAndGetNeighbors(int x, int y) {

		Point2D[] points = new Point2D[] {

				new Point2D(x - 1, y), 
				new Point2D(x + 1, y), 
				new Point2D(x, y - 1),
				new Point2D(x, y + 1), 
				new Point2D(x - 1, y - 1), 
				new Point2D(x + 1, y + 1), 
				new Point2D(x - 1, y + 1),
				new Point2D(x + 1, y - 1) 
		};

		List<Cell> neighbors = new ArrayList<Cell>();

		for (Point2D p : points) {
			if (isValidPoint(p)) {
				neighbors.add(getCell((int) p.getX(), (int) p.getY()));
			}
		}

		return neighbors.toArray(new Cell[0]);
	}

	/**
	 * Checking constraints i.e. all cells that form ships are within the grids,
	 * also tracking its associated neighboring cells are within the grid for
	 * further hitting related cells.
	 * 
	 * @param ship
	 *            it takes type(size) of ship along with direction whether the ship
	 *            placed is vertical or horizontal.
	 * @param x
	 *            X coordinate for placement of ship
	 * @param y
	 *            Y coordinate for placement of ship
	 * @return
	 */
	public boolean validPlacementShip(Ship ship, int x, int y) {

		int length = ship.type;

		if (ship.direction) {
			// Checking the ship placement validity for vertical ships.
			for (int i = y; i < y + length; i++) {
				if (!isValidPoint(x, i))
					return false;

				Cell cell = getCell(x, i);
				if (cell.ship != null)
					return false;

				for (Cell neighbor : checkAndGetNeighbors(x, i)) {
					if (!isValidPoint(x, i))
						return false;

					if (neighbor.ship != null)
						return false;
				}
			}
		} else {
			// Checking the ship placement validity for horizontal ships.
			for (int i = x; i < x + length; i++) {
				if (!isValidPoint(i, y))
					return false;

				Cell cell = getCell(i, y);
				if (cell.ship != null)
					return false;

				for (Cell neighbor : checkAndGetNeighbors(i, y)) {
					if (!isValidPoint(i, y))
						return false;

					if (neighbor.ship != null)
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * Same method OverLoading Technique used as the parameters is a point Object
	 * 
	 * @param point
	 *            takes x and y coordinate of the ship together as a 2D point and
	 *            checks their placement validity i.e if they are properly placed
	 *            within the grid,two ships coordinates do not interfere etc.
	 * @return
	 */
	public boolean isValidPoint(Point2D point) {
		return isValidPoint(point.getX(), point.getY());
	}

	/**
	 * Method that will help to validate the Point if it is in the Board
	 * 
	 * @param x
	 *            X coordinate of ship
	 * @param y
	 *            Y coordinate of ship
	 * @return
	 */
	private boolean isValidPoint(double x, double y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}

	public static class Cell extends Rectangle {

		public int row, col;

		public Ship ship = null;

		public boolean targetHit = false;

		public Board board;

		/**
		 * 
		 * @param x
		 *            X coordinate of ship
		 * @param y
		 *            Y coordinate of ship
		 * @param board
		 *            checks for opponent or player 1 board and accordingly does further
		 *            mouse events for playing the game.
		 */
		public Cell(int x, int y, Board board) {

			super(30, 30);
			this.row = x;
			this.col = y;
			this.board = board;

			setFill(Color.WHITE);
			setStroke(Color.BLACK);
			setArcWidth(10.0);
			setArcHeight(10.0);
			

		}

		/**
		 * This will help to check if the was on target or not
		 * 
		 * @return boolean
		 */
		public boolean shoot() {



			setFill(Color.BLACK);

			if (ship != null) {
				
				targetHit = true;
				ship.shipPartHit();
				ship.shotCellsOfShips.add(this);
				
				File hitRate = new File(".");

				/**
				 * Prints new image of hitship.
				 * <p>
				 * If one ship is destroyed completely it gets highlighted. This is done by:
				 * <code> if (!ship.shipIsAlive()) { for (Cell c : ship.shotCellsOfShips) {
				 * c.setFill(Color.RED); } board.amountOfships--; ship.shotCellsOfShips.clear();
				 * </p>
				 */

				Image hitFile = null;
				try {
					hitFile = new Image("file:///" + hitRate.getCanonicalFile() + "/hitimg.png");

					/**
					 * @throws io
					 *             exception
					 */
				} catch (IOException e) {

					e.printStackTrace();
				}

			
				setFill(new ImagePattern(hitFile));

				if (!ship.shipIsAlive()) {
					for (Cell c : ship.shotCellsOfShips) {

						c.setFill(Color.RED);
					}

					if (board.opponent) {
						Battle.shipDestructionMessage("OPPONENT", 1000, 600);
					} else {
						Battle.shipDestructionMessage("PLAYER", 500, 600);
					}

					if (board.opponent)
						Battle.player1Score += 20;
					else
						Battle.player2Score += 20;

					board.amountOfships--;
					ship.shotCellsOfShips.clear();
				}
				return true;
			}
			else if(ship == null) {
				try {
					throw new ShipNullCheckException("There is no ship present in this cell");
				} catch ( ShipNullCheckException e) {
					System.out.println("Unchecked Exception "+ e);
				}
				
			}

			return false;
		}

	}

}
