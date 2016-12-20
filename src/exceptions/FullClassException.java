package exceptions;

/**
 * Custom Exception class for cases where a CourseClass object has no spaces left.
 * In cases such as the aforementioned, this exception will be thrown.
 * 
 * @author Oscar Reyes
 *
 */
public class FullClassException extends Exception{
	
  public FullClassException() {}

  public FullClassException(String message) {
     super(message);

  }

}
