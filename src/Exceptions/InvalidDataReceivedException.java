package Exceptions;

/**
 *<p> This Custom Exception Class Handles data Checking Exception if the 
 * UDP send and Receive Method has the same Data.If not Then exception is thrown.</p> 
 * @author K3
 *
 */
public class InvalidDataReceivedException extends Exception{
	
	/**
	 * Constructor to handle the Message String
	 * @param s - This is a String Message.
	 */
	public InvalidDataReceivedException(String s) {
		super(s);
	}
}
