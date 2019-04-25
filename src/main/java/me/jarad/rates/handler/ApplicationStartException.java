package me.jarad.rates.handler;

public class ApplicationStartException extends RuntimeException {
    public ApplicationStartException() {
        super();
    }

    public ApplicationStartException(String message) {
        super(message);
    }

    public ApplicationStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationStartException(Throwable cause) {
        super(cause);
    }
}
