package exceptions;

/**
 * Custom Exception class for cases where the section of a Course Class
 * Object is not that of 3 Digits. For other cases, this exception will
 * be thrown.
 * 
 * The reason for this is that Three-lettered section codes or two-lettered
 * codes followed by a number are given to Dual-enrollment classes as well
 * as honors college classes. None of these classes are of importance
 * when looking for classes to create a schedule.
 * 
 * 
 * @author Oscar Reyes
 *
 */
public class InvalidSectionException extends Exception{
	 
	public InvalidSectionException() {}

	  public InvalidSectionException(String message) {
	     super(message);
	  }

}
