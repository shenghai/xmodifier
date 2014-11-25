package org.shai.xmodifier.exception;

/**
 * Created by Shenghai on 14-11-24.
 */
public class XModifyFailException extends RuntimeException {
    public XModifyFailException() {
    }

    public XModifyFailException(String message) {
        super(message);
    }

    public XModifyFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public XModifyFailException(Throwable cause) {
        super(cause);
    }

    public XModifyFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
