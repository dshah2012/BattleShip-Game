package Exceptions;

/**
 * <p>This Custom Exception Class Handles UDP data sent between two Players.
 * This calls the super exception to print the message.</p>
 * @author K3
 *
 */
public class DataNotSentException extends Exception{
	
	/**
	 * Constructor to handle the Message String
	 * @param s - This is a String Message.
	 */
	public DataNotSentException(String s) {
		super(s);
	}

}
