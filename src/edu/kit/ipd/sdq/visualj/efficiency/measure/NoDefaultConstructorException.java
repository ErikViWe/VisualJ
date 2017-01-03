package edu.kit.ipd.sdq.visualj.efficiency.measure;

/**
 * This exception is thrown when the library tries to default-construct an
 * object of a class that has no public default constructor.
 */
public class NoDefaultConstructorException extends RuntimeException {
    
    private static final long serialVersionUID = 7412203981023000140L;
    
    public NoDefaultConstructorException() {
        super();
    }
    
    public NoDefaultConstructorException(String message) {
        super(message);
    }
    
    public NoDefaultConstructorException(Throwable cause) {
        super(cause);
    }
    
    public NoDefaultConstructorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NoDefaultConstructorException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
