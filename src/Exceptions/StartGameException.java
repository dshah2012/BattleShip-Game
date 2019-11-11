package Exceptions;

/**
 * <p>This Custom Exception Class Handles the player to player Game Scenario.
 * If one player starts before the other player then this exception is thrown . </p> 
 * @author K3
 *
 */
public class StartGameException extends Exception {
	
	/**
	 * Constructor to handle the Message String
	 * @param s - This is a String Message.
	 */
	public StartGameException(String s) {
		super(s);
	}
}
