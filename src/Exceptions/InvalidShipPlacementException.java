package Exceptions;

/**
 * <p>This Custom Exception Class Handles InvalidShipPlacements .If the ship is
 * not placed on the board or a invalid position. <p> 
 * @author K3
 *
 */
public class InvalidShipPlacementException extends Exception{

	/**
	 * Constructor to handle the Message String
	 * @param s - This is a String Message.
	 */
	public InvalidShipPlacementException(String s){  
		  super(s);  
	}  
}
