package net.yangziwen.httptest.exception;

@SuppressWarnings("serial")
public class HttpTestException extends RuntimeException {

	private int errorId;
	private String errorMsg;

	public int getErrorId() {
		return errorId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	protected HttpTestException(int errorId, String errorMsg) {
		this.errorId = errorId;
		this.errorMsg = errorMsg;
	}
	
	protected HttpTestException(int errorId, String errorMsg, Object... args) {
		this(errorId, args != null? String.format(errorMsg, args): errorMsg);
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
	
	public static NotExistException notExistException(String errorMsg, Object... args) {
		return new NotExistException(errorMsg, args);
	}
	
	public static OperationFailedException operationFailedException(String errorMsg, Object... args) {
		return new OperationFailedException(errorMsg, args);
	}
	
	public static InvalidParameterException invalidParameterException(String errorMsg, Object... args) {
		return new InvalidParameterException(errorMsg, args);
	}
	
	public static UnauthorizedException unauthorizedException(String errorMsg, Object... args) {
		return new UnauthorizedException(errorMsg, args);
	}
	
	public static IllegalUrlException illegalUrlException (String errorMsg, Object... args) {
		return new IllegalUrlException(errorMsg, args);
	}
	
	// ----------------------------------- //
	
	static class NotExistException extends HttpTestException {
		protected NotExistException(String errorMsg, Object... args) {
			super(10001, errorMsg, args);
		}
	}
	
	static class OperationFailedException extends HttpTestException {
		protected OperationFailedException(String errorMsg, Object... args) {
			super(10002, errorMsg, args);
		}
	}
	
	static class InvalidParameterException extends HttpTestException {
		protected InvalidParameterException(String errorMsg, Object... args) {
			super(10003, errorMsg, args);
		}
	}
	
	static class UnauthorizedException extends HttpTestException {
		protected UnauthorizedException(String errorMsg, Object... args) {
			super(10004, errorMsg, args);
		}
	}
	
	static class IllegalUrlException extends HttpTestException {
		protected IllegalUrlException(String errorMsg, Object... args) {
			super(20001, errorMsg, args);
		}
		
	}
}
