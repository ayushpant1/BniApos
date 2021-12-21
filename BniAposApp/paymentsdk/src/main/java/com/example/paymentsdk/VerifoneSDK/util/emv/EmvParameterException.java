package com.example.paymentsdk.VerifoneSDK.util.emv;

/**
 * EMV parameter exception.
 */

public class EmvParameterException extends Exception {
    public EmvParameterException() {
    }

    public EmvParameterException(String message) {
        super(message);
    }

    public EmvParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmvParameterException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
