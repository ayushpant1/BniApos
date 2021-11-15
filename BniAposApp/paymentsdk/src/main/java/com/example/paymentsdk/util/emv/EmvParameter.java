package com.example.paymentsdk.util.emv;

/**
 * EMV parameter.
 */

interface EmvParameter {

    String pack() throws EmvParameterException;
}
