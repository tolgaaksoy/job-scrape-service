package io.jobmarks.jobscrapeservice.indeed.exception;

public class IndeedException extends RuntimeException {

    public IndeedException(String message) {
        super(message);
    }

    public IndeedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndeedException(Throwable cause) {
        super(cause);
    }

    public IndeedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
