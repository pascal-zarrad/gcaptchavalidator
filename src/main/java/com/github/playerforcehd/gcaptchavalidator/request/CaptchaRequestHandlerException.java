package com.github.playerforcehd.gcaptchavalidator.request;

/**
 * An exception thrown as wrapper for any exceptions that might occur during a request.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class CaptchaRequestHandlerException extends Exception {
    public CaptchaRequestHandlerException() {
    }

    public CaptchaRequestHandlerException(String message) {
        super(message);
    }

    public CaptchaRequestHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaRequestHandlerException(Throwable cause) {
        super(cause);
    }

    public CaptchaRequestHandlerException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
