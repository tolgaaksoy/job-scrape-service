package io.jobmarks.jobscrapeservice.linkedin.exception;

public class LinkedInException extends RuntimeException {

    public LinkedInException() {
    }

    public LinkedInException(String message) {
        super(message);
    }

    public LinkedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public LinkedInException(Throwable cause) {
        super(cause);
    }

    public LinkedInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
