package com.example.paymentsdk.VerifoneSDK.util.emv;

/**
 * EMV parameter.
 */

interface EmvParameter {

    String pack() throws EmvParameterException;
}
