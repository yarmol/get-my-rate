package me.jarad.rates.handler;

public class WrongRequestException extends Exception {
    public WrongRequestException() {
        super();
    }

    public WrongRequestException(String message) {
        super(message);
    }

    public WrongRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongRequestException(Throwable cause) {
        super(cause);
    }
}
