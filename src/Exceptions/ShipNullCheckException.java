package Exceptions;

/**
 * <p>This Custom Exception Class Handles Null Ship Exception.
 * If the ship is present at a particular cell or not .
 * If not then the exception is raised.</p>
 * @author K3
 *
 */
public class ShipNullCheckException extends Exception{
	
	/**
	 * Constructor to handle the Message String
	 * @param s - This is a String Message.
	 */
		public ShipNullCheckException(String s) {
			super(s);
		}
}
