package by.siarhei.devByDataParser.http.exception;

public class RequestSendException extends Exception {
    public RequestSendException() {
    }

    public RequestSendException(String message) {
        super(message);
    }

    public RequestSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestSendException(Throwable cause) {
        super(cause);
    }
}
