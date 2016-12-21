package exceptions;

public class NullClassFieldException extends Exception {
	public NullClassFieldException() {}

  public NullClassFieldException(String message) {
     super(message);
  }
}
