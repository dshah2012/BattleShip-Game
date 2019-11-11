package Exceptions;


/**
 * <p>This Custom Exception Class Handles All the Grid Bound Exceptions </p>
 * @author K3
 *
 */
public class BeyondGridBoundsException extends Exception{
	
	/**
	 * Constructor to handle the Message String
	 * @param s - This is a String Message.
	 */
	public BeyondGridBoundsException(String s) {
		super(s);
	}
}
